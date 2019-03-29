package com.ldz.service.biz.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ldz.service.biz.interfaces.ClYhService;
import com.ldz.dao.biz.mapper.ClYhMapper;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.bean.AccessToken;
import com.ldz.sys.bean.userInfoModel;
import com.ldz.sys.model.SysJg;
import com.ldz.sys.model.SysZdlm;
import com.ldz.sys.model.SysZdxm;
import com.ldz.sys.service.JgService;
import com.ldz.sys.service.ZdlmService;
import com.ldz.sys.service.ZdxmService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.EncryptUtil;
import com.ldz.util.commonUtil.JwtUtil;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.util.redis.RedisTemplateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.common.Mapper;

import com.ldz.dao.biz.model.ClYh;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClYhServiceImpl extends BaseServiceImpl<ClYh, String> implements ClYhService {

    @Autowired
    private ClYhMapper baseMapper;
    @Autowired
    private ZdxmService zdxmService;
    @Autowired
    private ZdlmService zdlmService;
    @Autowired
    private RedisTemplateUtil redisDao;
    @Autowired
    private JgService jgService;
    private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    protected Mapper<ClYh> getBaseMapper() {
        return baseMapper;
    }

    // 4DA3BB20330A34F4
    @Override
    public ApiResponse<String> regist(String userName, String password, String password1) {
        RuntimeCheck.ifBlank(password, "密码不能为空");
        RuntimeCheck.ifBlank(password1, "确认密码不能为空");
        RuntimeCheck.ifFalse(StringUtils.equals(password.trim(), password1.trim()), "两次密码输入不一致");
        RuntimeCheck.ifBlank(userName, "请输入账号");
        HttpServletRequest request = getRequset();
        String clientId = request.getHeader("clientId");
        SimpleCondition condition = new SimpleCondition(ClYh.class);
        condition.eq(ClYh.InnerColumn.username, userName);
//		condition.eq(ClYh.InnerColumn.clientId,clientId);
        List<ClYh> yhs = findByCondition(condition);

//		ClYh clYh = findById(userName);
        RuntimeCheck.ifTrue(CollectionUtils.isNotEmpty(yhs), "当前用户名已存在");

        String userPwd = EncryptUtil.encryptUserPwd(password.trim());
        ClYh yh = new ClYh();
        yh.setId(genId());
        yh.setSource("app");
        yh.setUsername(userName);
        yh.setPassword(userPwd);
        yh.setClientId(clientId);
        yh.setNickName(userName);
        yh.sethImg("h_img/tot.png");
        save(yh);
        return ApiResponse.success("注册成功");
    }

    @Override
    public ApiResponse<Map<String, Object>> login(String userName, String password) {
        RuntimeCheck.ifBlank(userName, "账号不能为空");
        RuntimeCheck.ifBlank(password, "密码不能为空");
        return commonLogin(userName, password);
    }

    @Override
    public ApiResponse<String> editPwd(String userName, String oldPwd, String newPwd, String newPwd1) {
        RuntimeCheck.ifBlank(userName, "用户名不能为空");
        RuntimeCheck.ifBlank(oldPwd, "原始密码不能为空");
        RuntimeCheck.ifBlank(newPwd, "新密码不能为空");
        RuntimeCheck.ifBlank(newPwd1, "确认密码不能为空");
        RuntimeCheck.ifTrue(!StringUtils.equals(newPwd1.trim(), newPwd.trim()), "两次密码不一致");
//        HttpServletRequest request = getRequset();
//		String clientId = request.getHeader("clientId");
        SimpleCondition condition = new SimpleCondition(ClYh.class);
        condition.eq(ClYh.InnerColumn.username, userName);

//		condition.eq(ClYh.InnerColumn.clientId,clientId);
        List<ClYh> yhs = findByCondition(condition);
        RuntimeCheck.ifTrue(CollectionUtils.isEmpty(yhs), "用户不存在");

        ClYh yh = yhs.get(0);
        RuntimeCheck.ifTrue(!StringUtils.equals(EncryptUtil.encryptUserPwd(oldPwd.trim()), yh.getPassword()), "原始密码错误");
        String userPwd = EncryptUtil.encryptUserPwd(newPwd.trim());
        yh.setPassword(userPwd);
        update(yh);
        return ApiResponse.success("修改成功");
    }

    @Override
    public ApiResponse<Map<String, Object>> getUserInfo() {
        HttpServletRequest requset = getRequset();
        String userId = requset.getHeader("user_id");
        ClYh item = findById(userId);
        RuntimeCheck.ifNull(item, "没有找到用户信息");
        Map<String, Object> rMap = new HashMap<>(2);
        ApiResponse<Map<String, Object>> result = new ApiResponse<>();
        try {
            loginInfo(rMap, result, item);
            return result;
        } catch (Exception e) {
            result.setCode(500);
            result.setMessage("未找到用户信息");
            return result;
        }

    }

    @Override
    public ApiResponse<Map<String, Object>> updateUserInfo(ClYh entity) {
        HttpServletRequest requset = getRequset();
        String userId = requset.getHeader("user_id");
        ClYh yh = new ClYh();
        BeanUtils.copyProperties(entity, yh, "username", "password","clientId","source");
        yh.setId(userId);
        update(yh);
        return getUserInfo();
    }

    private ApiResponse<Map<String, Object>> commonLogin(String userName, String password) {
        //加密密码
        try {
            password = EncryptUtil.encryptUserPwd(password.trim());
        } catch (Exception e1) {
            throw new RuntimeException("密码加密异常", e1);
        }

        Example condition = new Example(ClYh.class);
        condition.and()
                .andEqualTo(ClYh.InnerColumn.username.name(), userName)
                .andEqualTo(ClYh.InnerColumn.password.name(), password);
        List<ClYh> existUser = findByCondition(condition);
        Map<String, Object> rMap = new HashMap<>(2);
        ApiResponse<Map<String, Object>> result = new ApiResponse<>();
        if (existUser != null && existUser.size() > 0) {
            ClYh item = existUser.get(0);

            try {
                String token = JwtUtil.createToken(item.getUsername(), DateUtils.getNowTime());
                redisDao.boundValueOps(item.getUsername()).set(token);
                AccessToken aToken = new AccessToken();
                aToken.setUserId(item.getId());
                aToken.setUsername(item.getUsername());
                aToken.setToken(token);
                rMap.put("accessToken", aToken);
                loginInfo(rMap, result, item);
                // 获取字典项
                List<SysZdlm> zdlmList = zdlmService.findAll();
                getZdxm(zdlmList);
                rMap.put("dictList", zdlmList);
            } catch (Exception e) {
                result.setCode(ApiResponse.FAILED);
                result.setMessage("用户登陆失败，请重试！");
                return result;
            }
        } else {
            result.setCode(ApiResponse.FAILED);
            result.setMessage("用户名或密码不正确！");
            return result;
        }
        return result;
    }

    private void loginInfo(Map<String, Object> rMap, ApiResponse<Map<String, Object>> result, ClYh item) throws JsonProcessingException {

        redisDao.boundValueOps(item.getUsername() + "-userInfo").set(mapper.writeValueAsString(item));

        userInfoModel userInfomodel = new userInfoModel();
        userInfomodel.setHImg(item.gethImg());
        userInfomodel.setGender(item.getGender());
        userInfomodel.setBirthDay(item.getBirthDay());
        userInfomodel.setAddress(item.getAddress());
        userInfomodel.setEMail(item.geteMail());
        userInfomodel.setEduRecoed(item.getEduRecoed());
        userInfomodel.setDImg(item.getdImg());
        userInfomodel.setDCode(item.getdCode());
        userInfomodel.setDDate(item.getdDate());
        userInfomodel.setDNo(item.getdNo());
        userInfomodel.setXm(item.getNickName());
        userInfomodel.setYhid(item.getId());
        userInfomodel.setType(item.getSource());
        if (StringUtils.isNotBlank(item.getJgdm())) {
            userInfomodel.setJgdm(item.getJgdm());
            SysJg org = jgService.findByOrgCode(item.getJgdm());
            if (org != null) {
                rMap.put("jgmc", org.getJgmc());
            }
        }
        rMap.put("userInfo", userInfomodel);

        result.setResult(rMap);
    }

    private void getZdxm(List<SysZdlm> list) {
        List<String> lmdms = list.stream().map(SysZdlm::getLmdm).collect(Collectors.toList());
        List<SysZdxm> zdxms = zdxmService.findByZdlms(lmdms);
        Map<String, SysZdlm> zdlmMap = list.stream().collect(Collectors.toMap(SysZdlm::getLmdm, p -> p));
        for (SysZdxm zdxm : zdxms) {
            SysZdlm zdlm = zdlmMap.get(zdxm.getZdlmdm());
            if (zdlm == null) continue;
            if (zdlm.getZdxmList() == null) {
                List<SysZdxm> zdxmList = new ArrayList<>();
                zdxmList.add(zdxm);
                zdlm.setZdxmList(zdxmList);
            } else {
                zdlm.getZdxmList().add(zdxm);
            }
        }
    }
}
