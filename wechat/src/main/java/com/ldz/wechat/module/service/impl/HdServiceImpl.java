package com.ldz.wechat.module.service.impl;

import com.ldz.util.bean.SimpleCondition;
import com.ldz.wechat.base.BaseServiceImpl;
import com.ldz.wechat.module.mapper.SysHdyxMapper;
import com.ldz.wechat.module.mapper.SysYxhdwjMapper;
import com.ldz.wechat.module.model.SysHdyx;
import com.ldz.wechat.module.service.HdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chenwei
 * @copyright
 * @category
 * @since 2018/2/26
 */
@Service
public class HdServiceImpl extends BaseServiceImpl<SysHdyx,String> implements HdService {
    @Autowired
    private SysHdyxMapper hdyxMapper;
    @Autowired
    private SysYxhdwjMapper yxhdwjMapper;
    @Value("${staticUrl:/}")
    private String staticUrl;

    @Override
    protected Mapper<SysHdyx> getBaseMapper() {
        return hdyxMapper;
    }


    public List<SysHdyx> getAdsenseList(){
        SimpleCondition condition = new SimpleCondition(SysHdyx.class);
        condition.lte(SysHdyx.InnerColumn.kssj, new Date());//开始时间
        condition.gte(SysHdyx.InnerColumn.jssj, new Date());//结束时间
        condition.eq(SysHdyx.InnerColumn.hdlx, "00");//活动类型 00微信、10智能站牌
        condition.eq(SysHdyx.InnerColumn.zt, "10");//状态(00未开始 10 已开始  20 已结束)
        List<SysHdyx> list=hdyxMapper.selectByExample(condition);
        if(list==null){
            list=new ArrayList<SysHdyx>();
        }
        return setFiles(list);
    }

    private List<SysHdyx> setFiles(List<SysHdyx> list){
        if(list!=null&&list.size()>0){
            for(SysHdyx l:list){
                String tableUrl=l.getUrl();
                boolean b=(tableUrl.toLowerCase()).startsWith("http");//判断字符串是否已百度二字开头
                if(!b){
                    l.setUrl(staticUrl+l.getUrl());
                }
                l.setImg(l.getUrl());
            }
        }
        return list;
    }
}
