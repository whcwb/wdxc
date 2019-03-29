package com.ldz.service.biz.interfaces;


import com.ldz.dao.biz.model.ClXc;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

import java.util.List;
import java.util.Map;

public interface XcService extends BaseService<ClXc,String> {


    ApiResponse<String> saveEntity(ClXc clXc);

    ApiResponse<List<Map<String, Object>>> history(String zdbh, String startTime, String endTime);

    /**
     * 批量解析行程开始地址和结束地址
     * @return
     */
    ApiResponse<String> batchParseAddress();

    String getAddress(String lat, String lng, String type, String area);
}
