package com.ldz.api.module.app.controller;

import com.ldz.api.module.app.service.AppClService;
import com.ldz.api.util.ContextUtil;
import com.ldz.dao.biz.bean.WebsocketInfo;
import com.ldz.dao.biz.mapper.ClClMapper;
import com.ldz.dao.biz.mapper.ClGpsMapper;
import com.ldz.dao.biz.mapper.ClZdglMapper;
import com.ldz.dao.biz.mapper.ZdYhMapper;
import com.ldz.dao.biz.model.*;
import com.ldz.service.biz.interfaces.ClService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.SnowflakeIdWorker;
import com.ldz.util.exception.RuntimeCheck;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.util.resources.cldr.ar.CalendarData_ar_LY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/car")
public class CarCtrl extends BaseController<ClCl,String> {
    @Autowired
    private ClZdglMapper zdglMapper;
    @Autowired
    private ZdYhMapper zdYhMapper;
    @Autowired
    private ClClMapper clclmapper;
    @Autowired
    private AppClService appClService;

    @Autowired
    public SnowflakeIdWorker idGenerator;


    @Override
    protected BaseService<ClCl, String> getBaseService() {
        return appClService;
    }

    @RequestMapping("findByDeviceId")
    public ApiResponse<ClCl> findByDeviceId(String deviceId){
        ApiResponse<ClCl> res = new ApiResponse<>();
        RuntimeCheck.ifBlank(deviceId,"请输入终端编号");
        ClZdgl device = zdglMapper.selectByPrimaryKey(deviceId);
        if (device == null){
            res.setCode(4041);
            return res;
        }
        List<ClCl> carList = appClService.findEq(ClCl.InnerColumn.zdbh,deviceId);
        if (carList.size() == 0){
            res.setCode(4042);
            return res;
        }
        res.setResult(carList.get(0));
        return res;
    }
    @PostMapping("addCar")
    public ApiResponse<String> addCar(ClCl entity){
        String userId = (String) ContextUtil.getSessionAttribute("userId");
        RuntimeCheck.ifBlank(userId,"未找到登录用户");
        if (StringUtils.isEmpty(entity.getClId())){
            RuntimeCheck.ifBlank(entity.getCph(),"请输入车牌号");
            entity.setClId(String.valueOf(idGenerator.nextId()));

            ClZdgl device = zdglMapper.selectByPrimaryKey(entity.getZdbh());
            RuntimeCheck.ifNull(device,"终端不存在");

            SimpleCondition condition = new SimpleCondition(ClCl.class);
            condition.eq(ClCl.InnerColumn.zdbh, entity.getZdbh());
            List<ClCl> clZdgls = clclmapper.selectByExample(condition);
            RuntimeCheck.ifTrue(CollectionUtils.isNotEmpty(clZdgls), "此终端已经绑定其他车辆, 请勿重复绑定");

            entity.setJgdm(device.getJgdm());
            clclmapper.insertSelective(entity);
        }else{
            clclmapper.updateByPrimaryKeySelective(entity);
        }


        SimpleCondition condition = new SimpleCondition(ClZdYh.class);
        condition.eq(ClZdYh.InnerColumn.deviceId,entity.getZdbh());
        condition.eq(ClZdYh.InnerColumn.userId,userId);
        List<ClZdYh> zdYhList = zdYhMapper.selectByExample(condition);
        if (zdYhList.size() == 0){
            ClZdYh zdYh = new ClZdYh();
            zdYh.setId(""+idGenerator.nextId());
            zdYh.setDeviceId(entity.getZdbh());
            zdYh.setUserId(userId);
            zdYhMapper.insert(zdYh);
        }
        return ApiResponse.success();
    }

    @RequestMapping("unbind/{zdbh}")
    public ApiResponse<String> unbind(@PathVariable("zdbh")String zdbh){
        String userId = (String) ContextUtil.getSessionAttribute("userId");
        RuntimeCheck.ifBlank(userId,"未找到登录用户");
        RuntimeCheck.ifBlank(zdbh,"请选择车辆");
        ClZdgl device = zdglMapper.selectByPrimaryKey(zdbh);
        RuntimeCheck.ifNull(device,"车辆不存在");
        SimpleCondition clZdCond = new SimpleCondition(ClZdYh.class);
        clZdCond.eq(ClZdYh.InnerColumn.userId,userId);
        clZdCond.eq(ClZdYh.InnerColumn.deviceId,zdbh);
        zdYhMapper.deleteByExample(clZdCond);
        // zdglMapper.deleteByPrimaryKey(zdbh);

        SimpleCondition carCond = new SimpleCondition(ClCl.class);
        carCond.eq(ClCl.InnerColumn.zdbh,zdbh);
        clclmapper.deleteByExample(carCond);
        return ApiResponse.success();
    }


    @RequestMapping("getBindCars")
    public ApiResponse<List<ClCl>> getBindCars(){
        String userId = (String) ContextUtil.getSessionAttribute("userId");
        RuntimeCheck.ifBlank(userId,"未找到登录用户");
        SimpleCondition condition = new SimpleCondition(ClZdYh.class);
        condition.eq(ClZdYh.InnerColumn.userId,userId);
        List<ClZdYh> zdYhs = zdYhMapper.selectByExample(condition);
        if (zdYhs.size() == 0) return ApiResponse.success(new ArrayList<>());
        List<String> deviceIds = zdYhs.stream().map(ClZdYh::getDeviceId).collect(Collectors.toList());

        condition = new SimpleCondition(ClCl.class);
        condition.in(ClCl.InnerColumn.zdbh,deviceIds);
        List<ClCl> cars = clclmapper.selectByExample(condition);
        return ApiResponse.success(cars);
    }

}
