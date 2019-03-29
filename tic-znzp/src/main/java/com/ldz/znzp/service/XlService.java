package com.ldz.znzp.service;

import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.base.BaseService;
import com.ldz.znzp.model.ClXl;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public interface XlService extends BaseService<ClXl,String>{
    ApiResponse<String> saveEntity(ClXl entity);

    List<ClXl> getByZdId(String zdId);
    /**
     * 根据智能站牌终端ID，查询这个站牌要显示哪些路线
     * @param tid
     * @return
     */
    public ApiResponse<String> getRouterInfo(ChannelHandlerContext ctx, String tid);

    ClXl getByCarId(String carId);
}
