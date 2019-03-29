package com.ldz.api.module.app.controller;


import com.github.pagehelper.Page;
import com.ldz.dao.biz.mapper.ClDzwlClMapper;
import com.ldz.dao.biz.model.ClDzwl;
import com.ldz.dao.biz.model.ClDzwlCl;
import com.ldz.service.biz.interfaces.DzwlService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/app/dzwl")
public class DzwlCtrl {
    @Autowired
    private ClDzwlClMapper dzwlClMapper;
    @Autowired
    private DzwlService service;

    /**
     * 保存电子围栏
     * @param entity  实体
     * @return  操作成功与否
     */
    @PostMapping("/save")
    public ApiResponse<String> save(ClDzwl entity , @RequestParam(required = false) String clIds){
        entity.setWlly("app");
        return service.saveAppEntity(entity,clIds);
    }

    /**
     * 获取某辆车的电子围栏
     * @param clId  车辆id
     * @return  车辆的电子围栏
     */
    @RequestMapping("getByCarId")
    public ApiResponse<ClDzwl> getByCarId(String clId){
        return service.getByCarId(clId);
    }

    /**
     * 更新某个电子围栏
     * @param entity 电子围栏实体
     * @return  操作成功
     */
    @RequestMapping(value="/update", method={RequestMethod.POST})
    public ApiResponse<String> update(ClDzwl entity){
        return service.updateEntity(entity);
    }

    /**
     * 电子围栏查询
     */
    @PostMapping("/query")
    public ApiResponse<List<ClDzwl>> query(ClDzwl entity){
        ApiResponse<List<ClDzwl>> response = new ApiResponse<>();
        List<ClDzwl> dzwls = service.queryApp(entity);
        response.setResult(dzwls);
        return response;
    }

    /**
     * 电子围栏删除
     */
    @PostMapping("/remove/{pkid}")
    public ApiResponse<String> remove(@PathVariable("pkid") String id){
        service.removeEntity(id);
        return ApiResponse.success();
    }

    /**
     * 电子围栏分页查询
     */
    @PostMapping("/pager")
    public ApiResponse<List<ClDzwl>> pager(Page<ClDzwl> page){
        return service.pager(page);
    }

    /**
     * 设置车辆电子围栏
     * @param clId  车辆id
     * @param wlIds  围栏ids
     * @return 操作成功
     */
    @RequestMapping("setCarDzwl")
    public ApiResponse<String> setCarDzwl(String clId,List<String> wlIds){
        return service.setCarDzwl(clId,wlIds);
    }

    /**
     * 为多个车辆设置同一个电子围栏
     * @param carIds  多个车辆id
     * @param wlid  围栏id
     * @return  操作成功
     */
    @RequestMapping("setCarsDzwl")
    public ApiResponse<String> setCarsDzwl(String carIds,String wlid){
        return service.setCarsDzwl(carIds,wlid);
    }


}
