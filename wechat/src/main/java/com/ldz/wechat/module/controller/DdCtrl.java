package com.ldz.wechat.module.controller;

import com.github.pagehelper.Page;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.wechat.base.BaseController;
import com.ldz.wechat.base.BaseService;
import com.ldz.wechat.module.model.*;
import com.ldz.wechat.module.service.DdService;
import com.ldz.wechat.module.service.SjDdService;
import com.ldz.wechat.module.service.ZdxmService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/12.
 */
@RestController
@RequestMapping("put/dd")
public class DdCtrl extends BaseController<ClDd,String> {
    @Autowired
    private DdService service;
    @Autowired
    private SjDdService sjService;
    @Autowired
    private ZdxmService zdxmService;

    public static String getUserType(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String type = (String) request.getAttribute("type");
        RuntimeCheck.ifBlank(type,"当前登录用户未空！");
        return type;
    }


    /**
     * 获取当前登录用户信息
     * @return
     */
    public static String getCurrentUser(boolean require) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String type = (String) request.getAttribute("type");
        String userInfo = (String) request.getAttribute("userInfo");
        RuntimeCheck.ifTrue((StringUtils.isEmpty(type) || StringUtils.isEmpty(userInfo)) && require,"当前登录用户未空！");
        if ("jzg".equals(type)){
            SysJzgxx jzg = JsonUtil.toBean(userInfo,SysJzgxx.class);
            RuntimeCheck.ifNull(jzg,"未找到教职工信息");
            return jzg.getId();
        }else if ("jsy".equals(type)){
            ClJsy jsy = JsonUtil.toBean(userInfo,ClJsy.class);
            RuntimeCheck.ifNull(jsy,"未找到驾驶员信息");
            return jsy.getSfzhm();
        }else{
            RuntimeCheck.ifTrue(true,"未知用户类型");
            return null;
        }
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        //true:允许输入空值，false:不能为空值
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping("evaluate")
    public ApiResponse<String> evaluate(String orderId,String grade){
        return service.evaluate(orderId,grade);
    }

    @RequestMapping(value="/getzdxm", method={RequestMethod.POST})
    public ApiResponse<List<SysZdxm>> save(String zdlmdm){
        if(StringUtils.isEmpty(zdlmdm)){
            zdlmdm="ZDCLK0041";
        }
        List<SysZdxm> list=zdxmService.findByTypeCode(zdlmdm);
        return ApiResponse.success(list);
    }
//
    /**
     * 订单新增页面
     * @param entity
     *
     * @return
     */
    @RequestMapping(value="/save", method={RequestMethod.POST})
    public ApiResponse<String> save(ClDd entity){
        String type=getUserType();//
        RuntimeCheck.ifFalse(StringUtils.equals(type,"jzg"),"请用教职工角色登录");
        String userId = getCurrentUser(true);
        return service.saveEntity(entity,userId);
    }

    @Override
    protected BaseService<ClDd, String> getBaseService() {
        return service;
    }

    /**
     * 订单确认-订单详情
     * 通过订单ID 加 来验证驾驶员ID该订单的有效性
     * @param pkid
     *
     * @return
     *  ClDd 对象。
     */
    @RequestMapping(value="/ddxq/{pkid}", method={RequestMethod.GET})
    public ApiResponse<ClDd> get(@PathVariable("pkid")String pkid){
        String type=getUserType();//
        RuntimeCheck.ifFalse(StringUtils.equals(type,"jsy"),"请用司机角色登录");
        String userId = getCurrentUser(true);
        ClDd whereClDd=new ClDd();
        whereClDd.setId(pkid);
        whereClDd.setSj(userId);
        List<ClDd> list =service.findByEntity(whereClDd);
        if(list==null|| list.size()==0){
            return ApiResponse.success(new ClDd());
        }
        return ApiResponse.success(list.get(0));
    }

    /**
     * 订单确认-订单编辑(司机的修改功能。)
     * 1、订单处于：司机确认(行程结束)
     * 2、只有该队队长才能有限制
     * @param entity
    id      //订单ID  必填
    glf      //过路费
    sy      //事由
    zj      //总价   必填
    sc      //时长
    dj      //单价
    lc      //里程
    scf      //时长费
    lcf      //里程费
     * @return
     */
    @RequestMapping(value="/update", method={RequestMethod.POST})
    public ApiResponse<String> update(ClDd entity){
        String type=getUserType();//
        RuntimeCheck.ifFalse(StringUtils.equals(type,"jsy"),"请用司机角色登录");
        String userId = getCurrentUser(true);
        return service.updateOrder(entity,userId);
    }
//
//    /**
//     * 司机确认
//     * @param id
//     * @return
//     */
//    @RequestMapping(value = "driverConfirm",method = {RequestMethod.POST})
//    public ApiResponse<String> driverConfirm(String id){
//        String userId = getCurrentUser(true);
//        return service.driverConfirm(id,userId);
//    }

    /**
     * 教职工 订单查询
     * @return
     */
    @RequestMapping(value = "/getorderworkerslis",method = {RequestMethod.POST})
    public ApiResponse<List<ClDd>> getOrderWorkersList(){
        String type=getUserType();//
        RuntimeCheck.ifFalse(StringUtils.equals(type,"jzg"),"请用教职工角色登录");

        String userId = getCurrentUser(true);
        return service.getOrderWorkersList(userId);
    }
//    /**
//     * 列驾驶员订单 表 订单查询
//     * @param type 2、待确认  3、历史单据
//     * @return
//     */
//    @RequestMapping(value = "/getsjlist",method = {RequestMethod.POST})
//    public ApiResponse<List<ClDd>> getOrderDriverList(String type){
//        String userType=getUserType();//
//        RuntimeCheck.ifFalse(StringUtils.equals(userType,"jsy"),"请用司机角色登录");
//        String userId = getCurrentUser(true);
//        return service.getOrderDriverList(userId,type);
//    }

    /**
     * 列驾驶员订单  列表 订单查询
     * @param entity
     * @param pager
     * @return
     */
    @RequestMapping(value="/getsjlist", method={RequestMethod.POST, RequestMethod.GET})
    public ApiResponse<List<ClDd>> sjPager(ClDd entity, Page<ClDd> pager){
        String userType=getUserType();//
        RuntimeCheck.ifFalse(StringUtils.equals(userType,"jsy"),"请用司机角色登录");
        return sjService.pager(pager);
    }


    @RequestMapping("getStartPointAndEndPoint")
    public ApiResponse<Map<String,Object>> getStartPointAndEndPoint(String orderId){
        return service.getStartPointAndEndPoint(orderId);
    }

    /**
     * 获取当前订单的订单  车辆GPS列表
     * @param orderId
     * @return
     */
    @RequestMapping(value="/getddgps", method={RequestMethod.POST, RequestMethod.GET})
    public ApiResponse<List<Clyy>> getOrderGpsList(String orderId){
        return service.getOrderGpsList(orderId);
    }
}
