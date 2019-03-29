package com.ldz.sys.service.impl;

import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.mapper.SysClkPtjsMapper;
import com.ldz.sys.mapper.SysClkPtyhMapper;
import com.ldz.sys.mapper.SysJsGnMapper;
import com.ldz.sys.mapper.SysYhJsMapper;
import com.ldz.sys.model.SysJs;
import com.ldz.sys.model.SysJsGn;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.model.SysYhJs;
import com.ldz.sys.service.JsService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.exception.RuntimeCheck;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JsServiceImpl extends BaseServiceImpl<SysJs, String> implements JsService {

	@Autowired
	private SysClkPtjsMapper roleMapper;
	@Autowired
	private SysClkPtyhMapper userMapper;
	@Autowired
	private SysYhJsMapper userRoleMapper;
	@Autowired
	private SysJsGnMapper roleResourceMapper;

	@Override
	protected Mapper<SysJs> getBaseMapper() {
		return roleMapper;
	}

	@Override
	public ApiResponse<String> saveEntity(SysJs entity) {
		RuntimeCheck.ifBlank(entity.getJsmc(), "请输入角色名称");
		RuntimeCheck.ifBlank(entity.getJsId(), "请输入角色代码");
		SysJs selectByPrimaryKey = roleMapper.selectByPrimaryKey(entity.getJsId());
		if (selectByPrimaryKey != null) {
			return ApiResponse.fail("角色代码已存在");
		}
		SysYh user = getCurrentUser();
		entity.setJgdm(user.getJgdm());
		entity.setCjsj(new Date());
		entity.setCjr(getOperateUser());
		roleMapper.insertSelective(entity);
		return ApiResponse.success();
	}

	@Override
	public List<String> getUserRoleIds(String yhid) {
		Example userRoleExample = new Example(SysYhJs.class);
		userRoleExample.and().andEqualTo(SysYhJs.InnerColumn.yhId.name(), yhid);
		List<SysYhJs> userRoles = userRoleMapper.selectByExample(userRoleExample);
		if (userRoles.size() == 0)
			return new ArrayList<>();
		return userRoles.stream().map(SysYhJs::getJsId).collect(Collectors.toList());
	}

	/**
	 * 由于iview模板功能限制 返回系统所有角色，并把用户拥有的角色 _checked属性标记为true，以便前台修改用户角色
	 *
	 * @param yhid
	 * @return
	 */
	@Override
	public List<SysJs> getUserRolesWithChecked(String yhid) {
		List<SysJs> allRoles = roleMapper.selectAll();
		List<String> jsIds = getUserRoleIds(yhid);
		if (jsIds.size() == 0)
			return allRoles;
		for (SysJs role : allRoles) {
			if (jsIds.contains(role.getJsId())) {
				role.set_checked(true);
			}
		}
		return allRoles;
	}

	@Override
	public List<SysJs> getUserRoles(String yhid) {
		List<String> jsIds = getUserRoleIds(yhid);
		if (jsIds.size() == 0)
			return new ArrayList<>();
		Example roleExample = new Example(SysJs.class);
		roleExample.and().andIn(SysJs.InnerColumn.jsId.name(), jsIds);
		return roleMapper.selectByExample(roleExample);
	}

	@Override
	public List<SysJs> findByRoleIds(Iterable<String> jsIds) {
		Example roleExample = new Example(SysJs.class);
		roleExample.and().andIn(SysJs.InnerColumn.jsId.name(), jsIds);
		return roleMapper.selectByExample(roleExample);
	}

	@Override
	public ApiResponse<String> modifyUserRoles(String yhid, List<String> jsIds) {
		// 检查用户是否存在
		SysYh user = userMapper.selectByPrimaryKey(yhid);

		if (user == null)
			return ApiResponse.fail("用户不存在");
		RuntimeCheck.ifTrue(user.getLx().equals("00"), "用户为管理员，不宜分配其他角色");
		// 删除就数据
		Example userRoleExample = new Example(SysYhJs.class);
		userRoleExample.and().andEqualTo(SysYhJs.InnerColumn.yhId.name(), yhid);
		userRoleMapper.deleteByExample(userRoleExample);
		String createUser = getOperateUser();
		Date now = new Date();

		if(CollectionUtils.isNotEmpty(jsIds)) {
			// 插入新数据
			jsIds.forEach(jsId -> {
				SysYhJs userRole = new SysYhJs();
				userRole.setYhjsId(genId());
				userRole.setJsId(jsId);
				userRole.setYhId(yhid);
				userRole.setCjr(createUser);
				userRole.setCjsj(now);
				userRoleMapper.insert(userRole);

			});
		}
		/*for (String jsId : jsIds) {
			SysYhJs userRole = new SysYhJs();
			userRole.setYhjsId(genId());
			userRole.setJsId(jsId);
			userRole.setYhId(yhid);
			userRole.setCjr(createUser);
			userRole.setCjsj(now);
			userRoleMapper.insert(userRole);
		}*/
		return ApiResponse.success();
	}

	@Override
	public ApiResponse<String> modifyRolePermission(String jsId, List<String> bizIds, List<String> gndms) {
		// 检查角色是否存在
		SysJs role = roleMapper.selectByPrimaryKey(jsId);
		if (role == null)
			return ApiResponse.fail("角色不存在");

		// 删除旧数据

		SysJsGn roleResourceExample = new SysJsGn();
		roleResourceExample.setJsdm(jsId);
		roleResourceMapper.deleteByExample(roleResourceExample);

		// 插入新数据

		for (String gndm : gndms) {
			SysJsGn roleResource = new SysJsGn();
			roleResource.setJsdm(jsId);
			roleResource.setGndm(gndm);
			roleResourceMapper.insert(roleResource);
		}
		return ApiResponse.success();
	}

	@Override
	public ApiResponse<String> updateEntity(SysJs entity) {
		RuntimeCheck.ifBlank(entity.getJsmc(), "请输入角色名称");
		RuntimeCheck.ifBlank(entity.getJsId(), "请输入角色代码");
		entity.setXgr(getOperateUser());
		entity.setXgsj(new Date());
		update(entity);
		return  ApiResponse.updateSuccess()  ;
	}
}
