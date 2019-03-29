package com.ldz.service.biz.impl;

import com.ldz.dao.biz.bean.ObdMessageBean;
import com.ldz.service.biz.interfaces.ObdService;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.GpsObdMessageBean;
import com.ldz.util.bean.ObdFaultCodeBean;
import com.ldz.util.redis.RedisTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ObdServiceImpl extends BaseServiceImpl<ObdMessageBean,String> implements ObdService {
    @Autowired
    private RedisTemplate redisDao;
    @Autowired
    private RedisTemplateUtil redis;


    @Value("${gpsObdMessage-key:gpsObdMessage_}")
    private String gpsObdMessage;

    @Value("${obdFaultCodeList-key:obdFaultCodeList_}")
    private String obdFaultCodeListKey;


    @Override
    protected Mapper<ObdMessageBean> getBaseMapper() {
        return null;
    }

    public ApiResponse<Object> getObdTimely(String obdId){
        GpsObdMessageBean obds=new GpsObdMessageBean();
        List<ObdFaultCodeBean> list=new ArrayList<ObdFaultCodeBean>();
        try {
           obds=  (GpsObdMessageBean)redisDao.opsForValue().get(gpsObdMessage+obdId);
        }catch (Exception e){}

       try {
            list=redisDao.opsForList().range(obdFaultCodeListKey+obdId,0,-1);
       }catch (Exception E){}

        Map ret= new HashMap();
        ret.put("gpsObdMessage",obds);
        ret.put("obdFaultCode",list);
        return ApiResponse.success(ret);
    }
}
