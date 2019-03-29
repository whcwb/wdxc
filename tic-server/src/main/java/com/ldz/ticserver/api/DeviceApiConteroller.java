package com.ldz.ticserver.api;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.ticserver.service.BizApiService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.Consts;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.commonUtil.JsonUtil;

/**
 * 设备信息收集接口
 * @author wanggang
 *
 */
@RestController
@RequestMapping("/api/device")
public class DeviceApiConteroller {
	Logger accessLog = LoggerFactory.getLogger("access_info");
	private static final Logger logger = LoggerFactory.getLogger(DeviceApiConteroller.class);
	@Autowired
	private BizApiService bizApiService;
	@Autowired
	private StringRedisTemplate redisDao;
	
	
	@RequestMapping("/gps/test/{zdbh}")
	public ApiResponse<String> testGpsData(@PathVariable("zdbh") String zdbh){
		ApiResponse<String> ar = new ApiResponse<>();
		String[] gpsArray = new String[]{
				"114.2806883333","30.6273033333","29","180.0",
				"114.2806500000","30.6265883333","0","182.0",
				"114.2808166667","30.6264700000","24","64.0",
				"114.2822966667","30.6265283333","32","92.0",
				"114.2854133333","30.6250133333","21","110.0",
				"114.2857116667","30.6248933333","24","124.0",
				"114.2861316667","30.6246100000","12","126.0",
				"114.2862300000","30.6246033333","4","126.0",
				"114.2870533333","30.6242716667","27","112.0",
				"114.2953116667","30.6197433333","53","120.0",
				"114.2974883333","30.6187750000","61","0.0",
				"114.3085433333","30.6124850000","50","124.0",
				"114.3123233333","30.6101383333","46","126.0",
				"114.3140983333","30.6090066667","51","126.0",
				"114.3160500000","30.6077450000","58","126.0",
				"114.3628833333","30.5710183333","56","214.0",
				"114.3486933333","30.5415333333","60","160.0",
				"114.3483650000","30.5433383333","60","174.0",
				"114.3496100000","30.5397916667","49","152.0",
				"114.3503966667","30.5383500000","13","76.0",
				"114.3502883333","30.5377916667","30","196.0",
				"114.3503400000","30.5373516667","7","184.0",
				"114.3504600000","30.5371900000","0","0.0",
				"114.3505400000","30.5370200000","0","0.0",
				"114.3506033333","30.5368383333","3","188.0",
				"114.3505650000","30.5364766667","13","156.0",
				"114.3506716667","30.5365766667","19","38.0",
				"114.3509500000","30.5375033333","17","354.0",
				"114.3510283333","30.5376550000","11","32.0",
				"114.3512100000","30.5376850000","22","86.0"
		};
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (int i=0; i<gpsArray.length; i++){
					RequestCommonParamsDto dto = new RequestCommonParamsDto();
					dto.setDeviceId(zdbh);
					accessLog.debug("接收到客户端的数据postGpsData:"+JsonUtil.toJson(dto));
					//logger.debug("请求了GPS上传的方法");
					if(dto!=null && StringUtils.isNotBlank(dto.getDeviceId())){
						if(StringUtils.isNotBlank(dto.getSpeed())){
							if(Integer.parseInt(dto.getSpeed())>10 && dto.getSczt().equals("20")){//如果速度大于10 并且设备行驶状态是20【熄火】得时候，更改状态为行驶中
								dto.setSczt("10");
							}
						}
						bizApiService.pushData(dto);
					}
					
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				
			}
		}).start();;
		
		ar.setResult("");
		return ar;
	}
	
	/**
	 * 接收客户端发送的Gps坐标数据
	 * @return
	 */
	@RequestMapping("/gps")
	public ApiResponse<String> postGpsData(@RequestBody RequestCommonParamsDto dto){
		ApiResponse<String> ar = new ApiResponse<>();
		accessLog.debug("接收到客户端的数据postGpsData:"+JsonUtil.toJson(dto));
		//logger.debug("请求了GPS上传的方法");
		if(dto!=null && StringUtils.isNotBlank(dto.getDeviceId())){
			if(StringUtils.isNotBlank(dto.getSpeed())){
				if(Integer.parseInt(dto.getSpeed())>10 && dto.getSczt().equals("20")){//如果速度大于10 并且设备行驶状态是20【熄火】得时候，更改状态为行驶中
					dto.setSczt("10");
				}
			}
			bizApiService.pushData(dto);
		}
		
		//if(dto!=null && dto.getEventType()!=null && dto.getEventType().equals("60")){
		//	redisDao.boundSetOps(Consts.CAR_ONLINE_KEY).remove(dto.getDeviceId()+Consts.CAR_SPLITE+dto.getChannelId());
		//}else{
			redisDao.boundSetOps(Consts.CAR_ONLINE_KEY).add(dto.getDeviceId()+Consts.CAR_SPLITE+dto.getChannelId());
		//}
		ar.setResult(dto.toString());
		return ar;
	}
	
	/**
	 * 批量接收GPS离线坐标
	 * @param dtos
	 * @return
	 */
	@RequestMapping("/dbgps")
	public ApiResponse<String> postGpsDataAll(@RequestBody List<RequestCommonParamsDto> dtos){
		ApiResponse<String> ar = new ApiResponse<>();
		//logger.debug("请求了批量GPS上传的方法");
		
		for (RequestCommonParamsDto requestCommonParamsDto : dtos) {
			if(requestCommonParamsDto!=null && StringUtils.isNotBlank(requestCommonParamsDto.getDeviceId())){
				if(StringUtils.isNotBlank(requestCommonParamsDto.getSpeed())){
					if(Integer.parseInt(requestCommonParamsDto.getSpeed())>10 && requestCommonParamsDto.getSczt().equals("20")){//如果速度大于10 并且设备行驶状态是20【熄火】得时候，更改状态为行驶中
						requestCommonParamsDto.setSczt("10");
					}
				}
				bizApiService.pushData(requestCommonParamsDto);
			}
		}
		if(dtos!=null){
			redisDao.boundSetOps(Consts.CAR_ONLINE_KEY).add(dtos.get(0).getDeviceId()+Consts.CAR_SPLITE+dtos.get(0).getChannelId());
		}
		//ar.setResult(dto.toString());
		return ar;
	}
	
	
	/**
	 * 接收上传的汽车速度信息数据
	 * @return
	 */
	@RequestMapping("/speed")
	public ApiResponse<String> postSpeedData(@RequestBody RequestCommonParamsDto dto){
		ApiResponse<String> ar = new ApiResponse<>();
		
		
		return ar;
	}
	
	/**
	 * 接收上传的其它数据
	 * @return
	 */
	@RequestMapping("/other")
	public ApiResponse<String> postOtherData(@RequestBody RequestCommonParamsDto dto){
		ApiResponse<String> ar = new ApiResponse<>();
		
		
		return ar;
	}
	
	/**
	 * 接收点火数据
	 * @return
	 */
	@RequestMapping("/ignition")
	public ApiResponse<String> postIgnitionData(@RequestBody RequestCommonParamsDto dto){
		ApiResponse<String> ar = new ApiResponse<>();
		
		
		return ar;
	}
	
	/**
	 * 接收熄火数据
	 * @return
	 */
	@RequestMapping("/flameout")
	public ApiResponse<String> postFlameoutData(@RequestBody RequestCommonParamsDto dto){
		ApiResponse<String> ar = new ApiResponse<>();
		
		
		return ar;
	}
	
	
	/**
	 * 接收上传文件后的数据
	 * @return
	 */
	@RequestMapping("/fileparams")
	public ApiResponse<String> postUploadData(@RequestBody RequestCommonParamsDto dto){
		ApiResponse<String> ar = new ApiResponse<>();
		
		
		return ar;
	}
	
}
