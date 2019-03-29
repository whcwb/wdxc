package com.ldz.sys.util;

import com.ldz.util.exception.RuntimeCheck;
import com.ldz.sys.model.SysYh;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class ContextUtil {
    public static SysYh getCurrentUser(boolean required){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SysYh user =  (SysYh)request.getAttribute("userInfo");
        if (required){
            RuntimeCheck.ifNull(user,"未找到登录用户");
        }
        return user;
    }
    public static SysYh getCurrentUser(){
        return getCurrentUser(false);
    }
}
