package com.ldz.service.biz.interfaces;

import java.util.List;
import java.util.Map;

import com.ldz.dao.biz.bean.*;
import com.ldz.dao.biz.model.ClDd;
import com.ldz.dao.biz.model.ClJsy;
import com.ldz.sys.base.BaseService;
import com.ldz.sys.model.SysYh;
import com.ldz.util.bean.ApiResponse;

public interface DdService extends BaseService<ClDd,String>{
    ApiResponse<String> saveEntity(ClDd entity, SysYh userInfo);

    ApiResponse<String> updateOrderAuditing(ClDd entity);

    ApiResponse<Map<String,Object>> orderdDetails(ClDd entity);


    ApiResponse<List<ClDd>> assignedOrderd(ClDd entity);

    ApiResponse<List<ClJsyModel>> driverList(ClJsy entity);

    ApiResponse<List<ClDd>> affirmOrderList(ClDd entity);


    ApiResponse<String> updateAssignOrder(ClDd entity);

    ApiResponse<String> updateCancelAssignOrder(ClDd entity);

    ApiResponse<String> updateOrder(ClDd entity);

    ApiResponse<String> updateAffirmOracle(ClDd entity);

//    ApiResponse<List<Map<String,Object>>> proceedsDetail(ClDd entity);

//    ApiResponse<List<Map<String,Object>>> paymentDetail(ClDd entity);

    ApiResponse<String> updateFinanceOrder(ClDd entity);

    ApiResponse<String> updateFinanceConfirm(String[] ids);

    //订单统计 各种状态订单统计
    ApiResponse<List<Ddtongji>>  ddtongji(DdTongjiTJ dd);
    // 司机出车统计
    ApiResponse<List<Ddtongji>> chucheTj(DdTongjiTJ dd);

    ApiResponse<String> driverConfirm(String id);

    ApiResponse<Map<String,Object>> getVeryDayOrder(String cllx);
   //订单付款统计
	ApiResponse<List<Ddtongji>> FukuanTj(DdTongjiTJ dd);
   //机构收款统计
	ApiResponse<List<Ddtongji>> ShoukuanTj(DdTongjiTJ dd);

    ApiResponse<List<Map<String,Object>>> collectingList(ClDd entity);

    ApiResponse<List<Map<String,Object>>> paymentList(ClDd entity);

    ApiResponse<String> collectingConfirm(List<String> idList);

    ApiResponse<String> paymentConfirm(List<String> idList);
   // 统计一周订单 
    ApiResponse<DdTjTxReturn> ddzTj(DdTongjiTJ dd);
    //司机出车统计条形图
	ApiResponse<CcTjTx> chucheTTj(DdTongjiTJ dd);
}
