
package com.ldz.sys.controller;

import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.sys.model.SysFw;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.service.YhService;
import com.ldz.util.bean.ApiResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/api/yh")
public class YhController extends BaseController<SysYh, String> {

	@Autowired
	private YhService userService;
	@Override
	protected BaseService<SysYh, String> getBaseService() {
		return userService;
	}

	@Override
	public ApiResponse<String> save(SysYh entity) {
//		entity.setLx(Dict.UserType.ADMIN.getCode());
		return this.userService.validAndSave(entity);
	}

	@Override
	public ApiResponse<String> update(SysYh entity) {

		return this.userService.updateEntity(entity);
	}


	@RequestMapping(value = "mdfPwd",method = RequestMethod.POST)
	public ApiResponse<String> mdfPwd(@RequestParam(name = "oldPwd")String oldPwd,
									  @RequestParam(name = "newPwd")String newPwd,
									  @RequestParam(name = "secPwd")String secPwd){
		RuntimeCheck.ifTrue((
				StringUtils.isEmpty(oldPwd) ||
				StringUtils.isEmpty(newPwd) ||
				StringUtils.isEmpty(secPwd)),"请输入密码");
		RuntimeCheck.ifTrue(!newPwd.equals(secPwd),"两次输入密码不一致");
		SysYh user = getCurrentUser();
		RuntimeCheck.ifTrue(user == null,"请重启登录！");
		return userService.mdfPwd(user.getYhid(),oldPwd,newPwd);
	}
	/**
	 * 重置密码
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "resetpwd",method = RequestMethod.POST)
	public ApiResponse<String> resetPassword (@RequestParam(name = "userId")String userId){
		userId=StringUtils.trim(userId);
		RuntimeCheck.ifTrue(StringUtils.isEmpty(userId) ,"请选择需要重置用户");
		return userService.resetPassword(userId);
	}
}
