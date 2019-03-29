package com.ldz.util.gps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ldz.util.commonUtil.HttpUtil;
import com.ldz.util.gps.bean.RouteMatrixBean;

/**
 * Baidu地图Web api工具类
 * @author Lee
 *
 */
public class BaiduWebApi {
	//Web api所使用的AK值
	public final static String AK = "7hrRdzQAWNiBtYMdgdAgxqWOfSB8Rzcd";

	/**
	 * 批量算路服务:用户可通过该服务，根据起点和终点，返回路线规划距离和行驶时间。
	 * @param origins			起点经纬度:坐标格式为：lat<纬度>,lng<经度>|lat<纬度>,lng<经度>.最多传50个点
	 * @param destinations		终点经纬度:坐标格式为：lat<纬度>,lng<经度>|lat<纬度>,lng<经度>.最多传50个点
	 * @param drvType			计算方式：driving-驾车，riding-骑行，walking-步行。默认为驾车
	 * @return
	 */
	public static RouteMatrixBean routematrix(List<String> origins, List<String> destinations, String drvType){
		RouteMatrixBean result = new RouteMatrixBean();
		if (origins.size() == 0 || origins.size() > 50 || destinations.size() == 0 || destinations.size() > 50){
			result.setStatus(2);
			result.setMessage("参数错误！");
		}else{
			if (StringUtils.isEmpty(drvType)){
				drvType = "driving";
			}
			String url = "https://api.map.baidu.com/routematrix/v2/" + drvType;
			Map<String, String> params = new HashMap<String, String>();
			params.put("output", "json");
			params.put("ak", AK);
			params.put("origins", StringUtils.join(origins, "|"));
			params.put("destinations", StringUtils.join(destinations, "|"));
			String resultStr = HttpUtil.get(url, params);
			
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			try {
				result = mapper.readValue(resultStr, RouteMatrixBean.class);
			} catch (Exception e) {
				result.setStatus(404);
				result.setMessage("接口请求异常！");
			}
		}
		
		return result;
	}
}
