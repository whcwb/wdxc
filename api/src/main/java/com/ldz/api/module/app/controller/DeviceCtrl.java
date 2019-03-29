package com.ldz.api.module.app.controller;

import com.github.pagehelper.Page;
import com.ldz.api.util.ContextUtil;
import com.ldz.dao.biz.bean.GpsInfo;
import com.ldz.dao.biz.bean.WebsocketInfo;
import com.ldz.dao.biz.bean.gpsSJInfo;
import com.ldz.dao.biz.model.ClSpk;
import com.ldz.dao.biz.model.ClZdgl;
import com.ldz.service.biz.interfaces.*;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.commonUtil.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/device")
public class DeviceCtrl {
    @Autowired
    private ZdglService zdglService;
    @Autowired
    private XcService xcService;
    @Autowired
    private InstructionService intstruction;
    @Autowired
    private GpsService gpsservice;
    @Autowired
    private SbyxsjjlService service;
    @Autowired
    private SpkService spkService;

    /**
     * 批量解析行程开始地址和结束地址
     * @return
     */
    @PostMapping("batchParseAddress")
    public ApiResponse<String> batchParseAddress(){
        return xcService.batchParseAddress();
    }
    /**
     * 绑定终端
     * @return
     */
    @PostMapping("bind")
    public ApiResponse<String> bind(String userId,String deviceId){
        if (StringUtils.isEmpty(userId)){
            userId = (String) ContextUtil.getSessionAttribute("userId");
        }
        return zdglService.bindAppUser(userId,deviceId);
    }

    /**
     * 解绑app用户
     * @param deviceId
     * @return
     */
    @PostMapping("unbind")
    public ApiResponse<String> unbind(String userId,String deviceId){
        if (StringUtils.isEmpty(userId)){
            userId = (String) ContextUtil.getSessionAttribute("userId");
        }
        return zdglService.unbindAppUser(userId,deviceId);
    }

    /**
     * 获取用户绑定的终端列表
     * @return
     */
    @GetMapping("list")
    public ApiResponse<List<ClZdgl>> pager(Page<ClZdgl> pager){
        String userId = (String) ContextUtil.getSessionAttribute("userId");
        HttpServletRequest request = ContextUtil.getRequest();
        request.setAttribute("appUserId",userId);
        return zdglService.pager(pager);
    }

    @GetMapping("getDeviceInfo")
    public ApiResponse<Object> getDeviceInfo(String deviceId){
        return null;
    }


    @GetMapping("getGpsInfo")
    public ApiResponse<Object> getGpsInfo(String deviceId){
        return null;
    }

    /**
     * 轨迹列表
     * @param deviceId
     * @return
     */
    @GetMapping("getTrackList")
    public ApiResponse<List<Map<String, Object>>> getTrackList(String deviceId, String startTime, String endTime){
        return xcService.history(deviceId,startTime,endTime);
    }


    /**deviceId:设备id  cmdType:11拍视频  12拍照片 13合并视屏 (三选一)
     *
     * 	 * cmdType 为11和12的时候使用
     * 参数格式为分隔式字符串  如:0-10 前一个0 标识要抓拍的摄像头  后一个10标识当前时间点前后十秒
     * 摄像头参数如下:0,前后都抓拍, 1表示仅前摄像头, 2表示仅仅后摄像头。当cmdType为12的时候，此参数也是一样，只是抓拍前后多少秒参数无效【客户端自动判断，后台传递参数即可】
     * cmdType 为13的时候参数是0-0 或者1-0  ，特别注意，为13的时候，startTime和endTime必须有值
     * 摄像头参数如下:0 合并前摄像头  1 合并后摄像头  2 合并内置摄像头【内置摄像头这个暂时无法使用】
     */
    @PostMapping("takePhoto")
    public ApiResponse<String> takePhoto(String deviceId,String cmd){
        GpsInfo gpsInfo = new GpsInfo();
        gpsInfo.setDeviceId(deviceId);
        gpsInfo.setCmd(cmd);
        gpsInfo.setCmdType("12");
        return  intstruction.sendinstruction(gpsInfo);
    }


