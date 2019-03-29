package com.ldz.job.job;

import com.ldz.dao.biz.mapper.ClZdglMapper;
import com.ldz.dao.biz.model.ClZdgl;
import com.ldz.job.service.GpsService;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**  
 * 定时器说明:每隔6小时把上传百度鹰眼的点位导入数据库
 * 
 * @author liuzhihao
 *
 */
// 在成功执行了job类的execute方法后,更新JobDetail中JobDataMap的数据
@PersistJobDataAfterExecution
// 等待上一次任务执行完成，才会继续执行新的任务
@DisallowConcurrentExecution
public class GpsJuPianJob implements Job {
	Logger errorLog = LoggerFactory.getLogger("error_info");

	@Autowired
	private ClZdglMapper clzdglmapper;
	@Autowired
	private GpsService service;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		// 百度鹰眼纠偏接口只支持查询24小时内的， 且设置了3秒超时， 故将轨迹查询分段查询
		List<ClZdgl> gpslist = clzdglmapper.selectAll();
	    List<String> zdbhs =gpslist.stream().filter(s->StringUtils.isNotEmpty(s.getZdbh())).map(ClZdgl::getZdbh).collect(Collectors.toList());

	    long endTime = System.currentTimeMillis(); // 结束时间
		//12小时前
		long startTime = endTime - (43200000);  // 开始时间
		try {
			for (String zdbh : zdbhs) {
				service.guiJiJiuPian(zdbh,startTime,endTime);
			}
		} catch (Exception e) {
			errorLog.error("同步鹰眼纠偏数据异常", e);
			JobExecutionException e2 = new JobExecutionException(e);
			// 当任务执行失败时，立即停止所有相关这个任务的触发器
			e2.setRefireImmediately(true);
		}

	}

}
