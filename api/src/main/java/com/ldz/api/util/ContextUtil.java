package com.ldz.api.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class ContextUtil {

    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static String getParameter(String k){
        HttpServletRequest request = getRequest();
        return request.getParameter(k);
    }

    public static Object getSessionAttribute(String k){
        HttpServletRequest request = getRequest();
        return request.getSession().getAttribute(k);
    }
}
