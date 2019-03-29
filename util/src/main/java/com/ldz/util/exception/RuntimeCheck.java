package com.ldz.util.exception;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;


public class RuntimeCheck {

    public static void ifNull(Object o, String msg){
        if (o == null){
            throw new RuntimeCheckException(msg);
        }
    }
    public static void ifBlank(String o, String msg){
        if (StringUtils.isBlank(o)){
            throw new RuntimeCheckException(msg);
        }
    }
    public static void ifEmpty(Collection collection, String msg){
        if (collection.isEmpty()){
            throw new RuntimeCheckException(msg);
        }
    }
    public static void ifEmpty(Object[] array, String msg){
        if (array == null || array.length == 0){
            throw new RuntimeCheckException(msg);
        }
    }
    public static void ifTrue(boolean express, String msg){
        if (express){
            throw new RuntimeCheckException(msg);
        }
    }
    public static void ifFalse(boolean express, String msg){
        if (!express){
            throw new RuntimeCheckException(msg);
        }
    }
    public static void authCheck(boolean express){
        if (express){
            throw new AuthFailedException("授权认证失败");
        }
    }
}
