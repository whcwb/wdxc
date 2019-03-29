package com.ldz.job.service.impl;

import com.ldz.dao.biz.mapper.ClSbyxsjjlMapper;
import com.ldz.dao.biz.model.ClSbyxsjjl;
import com.ldz.job.service.ClSbyxsjjlService;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.redis.RedisTemplateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.ArrayList;
import java.util.List;
@Service
public class ClSbyxsjjlServiceImpl extends BaseServiceImpl<ClSbyxsjjl, String> implements ClSbyxsjjlService {
    @Autowired
    private ClSbyxsjjlMapper entityMapper;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;


    Logger access_info = LoggerFactory.getLogger("access_info");

    @Override
    protected Mapper<ClSbyxsjjl> getBaseMapper() {
        return entityMapper;
    }


    @Override
    public void saveBatch() {

        // 获取所有的事件记录
        BoundListOperations<Object, Object> boundListOps = redisTemplateUtil.boundListOps(ClSbyxsjjl.class.getSimpleName());
        if(boundListOps == null || boundListOps.size() == 0 ){
            access_info.info("没有事件");
            return;
        }
        List<ClSbyxsjjl> list = new ArrayList<>();
        String index = (String) boundListOps.index(0);
        if (StringUtils.isNotEmpty(index)) {
            Long length = boundListOps.size();

            for(int i = 0 ; i < length ;i++ ){
                String clSbyxsjjl =  (String) boundListOps.rightPop();
                ClSbyxsjjl clSbyxsjjl1 = JsonUtil.toBean(clSbyxsjjl, ClSbyxsjjl.class);
                list.add(clSbyxsjjl1);
            }

        }



        entityMapper.insertList(list);


    }
}
