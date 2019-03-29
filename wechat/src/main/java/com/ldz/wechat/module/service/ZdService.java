package com.ldz.wechat.module.service;

import com.ldz.util.bean.ApiResponse;
import com.ldz.wechat.base.BaseService;
import com.ldz.wechat.module.bean.DdClModel;
import com.ldz.wechat.module.bean.NearbyStation;
import com.ldz.wechat.module.model.ClXl;
import com.ldz.wechat.module.model.ClZd;

import java.util.List;
import java.util.Map;

public interface ZdService extends BaseService<ClZd,String> {

    ApiResponse<Map<String,Object>> getStationInfo(String lng, String lat);
}
