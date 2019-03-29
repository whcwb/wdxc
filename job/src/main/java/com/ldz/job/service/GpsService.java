package com.ldz.job.service;

import com.ldz.dao.biz.model.ClGps;
import com.ldz.sys.base.BaseService;

public interface GpsService extends BaseService<ClGps, String>{
	
	 /*
     * 插入一条数据 如果数据存在则更新
     * 
     */
    void insetAndUpdate(ClGps entity);
	
    
    /*
     * 根据终端编号 将redis里面的gps缓存存入数据库中
     */
    void InsetRedisToDb();
    
 
    /*
     * 根据终端编号 将存入百度鹰眼的gps纠偏后的数据写入更新
     */
    
    void guiJiJiuPian(String zdbh, long startTime, long endTime);
}
