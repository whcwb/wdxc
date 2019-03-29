package com.ldz.api.module.app.controller;

import com.ldz.api.util.ContextUtil;
import com.ldz.dao.biz.model.ClYh;
import com.ldz.service.biz.interfaces.ClYhService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.util.redis.RedisTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("app/user")
public class UserCtrl {
    @Autowired
    private ClYhService service;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    /**
     * app端缓存用户数据
     * （app页面跳转后无法获取值，所以写了这两个接口）
     * @param key
     * @param val
     * @return
     */
    @PostMapping("putData")
    public ApiResponse<String> putData(String key,String val){
        RuntimeCheck.ifBlank(key,"请设置key");
        RuntimeCheck.ifBlank(val,"请设置val");
        String userId = (String) ContextUtil.getSessionAttribute("userId");
        key = "UserData_"+userId+"_"+key;
        try{
            redisTemplateUtil.boundValueOps(key).set(val,2, TimeUnit.HOURS);
        }catch (Exception e){
            return ApiResponse.fail(e.getMessage());
        }
        return ApiResponse.success();
    }

    /**
     * app端获取缓存
     * @param key
     * @return
     */
    @PostMapping("getData")
    public ApiResponse<String> getData(String key){
        RuntimeCheck.ifBlank(key,"请设置key");
        String userId = (String) ContextUtil.getSessionAttribute("userId");
        key = "UserData_"+userId+"_"+key;
        ApiResponse<String> res = new ApiResponse<>();
        if (!redisTemplateUtil.hasKey(key)){
            res.setCode(404);
            return res;
        }
        String s = (String) redisTemplateUtil.boundValueOps(key).get();
        res.setResult(s);
        return res;
    }


    /**
     * 注册
     * @return
     */
    @PostMapping("regist")
    public ApiResponse<String> regist(String userName, String password, String password1){
        return service.regist(userName,password,password1);
    }

    @PostMapping("login")
    public ApiResponse<Map<String, Object>> login(String userName, String password){
        return service.login(userName,password);
    }

    /**
     * 修改密码
     */
    @PostMapping("editPwd")
    public ApiResponse<String> editPwd(String userName, String oldPwd, String newPwd, String newPwd1){
        return service.editPwd(userName, oldPwd, newPwd, newPwd1);
    }

    /**
     * 获取用户信息
     */
    @PostMapping("/getUserInfo")
    public ApiResponse<Map<String,Object>> getUserInfo(){
        return service.getUserInfo();
    }

    /**
     * 修改用户信息
     */
    @PostMapping("/editUserInfo")
    public ApiResponse<Map<String,Object>> updateUserInfo(ClYh entity){
        return service.updateUserInfo(entity);
    }


}
