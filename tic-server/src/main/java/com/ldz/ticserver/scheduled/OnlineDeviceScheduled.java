//package com.ldz.ticserver.scheduled;
//
//import java.util.Iterator;
//import java.util.Set;
//import java.util.concurrent.Executor;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.ldz.ticserver.plugins.push.AppPushUtils;
//import com.ldz.ticserver.service.BizApiService;
//import com.ldz.util.bean.Consts;
//import com.ldz.util.bean.RequestCommonParamsDto;
//
//@Component
//public class OnlineDeviceScheduled {
//
//	@Autowired
//	private StringRedisTemplate redisDao;
//
//	@Autowired
//	private BizApiService bizApiService;
//
//	@Autowired
//	private Executor executor;
//
//	/**
//	 * 检查设备是否在线状态(两分钟检查一次)
//	 * */
//	@Scheduled(cron = "0 0/2 * * * ?")
//    public void checkCarOnline(){
//		Set<String> resultSet = redisDao.boundSetOps(Consts.CAR_ONLINE_KEY).members();
//		System.err.println("两分钟执行一次的方法");
//		if(resultSet!=null && resultSet.size()>0){
//
//			Iterator<String> it = resultSet.iterator();
//			while(it.hasNext()){
//				String deviceIdAll = it.next();
//				executor.execute(new ThreedOnlineCheck(deviceIdAll));
//			}
//
//		}
//    }
//
//	/**
//	 * 检测用户是否在线的线程
//	 * @author wanggang
//	 *
//	 */
//	class ThreedOnlineCheck extends Thread{
//		String carIdAll = "";
//		public ThreedOnlineCheck(String d){
//			this.carIdAll = d;
//		}
//		public ThreedOnlineCheck(){
//
//		}
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			super.run();
//			String[] clientId = carIdAll.split(Consts.CAR_SPLITE);
//			if(AppPushUtils.checkIdMessage(clientId[0]).getCode() == 11){
//				//用户已经离线
//				RequestCommonParamsDto dto = new RequestCommonParamsDto();
//				dto.setDeviceId(clientId[0]);
//				dto.setEventType("80");//离线
//				bizApiService.pushData(dto);
//				redisDao.boundSetOps(Consts.CAR_ONLINE_KEY).remove(carIdAll);
//			}
//		}
//	}
//
//}
