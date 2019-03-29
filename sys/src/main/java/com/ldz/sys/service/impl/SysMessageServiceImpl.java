package com.ldz.sys.service.impl;

import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.mapper.SysMessageMapper;
import com.ldz.sys.model.SysMessage;
import com.ldz.sys.service.SysMessageService;
import com.ldz.sys.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.Set;

/**
 * Created by Administrator on 2018/5/10.
 */
@Service
public class SysMessageServiceImpl extends BaseServiceImpl<SysMessage,String> implements SysMessageService{
    @Autowired
    private SysMessageMapper entityMapper;

    @Override
    protected Mapper<SysMessage> getBaseMapper() {
        return entityMapper;
    }

    public void add(SysMessage entity) {
        try {
            entity.setId(genId());//生成的ID
            entity.setCreationTime(new Date());//创建时间
            entity.setStatus(0+"");
            entity.setSendCount(0);
            if(entity.getSendTime()==null){
                entity.setSendTime(new Date());//消息发送时间
            }
            int i=entityMapper.insert(entity);
            if(i>0){//向redis写入标记
                //下发消息
//                redisDao.
//                Set<String> keys =  redisDao.keys("*-");
//                redisDao
            }
        }catch (Exception e){}
    }


}
