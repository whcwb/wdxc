package com.ldz.sys.service.impl;

import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.sys.constant.Dict;
import com.ldz.sys.service.GnService;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.sys.mapper.*;
import com.ldz.sys.model.*;
import com.ldz.sys.service.JgService;
import com.ldz.sys.service.JsService;
import com.ldz.sys.service.YhService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.Des;
import com.ldz.util.commonUtil.EncryptUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class YhServiceImpl extends BaseServiceImpl<SysYh, String> implements YhService {
	Logger log = LogManager.getLogger(this);
	@Value("${resePwd:111111}")
	private String resePwd;
	@Autowired
	private SysClkPtyhMapper baseMapper;
	@Autowired
	private GnService gnService;
	@Autowired
	private JgService jgService;
	@Autowired
	private JsService jsService;
	@Autowired
	private SysJsGnMapper jsGnMapper;
	@Autowired
	private SysYhJsMapper yhJsMapper;

	@Override
	protected Class<SysYh> getEntityCls(){
		return SysYh.class;
	}

	@Override
	protected Mapper<SysYh> getBaseMapper() {
		return baseMapper;
	}

	/**
	 * 新增用户
	 *
	 * @param user 用户
	 * @return 执行结果
	 */
	@Override
	public ApiResponse<String> validAndSave(SysYh user) {
		RuntimeCheck.ifBlank(user.getZh(),"账号不能为空");
		RuntimeCheck.ifBlank(user.getXm(),"姓名不能为空");
		RuntimeCheck.ifBlank(user.getSjh(),"手机号不能为空");
		RuntimeCheck.ifFalse(StringUtils.isAlphanumeric(user.getZh()),"登陆名只能是数字和字母组成！");
		boolean exists = ifExists(SysYh.InnerColumn.zh.name(),user.getZh());
		RuntimeCheck.ifTrue(exists,"登陆名已存在，请更换别的登陆名！");
//		SysJg org = jgService.findByOrgCode(user.getJgdm());
//		RuntimeCheck.ifNull(org,"机构不存在");

		SysYh currentUser = getCurrentUser();
		user.setYhid(String.valueOf(idGenerator.nextId()));
		user.setMm(EncryptUtil.encryptUserPwd(user.getMm()));
		user.setCjr(currentUser.getYhid());
		user.setCjsj(new Date());
		user.setZt(Dict.UserStatus.VALID.getCode());
		if (StringUtils.isEmpty(user.getJgdm())){
			user.setJgdm(currentUser.getJgdm());
		}
		baseMapper.insertSelective(user);

		// 如果用户类型为管理员，则默认拥有系统管理权限
		if ("00".equals(user.getLx())){
			addAdminPermission(user);
		}
		return ApiResponse.success();
	}

	private void addAdminPermission(SysYh user){
		// 添加机构管理员角色
		Date now = new Date();
		SysJs adminRole = new SysJs();
		SysYh currentUser = getCurrentUser();
		SysJg jg = jgService.findByOrgCode(currentUser.getJgdm());
		adminRole.setCjr(currentUser.getZh());
		adminRole.setJgdm(user.getJgdm());
		adminRole.setCjsj(now);
		adminRole.setJsId(genId());
		adminRole.setJslx("00");
		adminRole.setJsmc(jg.getJgmc()+"-机构管理员");
		adminRole.setZt("00");
		jsService.saveEntity(adminRole);

		SysYhJs yhJs = new SysYhJs();
		yhJs.setCjr(currentUser.getZh());
		yhJs.setCjsj(now);
		yhJs.setJsId(adminRole.getJsId());
		yhJs.setYhId(user.getYhid());
		yhJs.setYhjsId(genId());
		yhJsMapper.insertSelective(yhJs);

		List<String> permissionList = Arrays.asList("system-user","system-role","system-framework");
		List<SysGn> functinos = gnService.findIn(SysGn.InnerColumn.gndm,permissionList);
		for (SysGn functino : functinos) {
			SysJsGn jsGn = new SysJsGn();
			jsGn.setCjr(currentUser.getZh());
			jsGn.setCjsj(now);
			jsGn.setFwdm("1");
			jsGn.setGndm(functino.getGndm());
			jsGn.setFgndm(functino.getFjd());
			jsGn.setJsdm(adminRole.getJsId());
			jsGn.setId(genId());
			jsGnMapper.insertSelective(jsGn);
		}
		gnService.cachePermission(Arrays.asList(adminRole.getJsId()));
	}

	@Override
	public ApiResponse<String> saveEntity(SysYh entity) {
		RuntimeCheck.ifBlank(entity.getZh(),"请先输入登陆名！");
		RuntimeCheck.ifBlank(entity.getMm(),"请先输入登陆密码！");
		RuntimeCheck.ifFalse(StringUtils.isAlphanumeric(entity.getZh()),"登陆名只能是数字和字母组成！");

		SimpleCondition condition = new SimpleCondition(SysYh.class);
		condition.eq(SysYh.InnerColumn.zh.name(), entity.getZh());
		Integer count = this.countByCondition(condition);
		RuntimeCheck.ifTrue(count > 0,"登陆名已存在，请更换别的登陆名！");

		entity.setYhid(genId());
		entity.setMm(EncryptUtil.encryptUserPwd(entity.getMm()));
		entity.setZt(Dict.UserStatus.VALID.getCode());
		entity.setSjh(entity.getSjh().trim());
		entity.setCjsj(new Date());
		if (StringUtils.isEmpty(entity.getJgdm())){
			SysYh user = getCurrentUser();
			entity.setJgdm(user.getJgdm());
		}
		this.save(entity);
		return ApiResponse.saveSuccess();
	}

	/**
	 * 修改登录密码
	 *
	 * @param userId 用户id
	 * @param oldPwd 旧密码
	 * @param newPwd 新密码
	 * @return 操作结果
	 */
	@Override
	public ApiResponse<String> mdfPwd(String userId, String oldPwd, String newPwd) {
		SysYh user = baseMapper.selectByPrimaryKey(userId);
		String newEncrypt;
		if (user == null) return ApiResponse.fail("用户不存在");
		try {
			String encrypt = Des.encrypt(oldPwd);
			if (!encrypt.equals(user.getMm())){
				return ApiResponse.fail("密码错误");
			}
			newEncrypt = Des.encrypt(newPwd);
		} catch (Exception e) {
			log.error("加密失败 oldPwd={},newPwd={}",oldPwd,newPwd);
			return ApiResponse.fail("加密失败");
		}
		user.setMm(newEncrypt);
		baseMapper.updateByPrimaryKeySelective(user);
		return ApiResponse.success();
	}



	@Override
	public ApiResponse<String> updateEntity(SysYh user) {
		RuntimeCheck.ifBlank(user.getZh(),"账号不能为空");
		RuntimeCheck.ifBlank(user.getXm(),"姓名不能为空");
		RuntimeCheck.ifBlank(user.getJgdm(),"请选择机构");
		RuntimeCheck.ifBlank(user.getSjh(),"手机号不能为空");
		RuntimeCheck.ifFalse(StringUtils.isAlphanumeric(user.getZh()),"登陆名只能是数字和字母组成！");
		SysJg org = jgService.findByOrgCode(user.getJgdm());
		RuntimeCheck.ifNull(org,"机构不存在");

		LimitedCondition condition= new LimitedCondition(SysYh.class);
		condition.eq(SysYh.InnerColumn.zh, user.getZh());
		List<SysYh> list=baseMapper.selectByExample(condition);

		if(list==null||list.size()==0){
			RuntimeCheck.ifTrue(true,"该账户不存在，或该账户不属于本机构，您无法修改！");
		}

		baseMapper.updateByPrimaryKeySelective(user);
		return updateSession(user);
	}

	@Override
	public ApiResponse<String> updateSession(SysYh user) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		request.setAttribute("userInfo",user);
		request.setAttribute("orgCode",user.getJgdm());
		return ApiResponse.success();
	}

	/**
	 * 重置密码
	 * @param userId
	 * @return
	 */
	@Override
	public ApiResponse<String> resetPassword(String userId){
		SysYh sysUser = getCurrentUser(true);
		RuntimeCheck.ifTrue(StringUtils.equals(sysUser.getYhid(),userId),"您不能为本人重置密码，请联系管理员进行重置密码。");
		SysYh user = baseMapper.selectByPrimaryKey(userId);
		if (user == null) return ApiResponse.fail("用户不存在");
		RuntimeCheck.ifFalse(user.getJgdm().indexOf(sysUser.getJgdm())==0,"您不能为非本机构的人员重置密码");
		String newEncrypt=null;
		try {
			newEncrypt = Des.encrypt(resePwd);
		} catch (Exception e) {
			log.error("加密失败 newEncrypt={}",resePwd);
			return ApiResponse.fail("加密失败");
		}

		user.setMm(newEncrypt);
		baseMapper.updateByPrimaryKeySelective(user);
		return ApiResponse.success();
	}
}
