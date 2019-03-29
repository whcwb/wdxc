package com.ldz.ticserver.api;

import com.ldz.ticserver.plugins.push.AppPushUtils;
import com.ldz.ticserver.plugins.push.PushModel;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.commonUtil.HttpUtil;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.redis.RedisTemplateUtil;
import net.sf.json.util.JSONUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 指令发送接口
 * @author wanggang
 *
 */
@RestController
@RequestMapping("/api/push")
public class PushApiController {

	@Value("${socketServerUrl}")
	private String socketServerUrl;

	Logger accessLog = LoggerFactory.getLogger("access_info");

	Logger errorLog = LoggerFactory.getLogger("error_info");
	//private
	@Autowired
	private RedisTemplateUtil redisTemplate;
	/**
	 * 向客户端发送指令
	 * @return
	 */
	@RequestMapping("/carcmd")
	public ApiResponse<String> pushCmdToClient(@RequestBody RequestCommonParamsDto dto){
		ApiResponse<String> ar = new ApiResponse<>();
		//RequestCommonParamsDto dto = JSON.parseObject(str, RequestCommonParamsDto.class); //JsonUtil.toBean(str, RequestCommonParamsDto.class);
		//任务为设备+指令+时间
		String taskId = dto.getDeviceId()+dto.getCmdType()+System.currentTimeMillis();
		PushModel pm = new PushModel();
		dto.setTaskId(taskId);
		pm.setClientId(dto.getDeviceId());
		pm.setPushData(dto);
		//消息类型为：透传消息
		pm.setPushType(4);
		accessLog.debug("下发指令：["+dto.toString()+"]");
		//System.err.println(dto0.toString());
		if(dto.getCmdType()!=null && dto.getCmdType().equals("11")||dto.getCmdType().equals("12")||dto.getCmdType().equals("13")){//发送拍照或者拍摄视频时的命令会用到
			ar.setResult(taskId);
			dto.setCmd(taskId);
			pm.setPushData(dto);
		}
		boolean checkOnline = false;
		RequestCommonParamsDto existDto = (RequestCommonParamsDto)redisTemplate.boundValueOps(RequestCommonParamsDto.class.getSimpleName()+"-"+dto.getDeviceId()).get();
		if (existDto != null && StringUtils.isNotBlank(existDto.getChannelId())){
			if(AppPushUtils.checkIdByChannelId(existDto.getChannelId()).getCode() == 11){
				//redisDao.boundSetOps(Consts.CAR_ONLINE_KEY).remove(deviceIdAll);
				//说明设备不在线，不做任何操作。。目前
			}else{
				checkOnline = true;
				pm.setClientId(existDto.getChannelId());
				AppPushUtils.pushMessageAllOrClientByChannelId(pm);
			}
		}else{
			//2018年11月2日。兼容之前版本问题
			if(AppPushUtils.checkIdMessage(dto.getDeviceId()).getCode() == 11){
				//redisDao.boundSetOps(Consts.CAR_ONLINE_KEY).remove(deviceIdAll);
				//说明设备不在线，不做任何操作。。目前
			}else{
				checkOnline = true;
				AppPushUtils.pushMessageAllOrClientByAlias(pm);
			}
		}

		if(checkOnline){
			ar.setMessage("操作成功");
		}else{

			accessLog.debug("下发指令失败，设备不在线,通过socket发送指令，指令数据：["+dto.toString()+"]");
//			ar.setCode(ApiResponse.FAILED);
//			ar.setMessage("设备不在线，发送指令失败");
			//使用新方法发送
			Map<String,String> header = new HashMap<>();
			header.put("Content-Type","application/json");
			String res = null;
			try {
				res = HttpUtil.postJson(socketServerUrl+"api/set",header, JsonUtil.toJson(dto));
				accessLog.debug("通过socket发送指令，结果："+res);
				if (res.contains("未在线")){
				    return ApiResponse.fail("当前终端未在线！");
                }
			} catch (Exception e) {
				errorLog.error("发送失败",e);
				return ApiResponse.fail("发送失败");
			}
			return ar;
		}
		return ar;
	}

	/**
	 * 验证客户端是否在线
	 * @return
	 */
	@GetMapping("/checkOnlin/{deviceId}")
	public ApiResponse<String> checkClientOnline(@PathVariable("deviceId") String deviceId){
		ApiResponse<String> ar = new ApiResponse<>();
		boolean checkOnline = false;
		RequestCommonParamsDto existDto = (RequestCommonParamsDto)redisTemplate.boundValueOps(RequestCommonParamsDto.class.getSimpleName()+"-"+deviceId).get();
		if (existDto != null && StringUtils.isNotBlank(existDto.getChannelId())){
			if(AppPushUtils.checkIdByChannelId(existDto.getChannelId()).getCode() == 11){
				//redisDao.boundSetOps(Consts.CAR_ONLINE_KEY).remove(deviceIdAll);
				//说明设备不在线，不做任何操作。。目前
			}else{
				checkOnline = true;
			}
		}else{
			//2018年11月2日。兼容之前版本问题
			if(AppPushUtils.checkIdMessage(deviceId).getCode() == 11){
				//redisDao.boundSetOps(Consts.CAR_ONLINE_KEY).remove(deviceIdAll);
				//说明设备不在线，不做任何操作。。目前
			}else{
				checkOnline = true;
			}
		}

		
		if(checkOnline){
			ar.setMessage("设备目前在线");
		}else{
//			ar.setCode(ApiResponse.FAILED);
//			ar.setMessage("设备不在线");
			//使用新接口判断是否在线
			Map<String,String> param = new HashMap<>();
			param.put("zdbh",deviceId);
			String res = HttpUtil.get(socketServerUrl+"api/checkOnline",param);
			accessLog.debug("通过socket发送指令，结果："+res);
			ApiResponse<String> result = JsonUtil.toBean(res,ApiResponse.class);
			return result;
		}
		return ar;
	}



}
