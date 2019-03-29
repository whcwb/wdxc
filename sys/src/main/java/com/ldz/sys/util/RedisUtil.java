package com.ldz.sys.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * auther chenwei
 * create at 2018/5/6
 */
@Slf4j
@Component
public class RedisUtil {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    public void deleteRedisKey(String url,String key){
        log.info("================准备发送 deleteRedisKey 请求==================");
        log.info("================请求 url:"+url);
        Map<String,Object> params = new HashMap<>();
        params.put("key",key);
        try{
            ResponseEntity<String> responseEntity = restTemplateBuilder.build().postForEntity(url,params,String.class);
            log.info("deleteRedisKey responst:",responseEntity.toString());
        }catch (Exception e){
            log.error("deleteRedisKey 请求异常",e);
        }
    }
}
