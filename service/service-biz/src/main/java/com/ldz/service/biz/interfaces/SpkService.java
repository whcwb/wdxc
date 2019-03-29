package com.ldz.service.biz.interfaces;

import com.github.pagehelper.Page;
import com.ldz.dao.biz.bean.GpsInfo;
import com.ldz.dao.biz.model.ClSpk;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

import java.util.List;
import java.util.Map;

public interface SpkService extends BaseService<ClSpk,String>{

    ApiResponse<String> saveEntity(ClSpk entity);

    ApiResponse<String> updateEntity(ClSpk entity);

    /*
     * 对tic-server提供上传云视屏库接口
     *
     *
     */
    ApiResponse<String> saveSpk(GpsInfo entity);

    ApiResponse<List<Map<String,Object>>> groupByDay(Page<ClSpk> page);

}
