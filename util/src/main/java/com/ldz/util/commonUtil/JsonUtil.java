package com.ldz.util.commonUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by chenwei on 2017/9/6
 */
public class JsonUtil {
    private static Logger logger = LogManager.getLogger(JsonUtil.class);
    private static ObjectMapper mapper = new ObjectMapper();
    static {
        //字段为NULL的时候不会列入
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //由于vo中缺少json的某个字段属性引起
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//允许出现特殊字符和转义符
//        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;

    }

    public static String toJson(Object o){
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            logger.error("json转换异常",e);
        }
        return "";
    }

    public static <T> T toBean(String s,Class<T> cls){
        try {
            return mapper.readValue(s,cls);
        } catch (IOException e) {
            logger.error("json转换异常",e);
        }
        return null;
    }

    public static <T> List<T> toList(String s,Class<?> cls){
        try {
            return mapper.readValue(s,mapper.getTypeFactory().constructParametricType(List.class,cls));
        } catch (IOException e) {
            logger.error("json转换异常",e);
        }
        return null;
    }
    public static Map toMap(String s){
        Map map = null;
        try {
            map = mapper.readValue(s, Map.class);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
