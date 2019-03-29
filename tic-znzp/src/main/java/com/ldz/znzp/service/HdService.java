package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.znzp.model.SysHdyx;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Map;

/**
 * @author chenwei
 * @since 2018/2/26
 */
public interface HdService extends BaseService<SysHdyx,String> {

    List<SysHdyx> getHd(String jgdm);

    List<Map<String,String>> convert(List<SysHdyx> list);
    void sendActivityNews(ChannelHandlerContext ctx, String tid);

    void sendActivitys();
}
