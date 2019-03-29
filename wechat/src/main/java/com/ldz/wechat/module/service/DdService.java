package com.ldz.wechat.module.service;

import com.ldz.util.bean.ApiResponse;
import com.ldz.wechat.base.BaseService;
import com.ldz.wechat.module.model.ClDd;
import com.ldz.wechat.module.model.ClGpsLs;
import com.ldz.wechat.module.model.ClJsy;
import com.ldz.wechat.module.model.Clyy;

import java.util.List;
import java.util.Map;

public interface DdService extends BaseService<ClDd,String> {

    ApiResponse<String> saveEntity(ClDd entity, String userId);

    ApiResponse<String> updateOrder(ClDd entity,String userId);


//    ApiResponse<String> driverConfirm(String id,String userId);
    /**
     * 教职工 订单查询
     * @return
     */
    ApiResponse<List<ClDd>> getOrderWorkersList(String userId);

    ApiResponse<List<ClDd>> getOrderDriverList(String userId, String type);

    ApiResponse<String> evaluate(String orderId, String grade);

    ApiResponse<Map<String,Object>> getStartPointAndEndPoint(String orderId);

    ApiResponse<List<Clyy>> getOrderGpsList(String orderId);
}
