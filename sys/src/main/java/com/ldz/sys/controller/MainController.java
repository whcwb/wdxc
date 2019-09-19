package com.ldz.sys.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.ldz.sys.bean.AccessToken;
import com.ldz.sys.bean.Menu;
import com.ldz.sys.bean.UserPassCredential;
import com.ldz.sys.bean.userInfoModel;
import com.ldz.sys.model.SysJg;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.model.SysZdlm;
import com.ldz.sys.model.SysZdxm;
import com.ldz.sys.service.*;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.commonUtil.Des;
import com.ldz.util.commonUtil.FileUtil;
import com.ldz.util.commonUtil.JwtUtil;
import com.ldz.util.exception.RuntimeCheck;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 处理用户登陆、登出、查询字典信息等相关访问接口
 * @author Lee
 * @date 2017年8月12日
 */
@RestController
@Slf4j
public class MainController {

	@Value("${staticPath:/}")
	private String staticPath;

	@Autowired
	private YhService userService;
	@Autowired
	private JgService jgService;
	@Autowired
	private GnService gnService;
	@Autowired
	private ZdlmService zdlmService;
	@Autowired
	private ZdxmService zdxmService;
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
	private StringRedisTemplate redisDao;
 // 忽略当接收json字符串中没有bean结构中的字段时抛出异常问题
 	private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	/**
	 * 用户登陆接口
	 * @param userCred
	 * @return
	 */
//	@RequestMapping(value="/login", method={RequestMethod.POST})
	@RequestMapping(value="/login")
	public ApiResponse<Map<String,Object>> login(UserPassCredential userCred, HttpServletRequest request){
//		RuntimeCheck.ifBlank(userCred.getCodeID(),"验证码不正确！");
		RuntimeCheck.ifTrue((
				StringUtils.isEmpty(userCred.getUsername()) ||
				StringUtils.isEmpty(userCred.getPassword())),
//				StringUtils.isEmpty(userCred.getCaptcha())),
				"请提交登陆用户信息！");
//		String code = (String)request.getSession().getAttribute(userCred.getCodeID());
//		RuntimeCheck.ifTrue(!userCred.getCaptcha().equals(code),"验证码不正确！");
		return commonLogin(userCred);
	}


	private ApiResponse<Map<String,Object>> commonLogin(UserPassCredential userCred){
		//加密密码
		try {
			userCred.setPassword(Des.encrypt(userCred.getPassword()));
		} catch (Exception e1) {
			throw new RuntimeException("密码加密异常",e1);
		}

		Example condition = new Example(SysYh.class);
		condition.and()
				.andEqualTo(SysYh.InnerColumn.zh.name(), userCred.getUsername())
				.andEqualTo(SysYh.InnerColumn.mm.name(), userCred.getPassword());
		log.info(" b findByCondition");
		List<SysYh> existUser = this.userService.findByCondition(condition);
		log.info(" a findByCondition");
		Map<String,Object> rMap = new HashMap<>(2);
		ApiResponse<Map<String,Object>> result = new ApiResponse<>();
		if (existUser != null && existUser.size() > 0){
			SysYh item = existUser.get(0);
			RuntimeCheck.ifTrue(!"01".equals(item.getZt()),"用户已禁用！");

			try {
				String token = JwtUtil.createToken(item.getYhid(),item.getZh());
				redisDao.boundValueOps(item.getYhid()).set(token, 1, TimeUnit.DAYS);
				redisDao.boundValueOps(item.getYhid()+"-userInfo").set(mapper.writeValueAsString(item), 1, TimeUnit.DAYS);
				AccessToken aToken = new AccessToken();
				aToken.setUserId(item.getYhid());
				aToken.setUsername(item.getZh());
				aToken.setToken(token);
				userInfoModel userInfomodel = new userInfoModel();
				userInfomodel.setXm(item.getXm());
				userInfomodel.setYhid(item.getYhid());
				userInfomodel.setJgdm(item.getJgdm());
				userInfomodel.setType(item.getLx());
				rMap.put("accessToken", aToken);
				rMap.put("userInfo", userInfomodel);
				SysJg org = jgService.findByOrgCode(item.getJgdm());
				if (org != null){
					rMap.put("jgmc", org.getJgmc());
				}

				// 获取用户菜单树
				List<Menu> menuTree = gnService.getMenuTree(item);
				if (menuTree.size() == 0){
					result.setCode(ApiResponse.FAILED);
					result.setMessage("您的账号暂未分配权限，请联系机构管理员");
					return result;
				}
				rMap.put("menuTree",menuTree);

				// 获取字典项
				List<SysZdlm> zdlmList = zdlmService.findAll();
				getZdxm(zdlmList);
				rMap.put("dictList",zdlmList);
				result.setResult(rMap);
			} catch (Exception e) {
				result.setCode(ApiResponse.FAILED);
				result.setMessage("用户登陆失败，请重试！");
				return result;
			}
		}else{
			result.setCode(ApiResponse.FAILED);
			result.setMessage("用户名或密码不正确！");
			return result;
		}
		return result;
	}


