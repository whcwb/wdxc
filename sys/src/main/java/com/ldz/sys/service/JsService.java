package com.ldz.sys.service;

import com.ldz.sys.base.BaseService;
import com.ldz.sys.model.SysJs;
import com.ldz.util.bean.ApiResponse;

import java.util.List;

public interface JsService extends BaseService<SysJs, String> {
    ApiResponse<String> saveEntity(SysJs entity);
    ApiResponse<String> updateEntity(SysJs entity);
    /**
     * 获取用户拥有的角色id
     * @param userId 用户id
     * @return 角色id列表
     */
    List<String> getUserRoleIds(String userId);

    /**
     * 获取用户拥有的角色
     * @param userId 用户id
     * @return 角色列表
     */
    List<SysJs> getUserRoles(String userId);
    List<SysJs> getUserRolesWithChecked(String userId);
    List<SysJs> findByRoleIds(Iterable<String> roleIds);
    ApiResponse<String> modifyUserRoles(String userId, List<String> roleIds);
    ApiResponse<String> modifyRolePermission(String roleId, List<String> bizIds, List<String> resourceIds);
}
