//package com.ldz.api.module.app.controller;
//
//import com.ldz.service.biz.interfaces.ClYhService;
//import com.ldz.util.bean.ApiResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController
//public class MainCtrl {
//
//
//    @Autowired
//    private ClYhService service;
//    /**
//     * 注册
//     * @return
//     */
//    @PostMapping("regist")
//    public ApiResponse<String> regist(String userName, String password, String password1){
//
//        return service.regist(userName,password,password1);
//    }
//
//    @PostMapping("login")
//    public ApiResponse<Map<String, Object>> login(String userName, String password){
//
//        return service.login(userName,password);
//    }
//    /**
//     * 修改密码
//     */
//    @PostMapping("editPwd")
//    public ApiResponse<String> editPwd(String userName, String oldPwd, String newPwd, String newPwd1){
//        return service.editPwd(userName, oldPwd, newPwd, newPwd1);
//    }
//
//    @RequestMapping(value = "/403",method = {RequestMethod.GET,RequestMethod.POST})
//    public ApiResponse<String> forbidden(){
//        return ApiResponse.forbidden();
//    }
//    @RequestMapping(value = "/authFiled",method = {RequestMethod.GET,RequestMethod.POST})
//    public ApiResponse<String> authFiled(){
//        return ApiResponse.authFailed();
//    }
//}
