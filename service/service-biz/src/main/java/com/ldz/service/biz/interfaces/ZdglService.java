package com.ldz.service.biz.interfaces;

import com.github.pagehelper.Page;
import com.ldz.dao.biz.model.ClZdgl;
import com.ldz.dao.dwq.model.GpsBean;
import com.ldz.dao.obd.model.GpsObdMessageBean;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ZdglService extends BaseService<ClZdgl,String>{
    ApiResponse<String> bindAppUser(String userId,String deviceId);
    ApiResponse<String> unbindAppUser(String userId,String deviceId);
    ApiResponse<String> saveEntity(ClZdgl entity);
    /*
     * 插入一条数据 如果数据存在则更新
     *
     */
    void insetAndUpdate(ClZdgl entity);

    ApiResponse<String> updateEntity(ClZdgl entity);


    /**
     * 查询所有未被绑定的未被停用的终端
     * @return
     */
    ApiResponse<List<ClZdgl>> unboundList();
    //获取设备暂时
	ApiResponse<Map<String, Integer>> getzdxc();

    ApiResponse<String> getVersionInfo(String deviceId);

    ApiResponse<List<String>> saveBatch(String filePath) throws IOException;

    void saveBatch(List<ClZdgl> clZdgls);


    ApiResponse<String> saveList(String zdbhs, ClZdgl entity);

    ApiResponse<String> getAllZd(Page<ClZdgl> page);

    ApiResponse<String> check(String zdbh);

    ApiResponse<String> updateDelay(String zdbh, String fwnx) throws ParseException;

    ApiResponse<String> updateDelayList(String zdbhs, String fwnx) throws ParseException;

    ApiResponse<GpsObdMessageBean> getObdInfo();

    ApiResponse<GpsBean> getDwqInfo();

    ApiResponse<ClZdgl> getZdInfo(String zdbh);

    ApiResponse<String> redisCache();

}
