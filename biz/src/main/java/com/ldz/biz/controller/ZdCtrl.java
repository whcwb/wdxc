package com.ldz.biz.controller;

import com.github.pagehelper.Page;
import com.ldz.dao.biz.model.ClZdgl;
import com.ldz.dao.dwq.model.GpsBean;
import com.ldz.service.biz.interfaces.GpsService;
import com.ldz.service.biz.interfaces.ZdglService;
import com.ldz.dao.obd.model.GpsObdMessageBean;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.redis.RedisTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 终端管理
 */
@RestController
@RequestMapping("api/zdgl")
public class ZdCtrl extends BaseController<ClZdgl,String> {
    @Autowired
    private ZdglService service;
    @Autowired
    private GpsService gpsService;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Override
    protected BaseService<ClZdgl, String> getBaseService() {
        return service;
    }

    @GetMapping("getWebsocketInfo")
    public ApiResponse<List<String>> getWebsocketInfo(String deviceIds){
        return gpsService.getWebsocketInfo(deviceIds);
    }
    @GetMapping("redisCache")
    public ApiResponse<String> redisCache(){
        return service.redisCache();
    }

    @PostMapping("/saveList")
    public ApiResponse<String> saveList(String zdbhs,ClZdgl entity){
        return service.saveList(zdbhs,entity);
    }

    @Override
    @RequestMapping(value="/save", method={RequestMethod.POST})
    public ApiResponse<String> save(@Valid ClZdgl znzp){
        return service.saveEntity(znzp);
    }

    @Override
    @RequestMapping(value="/update", method={RequestMethod.POST})
    public ApiResponse<String> update(@Valid ClZdgl entity){
        return service.updateEntity(entity);
    }

    /**
     * 查询所有未被绑定的未被停用的终端
     * @return
     */
    @RequestMapping(value="/getunboundlist", method={RequestMethod.POST})
    public ApiResponse<List<ClZdgl>> UnboundList(){
        return service.unboundList();
    }
    /*
     * 获取设备状况展示
     */
    @GetMapping("/zdcx")
    public ApiResponse<Map<String, Integer>> getzdxc(){

    	return service.getzdxc();
    }

    @RequestMapping("getVersionInfo")
    public ApiResponse<String> getVersionInfo(String deviceId){
        return service.getVersionInfo(deviceId);
    }

    /**
     * 上传excel文件，批量导入终端
     */
    @PostMapping("/excel")
    public ApiResponse<List<String>> saveBatch(String filePath) throws IOException {
        return service.saveBatch(filePath);
    }

    /**
     * 根据机构代码查询机构下的终端  或 查询机构下子机构的所有终端
     */
    @PostMapping("/getAllZd")
    public ApiResponse<String> getAllZd(Page<ClZdgl> page){
        return service.getAllZd(page);
    }

    /**
     * 检测接口
     */
    @PostMapping("/check")
    public ApiResponse<String> check(String zdbh){
        return service.check(zdbh);
    }

    /**
     * 终端设备延期接口
     */
    @PostMapping("/delay")
    public ApiResponse<String> updateDelay(String zdbh, String fwnx) throws ParseException {
        return service.updateDelay(zdbh,fwnx);
    }

    /**
     * 批量延期
     */
    @PostMapping("/delayList")
    public ApiResponse<String> updateDelayList(String zdbhs, String fwnx) throws ParseException {
        return service.updateDelayList(zdbhs,fwnx);
    }

    /**
     * 获取单个 obd 的详细信息
     */
    @PostMapping("/getObdInfo")
    public ApiResponse<GpsObdMessageBean> getObdInfo(){
        return service.getObdInfo();
    }

    /**
     * 获取定位器的详细信息
     */
    @PostMapping("/getDwqInfo")
    public ApiResponse<GpsBean> getDwqInfo(){
        return service.getDwqInfo();
    }

}
