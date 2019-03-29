package com.ldz.biz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.dao.biz.model.ClDzwl;
import com.ldz.service.biz.interfaces.DzwlService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

/**
 * 电子围栏
 */
@RestController
@RequestMapping("api/dzwl")
public class DzwlCtrl extends BaseController<ClDzwl,String>{
    @Autowired
    private DzwlService service;

    @Override
    protected BaseService<ClDzwl, String> getBaseService() {
        return service;
    }
    @Override
    @RequestMapping(value="/save", method={RequestMethod.POST})
    public ApiResponse<String> save(ClDzwl entity){
        entity.setWlly("pc");
        return service.saveEntity(entity);
    }
    @Override
    @RequestMapping(value="/update", method={RequestMethod.POST})
    public ApiResponse<String> update(ClDzwl entity){
        return service.updateEntity(entity);
    }

    @RequestMapping("getByCarId")
    public ApiResponse<ClDzwl> getByCarId(String clId){
        return service.getByCarId(clId);
    }

    /**
     * 设置车辆电子围栏
     * @param clId
     * @param wlIds
     * @return
     */
    @RequestMapping("setCarDzwl")
    public ApiResponse<String> setCarDzwl(String clId,List<String> wlIds){
        return service.setCarDzwl(clId,wlIds);
    }

    /**
     * 为多个车辆设置同一个电子围栏
     * @param carIds
     * @param wlid
     * @return
     */
    @RequestMapping("setCarsDzwl")
    public ApiResponse<String> setCarsDzwl(String carIds,String wlid){
        return service.setCarsDzwl(carIds,wlid);
    }
}
