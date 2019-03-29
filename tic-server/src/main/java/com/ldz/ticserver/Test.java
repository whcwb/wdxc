package com.ldz.ticserver;

import java.util.HashMap;
import java.util.Map;

import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.commonUtil.HttpUtil;
import com.ldz.util.commonUtil.JsonUtil;

public class Test {

	public static void main(String[] args)throws Exception {
		/*List<String> lines = FileUtils.readLines(new File("e:/data/access_info.log"), "GBK");
		for (int i=0; i<lines.size(); i++){
			String line = lines.get(i);
			if (line.indexOf("865923030038860") != -1){
				FileUtils.write(new File("e:/data/865923030038860.txt"), line+"\n", true);
			}			
		}*/
		/*Map<String,String> header = new HashMap<>();
		header.put("apicode","1231231231312");
		
		Map<String,String> params = new HashMap<>();
		params.put("deviceIds","851510000091");
		params.put("time", new Date().getTime()+"");
		
		while(true){
			System.out.println(HttpUtil.get("http://www.168car.net:9193/app/device/getWebsocketInfo", params));
			Thread.sleep(5000);
		}*/
		
		RequestCommonParamsDto dto = new RequestCommonParamsDto();
		dto.setDeviceId("865923030038753");
		//dto.setCmdType("999");
		dto.setCmdType("97");
		dto.setCmd("/storage/emulated/0/TicServer/20190303logs_0.txt");
		Map<String,String> header = new HashMap<>();
		header.put("Content-Type","application/json");
		String res = null;
		try {
			System.out.println("通过socket发送指令，结果："+dto.toString());
			res = HttpUtil.postJson("http://119.23.242.234:10088/api/set/",header, JsonUtil.toJson(dto));
			System.out.println("通过socket发送指令，结果："+res);
			if (res.contains("未在线")){
				System.out.println("当前终端未在线！");
            }
		} catch (Exception e) {
			System.out.println("发送失败");
		}
		/*String zdbh = "353211085290316";
		String[] gpsArray = new String[]{
				"114.2806883333,30.6273033333,29,180.0",
				"114.2806500000,30.6265883333,0,182.0",
				"114.2808166667,30.6264700000,24,64.0",
				"114.2822966667,30.6265283333,32,92.0",
				"114.2854133333,30.6250133333,21,110.0",
				"114.2857116667,30.6248933333,24,124.0",
				"114.2861316667,30.6246100000,12,126.0",
				"114.2862300000,30.6246033333,4,126.0",
				"114.2870533333,30.6242716667,27,112.0",
				"114.2953116667,30.6197433333,53,120.0",
				"114.2974883333,30.6187750000,61,0.0",
				"114.3085433333,30.6124850000,50,124.0",
				"114.3123233333,30.6101383333,46,126.0",
				"114.3140983333,30.6090066667,51,126.0",
				"114.3160500000,30.6077450000,58,126.0",
				"114.3628833333,30.5710183333,56,214.0",
				"114.3486933333,30.5415333333,60,160.0",
				"114.3483650000,30.5433383333,60,174.0",
				"114.3496100000,30.5397916667,49,152.0",
				"114.3503966667,30.5383500000,13,76.0",
				"114.3502883333,30.5377916667,30,196.0",
				"114.3503400000,30.5373516667,7,184.0",
				"114.3504600000,30.5371900000,0,0.0",
				"114.3505400000,30.5370200000,0,0.0",
				"114.3506033333,30.5368383333,3,188.0",
				"114.3505650000,30.5364766667,13,156.0",
				"114.3506716667,30.5365766667,19,38.0",
				"114.3509500000,30.5375033333,17,354.0",
				"114.3510283333,30.5376550000,11,32.0",
				"114.3512100000,30.5376850000,22,86.0"
		};
		// TODO Auto-generated method stub
		Map<String, String> postHeaders = new HashMap<>();
		postHeaders.put("Content-type", "application/json");
		for (int i=0; i<gpsArray.length; i++){
			RequestCommonParamsDto dto = new RequestCommonParamsDto();
			dto.setDeviceId(zdbh);
			dto.setStartTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
			dto.setEndTime(dto.getStartTime());
			dto.setSczt("10");
			dto.setFxj(gpsArray[i].split(",")[3]);
			dto.setSpeed(gpsArray[i].split(",")[2]);
			dto.setLatitude(gpsArray[i].split(",")[1]);
			dto.setLongitude(gpsArray[i].split(",")[0]);
			//logger.debug("请求了GPS上传的方法");
			
			String result = HttpUtil.postJson("http://119.23.242.234:9095/api/device/gps", postHeaders, JsonUtil.toJson(dto));
			
			System.out.println("响应结果："+result);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
	}

}
