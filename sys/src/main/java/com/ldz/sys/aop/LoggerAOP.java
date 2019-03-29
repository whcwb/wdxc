package com.ldz.sys.aop;

import com.ldz.sys.mapper.SysPtrzMapper;
import com.ldz.sys.model.SysRz;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.util.ContextUtil;
import com.ldz.util.commonUtil.JsonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Component
@Aspect
public class LoggerAOP {
    private static final Logger log = LoggerFactory.getLogger(LoggerAOP.class);

    @Autowired
    private SysPtrzMapper rzMapper;


    @Around("@annotation(com.ldz.sys.aop.Log))")
    public Object around(ProceedingJoinPoint point){
        log.info("AOP日志记录开始");
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable e) {
            log.error("方法执行异常",e);
        }finally {
            try{
                long endTime = System.currentTimeMillis();
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                MethodSignature methodSignature = (MethodSignature)point.getSignature();
                Method targetMethod = methodSignature.getMethod();
                Log log = targetMethod.getAnnotation(Log.class);
                SysYh user = ContextUtil.getCurrentUser();
                SysRz logger = new SysRz();
                String method = point.getSignature().getName();
                Result res = resultToString(result);
                String reqIP = request.getParameter("reqIP");
                String data = point.getTarget().getClass().getSimpleName()+"."+method+paramsToString(point.getArgs());
                if (data.length() > 4000){
                    data = data.substring(0,4000);
                }
                logger.setCs(data);
                logger.setZxsj((int) (endTime - startTime));
                logger.setSm(log.title());
                logger.setCzlx(log.type().getCode());
//                logger.setIp(reqIP);
                logger.setCzsj(new Date());
                if (LogType.LOGIN.getCode().equals(log.type().getCode())){
                    // 登录操作，由于返回值较长，result只记录username
                }else if (LogType.LOGOUT.getCode().equals(log.type().getCode())){
                    // 退出操作，不记录result
//                    logger.setStatusCode("200");
                }else{
//                    logger.setStatusCode(res.getStatusCode());
                    logger.setJg(res.getContent());
                }
                if (user != null){
                    logger.setCzr(user.getYhid());
                }
                rzMapper.insertSelective(logger);
            }catch (Exception e){
                log.error("AOP日志异常",e);
            }
        }
        return result;
    }

    private Result resultToString(Object result){
        Field[] fields = result.getClass().getDeclaredFields();
        List<Field> fieldList = Arrays.asList(fields);
        Map<String,Object> paramMap = new HashMap<>(fields.length);
        String statusCode = "-1";
        try {
            for (Field field : fieldList) {
                Class cls = field.getType();
                // 去掉不必要参数（HttpServletResponse等）
                if (cls.equals(HttpServletResponse.class))continue;
                field.setAccessible(true);
                Object val = field.get(result);
                paramMap.put(field.getName(),val);
                if ("statusCode".equals(field.getName())){
                    statusCode = (String)val;
                }
            }
        } catch (IllegalAccessException e) {
            log.error("反射获取结果失败",e);
            return new Result("反射获取结果失败");
        }
        String res = null;
        try {
            res = JsonUtil.toJson(paramMap);
        } catch (Exception e) {
            log.error("json转换异常",e);
            return new Result("json转换异常");
        }
        return new Result(statusCode,res);
    }

    private String paramsToString(Object[] p){
        List<Object> params = new ArrayList<>(Arrays.asList(p));
        // 去掉不必要参数（HttpServletRequest 等）
        params.removeIf(param -> param instanceof HttpServletRequest
                || param instanceof MultipartFile
                || param instanceof MultipartFile[]);
        String result = null;
        try {
            result = JsonUtil.toJson(params);
        } catch (Exception e) {
            log.error("json转换异常",e);
        }
        return result;

    }

    private class Result{
        String statusCode = "-1";
        String content;

        public Result(String content){
            this.content = content;
        }

        public Result(String statusCode,String content){
            this.statusCode = statusCode;
            this.content = content;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public String getContent() {
            return content;
        }
    }
}