package com.ldz.job.job;

import com.ldz.dao.biz.mapper.ClZdglMapper;
import com.ldz.dao.biz.model.ClZdgl;
import com.ldz.job.bean.GpsInfo;
import com.ldz.job.service.ClZdglService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.commonUtil.HttpUtil;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.redis.RedisTemplateUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 临时处理，用于更新设备版本和修改接口地址
 * 
 * @author liuzhihao
 *
 */
// 在成功执行了job类的execute方法后,更新JobDetail中JobDataMap的数据
@PersistJobDataAfterExecution
// 等待上一次任务执行完成，才会继续执行新的任务
@DisallowConcurrentExecution
public class DeviceUpdateSyncJob implements Job {
	@Autowired
	private ClZdglService clZdglService;
	Logger access_info = LoggerFactory.getLogger("error_info");
	
	/*@Resource(name="redisOtherDB")
	private RedisTemplateUtil redisTemplate;*/
	
	@Autowired
	private RedisTemplateUtil mainRedis;
	
	@Value("${testVersionCode}")
	private String testVersionCode;
	@Value("${testUpgradeUrl}")
	private String testUpgradeUrl;
	@Value("${testUpgradeApk}")
	private String testUpgradeApk;
	//一个终端一天最多发送升级指令5次
	private Map<String, Integer> uMap = new ConcurrentHashMap<String, Integer>();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		mainRedis.delete("whdxclglpt.ClZdglMapper");
		mainRedis.delete(ClZdglMapper.class.getName());
		try {
			String[] curVersionCodes = testVersionCode.split("-");
			int curCode = Integer.parseInt(curVersionCodes[0]);
			float curVer = Float.parseFloat(curVersionCodes[1].substring(2));
			Example condition = new Example(ClZdgl.class);
			condition.and().andNotEqualTo(ClZdgl.InnerColumn.version.name(), testVersionCode);
			List<ClZdgl> zdglList = clZdglService.findByCondition(condition);
			access_info.error("待升级的终端数量["+zdglList.size()+"]");
			//修改接口地址
			for (int i=0; i<zdglList.size(); i++){
				ClZdgl zdglTmp = zdglList.get(i);
				String verTmp = zdglTmp.getVersion();
				if (StringUtils.isNotBlank(verTmp)){
					String[] tmps = verTmp.split("-");
					int tmpCode = Integer.parseInt(tmps[0]);
					float tmpVer = Float.parseFloat(tmps[1].substring(2));
					if (tmpCode >= curCode || tmpVer > curVer){
						continue;
					}
				}
				
				ApiResponse<String> bean = null;
		        try {
		            String string = HttpUtil.get(testUpgradeUrl +"/push/checkOnlin/" + zdglTmp.getZdbh());
		            bean = JsonUtil.toBean(string, ApiResponse.class);
		        } catch (Exception e) {
		            
		        }
		        
		        if(ObjectUtils.isEmpty(bean) || bean.getCode() !=200) {
		        	//设备不在线就先不发送升级指令
		        	access_info.error("终端["+zdglTmp.getZdbh()+"]不在线，暂时不升级");
		        }else{
		        	String result = "";
					try {
						String mKey = zdglTmp.getZdbh() + DateTime.now().toString("yyyyMMdd");
						Integer getVal = (Integer)mainRedis.boundValueOps(mKey).get();
						if (getVal != null && getVal > 2){							
							access_info.error("["+zdglTmp.getZdbh()+"]一天最多升级3次["+getVal+"]");
							continue;
						}
						
						Map<String, String> postHeaders = new HashMap<String, String>();
						postHeaders.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);

						GpsInfo info = new GpsInfo();
						info.setCmdType("91");
						info.setCmd(testUpgradeUrl);
						info.setDeviceId(zdglTmp.getZdbh());
						String postEntity = JsonUtil.toJson(info);
						result = HttpUtil.postJson(testUpgradeUrl + "/push/carcmd", postHeaders, postEntity);

						/*if (getVal == null){
							mainRedis.boundValueOps(mKey).set(1, 24, TimeUnit.HOURS);
						}else{
							mainRedis.boundValueOps(mKey).set(new Integer(getVal + 1));
						}*/
						access_info.error("修改接口地址["+zdglTmp.getZdbh()+"]=["+result+"]");
					} catch (Exception e) {
						e.printStackTrace();
					}
		        }
			}
			//升级终端设备
			for (int i=0; i<zdglList.size(); i++){
				ClZdgl zdglTmp = zdglList.get(i);
				String verTmp = zdglTmp.getVersion();
				if (StringUtils.isNotBlank(verTmp)){
					String[] tmps = verTmp.split("-");
					int tmpCode = Integer.parseInt(tmps[0]);
					float tmpVer = Float.parseFloat(tmps[1].substring(2));
					if (tmpCode >= curCode || tmpVer > curVer){
						continue;
					}
				}
				
				ApiResponse<String> bean = null;
		        try {
		            String string = HttpUtil.get(testUpgradeUrl +"/push/checkOnlin/" + zdglTmp.getZdbh());
		            bean = JsonUtil.toBean(string, ApiResponse.class);
		        } catch (Exception e) {
		            
		        }
		        
		        if(ObjectUtils.isEmpty(bean) || bean.getCode() !=200) {
		        	//设备不在线就先不发送升级指令
		        	//access_info.error("终端["+zdglTmp.getZdbh()+"]不在线，暂时不升级");
		        }else{
		        	String result = "";
					try {
						String mKey = zdglTmp.getZdbh() + DateTime.now().toString("yyyyMMdd");
						Integer getVal = (Integer)mainRedis.boundValueOps(mKey).get();
						if (getVal != null && getVal > 2){							
							access_info.error("["+zdglTmp.getZdbh()+"]一天最多升级3次["+getVal+"]");
							continue;
						}
						
						Map<String, String> postHeaders = new HashMap<String, String>();
						postHeaders.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);

						GpsInfo info = new GpsInfo();
						info.setCmdType("90");
						info.setCmd(testUpgradeApk);
						info.setDeviceId(zdglTmp.getZdbh());
						String postEntity = JsonUtil.toJson(info);
						result = HttpUtil.postJson(testUpgradeUrl + "/push/carcmd", postHeaders, postEntity);

						access_info.error("升级APK["+zdglTmp.getZdbh()+"]=["+result+"],已升级次数："+getVal);
						
						
						if (getVal == null){
							mainRedis.boundValueOps(mKey).set(1, 24, TimeUnit.HOURS);
						}else{
							mainRedis.boundValueOps(mKey).set(new Integer(getVal + 1));
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
		        }
			}
		} catch (Exception e) {
			access_info.error("升级的终端数量异常", e);
			JobExecutionException e2 = new JobExecutionException(e);
			// 当任务执行失败时，立即停止所有相关这个任务的触发器
			e2.setRefireImmediately(true);
		}

	}

}