    /**deviceId:设备id  cmdType:11拍视频  12拍照片 13合并视屏 (三选一)
     *
     * 	 * cmdType 为11和12的时候使用
     * 参数格式为分隔式字符串  如:0-10 前一个0 标识要抓拍的摄像头  后一个10标识当前时间点前后十秒
     * 摄像头参数如下:0,前后都抓拍, 1表示仅前摄像头, 2表示仅仅后摄像头。当cmdType为12的时候，此参数也是一样，只是抓拍前后多少秒参数无效【客户端自动判断，后台传递参数即可】
     * cmdType 为13的时候参数是0-0 或者1-0  ，特别注意，为13的时候，startTime和endTime必须有值
     * 摄像头参数如下:0 合并前摄像头  1 合并后摄像头  2 合并内置摄像头【内置摄像头这个暂时无法使用】
     */
    @PostMapping("takeVideo")
    public ApiResponse<String> takeVideo(String deviceId,String cmd){
        GpsInfo gpsInfo = new GpsInfo();
        gpsInfo.setDeviceId(deviceId);
        gpsInfo.setCmd(cmd);
        gpsInfo.setCmdType("11");
        return  intstruction.sendinstruction(gpsInfo);
    }


    @PostMapping("sendCommand")
    public ApiResponse<String> sendCommand(GpsInfo gpsInfo){
        return  intstruction.sendinstruction(gpsInfo);
    }

    @PostMapping("/InitClGps")
    public ApiResponse<List<WebsocketInfo>> inintGps() {
        String userId = (String) ContextUtil.getSessionAttribute("userId");
        HttpServletRequest request = ContextUtil.getRequest();
        request.setAttribute("appUserId",userId);
        return gpsservice.inintGps();
    }
    
    @RequestMapping(method={RequestMethod.GET, RequestMethod.POST}, value="/getWebsocketInfo")
    public ApiResponse<List<WebsocketInfo>> getWebsocketInfo(String deviceIds){
        return gpsservice.getWebsocketInfoToApp(deviceIds);
    }


    /*
     *
     * 获取该终端所有历史轨迹数据
     *  入参每个必填 点火熄火固定值
     */
    @RequestMapping("/history")
    public ApiResponse<List<Map<String, Object>>> historyTrajectory(gpsSJInfo gpssjinfo) {

        String minTime = gpssjinfo.getStartTime();
        String maxTime = gpssjinfo.getEndTime();

        if (StringUtils.isBlank(minTime)) {
            minTime = DateUtils.getToday();
        }
        minTime += " 00:00:00";
        if (StringUtils.isBlank(maxTime)) {
            maxTime = DateUtils.getToday();
        }
        maxTime += " 23:59:59";
        return xcService.history(gpssjinfo.getZdbh(),minTime,maxTime);
    }

    /**
     * 查询本地库中的百度鹰眼历史数据
     * @param gpssjinfo
     * @return
     */
    @PostMapping("/yyguiji")
    public ApiResponse<List<com.ldz.util.bean.Point>> getYyGuiJi(gpsSJInfo gpssjinfo){
        return service.getYyGuiJi(gpssjinfo);
    }

    @GetMapping("photoList")
    public ApiResponse<List<ClSpk>> photoList(Page<ClSpk> page){
        HttpServletRequest request = ContextUtil.getRequest();
        request.setAttribute("fileType","photo");
        return spkService.pager(page);
    }
    @GetMapping("videoList")
    public ApiResponse<List<ClSpk>> videoList(Page<ClSpk> page){
        HttpServletRequest request = ContextUtil.getRequest();
        request.setAttribute("fileType","video");
        return spkService.pager(page);
    }
    @GetMapping("photoGroup")
    public ApiResponse<List<Map<String,Object>>> photoGroup(Page<ClSpk> page){
        HttpServletRequest request = ContextUtil.getRequest();
        request.setAttribute("fileType","photo");
        return spkService.groupByDay(page);
    }
    @GetMapping("videoGroup")
    public ApiResponse<List<Map<String,Object>>> videoGroup(Page<ClSpk> page){
        HttpServletRequest request = ContextUtil.getRequest();
        request.setAttribute("fileType","video");
        return spkService.groupByDay(page);
    }

    @PostMapping("getZdInfo")
    public ApiResponse<ClZdgl> getZdInfo(String zdbh){
        return  zdglService.getZdInfo(zdbh);
    }

    @PostMapping("/check")
    public ApiResponse<String> check(String zdbh){
       return zdglService.check(zdbh);
    }


}