	private void getZdxm(List<SysZdlm> list){
		List<String> lmdms = list.stream().map(SysZdlm::getLmdm).collect(Collectors.toList());
		List<SysZdxm> zdxms = zdxmService.findByZdlms(lmdms);
		Map<String,SysZdlm> zdlmMap = list.stream().collect(Collectors.toMap(SysZdlm::getLmdm,p->p));
		for (SysZdxm zdxm : zdxms) {
			SysZdlm zdlm = zdlmMap.get(zdxm.getZdlmdm());
			if (zdlm == null)continue;
			if (zdlm.getZdxmList() == null){
				List<SysZdxm> zdxmList = new ArrayList<>();
				zdxmList.add(zdxm);
				zdlm.setZdxmList(zdxmList);
			}else{
				zdlm.getZdxmList().add(zdxm);
			}
		}
	}

	/**
	 * 用户退出接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/logout", method={RequestMethod.GET})
	public ApiResponse<AccessToken> logout(HttpServletRequest request){
		ApiResponse<AccessToken> result = new ApiResponse<>();
		String userId = request.getHeader("userid");
		redisDao.delete(userId);
		redisDao.delete(userId+"-userInfo");

		return result;
	}

	/**
	 * 登陆验证码图片生成
	 * @param id
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @throws Exception
	 */
    @RequestMapping(value="/pub/image/Kaptcha/{id}", method={RequestMethod.GET}, produces={MediaType.IMAGE_JPEG_VALUE})
    public void Kaptcha(@PathVariable("id")String id , HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
    	if (StringUtils.isEmpty(id)){
    		return;
    	}

        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        try {
        	//生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
			httpServletRequest.getSession().setAttribute(id, createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", httpServletResponse.getOutputStream());
        } catch (IllegalArgumentException e) {
        	httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
    }

	//处理文件上传
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse<String> uploadImg(@RequestParam("file") MultipartFile file, String targetPath) {
    	if (StringUtils.isEmpty(targetPath)) targetPath = "temp";
		targetPath = targetPath + "/";
		String fileName = file.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		UUID uuid = UUID.randomUUID();
		fileName = uuid.toString().replaceAll("-","") + suffix;
		String filePath = staticPath + targetPath;
		String path = targetPath + fileName;
		try {
			FileUtil.uploadFile(file.getBytes(), filePath, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApiResponse.success(path);
	}
	@RequestMapping(value = "/403",method = {RequestMethod.GET,RequestMethod.POST})
	public ApiResponse<String> forbidden(){
		return ApiResponse.forbidden();
	}
	@RequestMapping(value = "/authFiled",method = {RequestMethod.GET,RequestMethod.POST})
	public ApiResponse<String> authFiled(){
		return ApiResponse.authFailed();
	}
}
