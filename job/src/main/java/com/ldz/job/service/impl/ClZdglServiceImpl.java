package com.ldz.job.service.impl;

import com.ldz.job.bean.GpsInfo;
import com.ldz.dao.biz.mapper.ClZdglMapper;
import com.ldz.dao.biz.model.ClGps;
import com.ldz.dao.biz.model.ClZdgl;
import com.ldz.job.service.ClZdglService;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.HttpUtil;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.redis.RedisTemplateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class ClZdglServiceImpl extends BaseServiceImpl<ClZdgl,String> implements ClZdglService {

	private static final Logger log = LoggerFactory.getLogger(ClZdglServiceImpl.class);
	Logger errorLog = LoggerFactory.getLogger("error_info");
	Logger accessLog = LoggerFactory.getLogger("access_info");
	@Value("${ticserver.url}")
	private String ticserverurl;

	@Value("${biz.url}")
	private String bizurl;

	@Value("${znzp.url}")
	private String znzpurl;

	@Autowired
	private Executor executor;

	@Autowired
	private RedisTemplateUtil redis;

	@Autowired
	private ClZdglMapper clZdglMapper;


	@Override
	protected Mapper<ClZdgl> getBaseMapper() {
		return clZdglMapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<String> checkOnline() {

		// 获取所有正常的在线设备
		SimpleCondition condition = new SimpleCondition(ClZdgl.class);
		condition.eq(ClZdgl.InnerColumn.zt.name(), "00");
		condition.and().andCondition(" ( ZXZT = '10' or ZXZT = '00' )");
		List<ClZdgl> gpslist = findByCondition(condition);

		// 获取所有正常的设备
		/*ClZdgl clZdgl = new ClZdgl();
		clZdgl.setZt("00");
		List<ClZdgl> gpslist = clZdglMapper.select(clZdgl);*/
		/*
		 * List<String> zubhList = gpslist.stream().filter(s ->
		 * StringUtils.isNotEmpty(s.getZdbh()))
		 * .map(ClZdgl::getZdbh).collect(Collectors.toList());
		 */

		Map<String, ClZdgl> collect = gpslist.stream().filter(s -> StringUtils.isNotEmpty(s.getZdbh()))
				.collect(Collectors.toMap(ClZdgl::getZdbh, ClZdgl -> ClZdgl));

		Set<String> keySet = collect.keySet();

		for (String zdbh : keySet) {
			ApiResponse<String> bean = null;
			try {
				String string = HttpUtil.get(ticserverurl + zdbh);
				bean = JsonUtil.toBean(string, ApiResponse.class);
			} catch (Exception e) {
				log.error(e.getMessage());
				continue;
			}
			if (bean.getCode() != 200) {

				ClZdgl clZdgl2 = collect.get(zdbh);
				clZdgl2.setZxzt("20");
				clZdglMapper.updateByPrimaryKeySelective(clZdgl2);

                //独立线程通知其他服务器离线消息
				executor.execute(new Runnable() {

					@Override
					public void run() {

						// 并将离线消息通知到gps上传
						ApiResponse<String> senML = senML(zdbh, bizurl);
						accessLog.debug(senML+"biz接口离线消息返回");

						// mysql版本不需要智能站牌
//						ApiResponse<String> znzpsenML = senML(zdbh, znzpurl);
//						accessLog.debug(znzpsenML+"znzp接口离线消息返回");


					}
				});

				log.info("更新了一条正常的设备状态终端编号为:" + zdbh);
			}

		}

		return ApiResponse.success();

	}

	@SuppressWarnings("unchecked")
	public ApiResponse<String> senML(String zdbh, String url) {
		String bean2 = (String) redis.boundValueOps(ClGps.class.getSimpleName() + zdbh).get();
		ClGps object2 = JsonUtil.toBean(bean2, ClGps.class);
		GpsInfo gpsinfo = new GpsInfo();
		gpsinfo.setDeviceId(zdbh);
		gpsinfo.setEventType("80");
		//百度经纬度
		gpsinfo.setLatitude(object2.getBdjd().toString());
		gpsinfo.setLongitude(object2.getBdwd().toString());
		gpsinfo.setFxj(object2.getFxj().toString());
		gpsinfo.setGpsjd(object2.getJd().toString());

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String starttime = simpleDateFormat.format(object2.getCjsj());
		gpsinfo.setStartTime(starttime);
		String postEntity = JsonUtil.toJson(gpsinfo);
		ApiResponse<String> apiResponse = null;
		Map<String, String> postHeaders = new HashMap<String, String>();
		postHeaders.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		try {
			String postJson = HttpUtil.postJson(url, postHeaders, postEntity);
			apiResponse = (ApiResponse<String>) JsonUtil.toBean(postJson, ApiResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return apiResponse;
	}

	public static void main(String[] args) {

		String url1 = "http://47.98.39.45:9095/api/push/checkOnlin/";
		String aString = "865923030032376";
		String string = HttpUtil.get(url1 + aString);
		System.out.println(string);

	}
}
