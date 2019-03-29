package com.ldz.znzp.service.impl;

import com.ldz.util.bean.ApiResponse;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClZnzpMapper;
import com.ldz.znzp.model.ClZnzp;
import com.ldz.znzp.model.ClZpXl;
import com.ldz.znzp.model.SysHdyx;
import com.ldz.znzp.service.HdService;
import com.ldz.znzp.service.ZnzpService;
import com.ldz.znzp.service.ZpXlService;
import com.ldz.znzp.util.NettyUtil;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ZnzpServiceImpl extends BaseServiceImpl<ClZnzp,String> implements ZnzpService{
    @Autowired
    private ClZnzpMapper entityMapper;
    @Autowired
    private ZpXlService zpXlService;
    @Autowired
    private HdService hdService;

    @Autowired
    private NettyUtil nettyUtil;

    @Override
    protected Mapper<ClZnzp> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClZnzp.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClZnzp entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }

    @Override
    public ApiResponse<String> updateMedia(String jgdm) {
        // 获取所有站牌
        List<ClZnzp> znzpList = findAll();
        if (znzpList.size() == 0)return ApiResponse.fail("未找到智能站牌");
        List<String> deviceIds = znzpList.stream().map(ClZnzp::getZdbh).collect(Collectors.toList());
        Map<String,Object> tidChannelMap = nettyUtil.getChannelByTids(deviceIds);
        if (tidChannelMap == null){
//            return ApiResponse.fail("未找到channel");
        }
//        for (Map.Entry<String, Object> entry : tidChannelMap.entrySet()) {
//            hdService.sendActivityNews(entry.getValue(),entry.getKey());
//        }
        return ApiResponse.success();
    }

    @Override
    public List<ClZnzp> getByXlId(String xlId) {
        List<ClZpXl> zpXlList = zpXlService.findEq(ClZpXl.InnerColumn.xlId,xlId);
        List<String> zpIds = zpXlList.stream().map(ClZpXl::getZpId).collect(Collectors.toList());
        List<ClZnzp> znzps = findIn(ClZnzp.InnerColumn.zdbh,zpIds);
        return znzps;
    }
}
