package com.ldz.job.job;

import com.ldz.dao.biz.mapper.ClZdglMapper;
import com.ldz.job.service.GpsService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 定时器说明:每隔1分钟定时从redis里面获取数据写入CLgps,CLgpsLs表中
 * 
 * @author 
 *
 */
// 在成功执行了job类的execute方法后,更新JobDetail中JobDataMap的数据
@PersistJobDataAfterExecution
// 等待上一次任务执行完成，才会继续执行新的任务
@DisallowConcurrentExecution
public class GpsSaveJob implements Job {
	Logger errorLog = LoggerFactory.getLogger("error_info");
	Logger accessLog= LoggerFactory.getLogger("access_info");  

	@Autowired
	private ClZdglMapper clzdglmapper;
	@Autowired
	private GpsService GpsService;


	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		
	/*	List<ClZdgl> gpslist = clzdglmapper.getZDNotYy();
	     for (ClZdgl clZdgl : gpslist) {
//			if (StringUtils.isEmpty(clZdgl.getSfyy())) {
				YyEntity yyEntity = new YyEntity();
				yyEntity.setAk(GuiJIApi.AK);
				yyEntity.setEntity_name(clZdgl.getZdbh());
				yyEntity.setService_id(GuiJIApi.SERVICE_ID);
				YingyanResponse changeEntity = GuiJIApi.changeEntity(yyEntity, GuiJIApi.saveEntityuRL);
				accessLog.debug(changeEntity+"");
				if (StringUtils.equals(changeEntity.getStatus(), "0")) {
					clZdgl.setSfyy("已上传鹰眼服务器");
					clzdglmapper.updateByPrimaryKeySelective(clZdgl);
				}
//			}
	    	 
		}
	     List<String> zdbhs =gpslist.stream().filter(s->StringUtils.isNotEmpty(s.getZdbh())).map(ClZdgl::getZdbh).collect(Collectors.toList());*/
	
	
	
		try {

			/*for (String zdbh : zdbhs) {*/
			GpsService.InsetRedisToDb();
			/*}*/
		} catch (Exception e) {
			errorLog.error("同步redis中gps数据异常", e);
			JobExecutionException e2 = new JobExecutionException(e);
			// 当任务执行失败时，立即停止所有相关这个任务的触发器
			e2.setRefireImmediately(true);
		}

	}
}
