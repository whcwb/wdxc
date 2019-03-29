package com.ldz.service.biz.interfaces;

import com.ldz.dao.biz.model.ClYh;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

import java.util.Map;

public interface ClYhService extends BaseService<ClYh, String> {
    /**
     * 用户注册接口
     * @param userName  账号
     * @param password  密码
     * @param password1  确认密码
     * @return
     */
    ApiResponse<String> regist(String userName, String password, String password1);

    ApiResponse<Map<String, Object>> login(String userName, String password);

    ApiResponse<String> editPwd(String userName, String oldPwd, String newPwd, String newPwd1);

    ApiResponse<Map<String, Object>> getUserInfo();

    ApiResponse<Map<String, Object>> updateUserInfo(ClYh entity);
}