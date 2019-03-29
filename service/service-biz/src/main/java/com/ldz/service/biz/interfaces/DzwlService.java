package com.ldz.service.biz.interfaces;

import com.ldz.util.bean.ApiResponse;
import com.ldz.sys.base.BaseService;
import com.ldz.dao.biz.model.ClDzwl;

import java.util.List;

public interface DzwlService extends BaseService<ClDzwl,String>{
    ApiResponse<String> saveEntity(ClDzwl entity);
    ApiResponse<String> updateEntity(ClDzwl entity);

    /**
     * 设置车辆电子围栏
     * @param clId
     * @param wlIds
     * @return
     */
    ApiResponse<String> setCarDzwl(String clId, List<String> wlIds);

    ApiResponse<String> setCarsDzwl(String carIds, String wlid);

    ApiResponse<ClDzwl> getByCarId(String clId);


    ApiResponse<String> saveAppEntity(ClDzwl entity,String clIds);

    List<ClDzwl> queryApp(ClDzwl entity);

    void removeEntity(String id);
}
