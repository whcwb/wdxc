package com.ldz.ticserver;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.commonUtil.HttpUtil;
import com.ldz.util.commonUtil.JsonUtil;

public class Test {

	public static void main(String[] args)throws Exception {
		//testBD();
		//postCmd();
		pushMsg();
		//postLanxin();
		//postLanxinPcd();
		//postLanxinPcdAdd();
		//postLanxinLogin();
		//Hex.encodeHex(data)
		String salt = "1";
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
        if (salt != null) {
            digest.reset();
            digest.update(salt.getBytes());
        }
        byte[] hashed = digest.digest("app".getBytes());
        /*int iterations = hashIterations - 1; //already hashed once above
        //iterate remaining number:
        for (int i = 0; i < iterations; i++) {
            digest.reset();
            hashed = digest.digest(hashed);
        }*/
		//System.out.println(HashUtils.encrypt("app".getBytes(), "1", "SHA-1"));
		//postLanxinPcdMdf();
		//postLanxinClxx();
		
		//System.out.println(MD5Util.getMD5String("123456").toUpperCase());
		//postLanxinLogin();
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
	
	public static void pushMsg() throws Exception{
		String ymd = DateTime.now().toString("YYYYMMdd");
		String utf8Ymd = MD5Util.getMD5String(ymd);
		System.out.println(utf8Ymd);
		
		HashMap map =new HashMap();
	    map.put("puid",  "100132791");
	    map.put("sn", utf8Ymd);
	    map.put("content", "测试消息");
	    // 54835224
	    map.put("topuids", "54835224");
		String res = null;
		/*try {
			
			res = HttpUtil.post("https://campus.chaoxing.com/interface/sendNotice", map, "UTF-8");
			System.out.println("结果："+res);
			
		} catch (Exception e) {
			System.out.println("发送失败");
		}*/
		Document doc = Jsoup.connect("https://campus.chaoxing.com/interface/sendNotice")
			 .data("puid", "100132791")
			 .data("sn", utf8Ymd.toUpperCase())
			 .data("content", URLEncoder.encode("测试消息", "UTF-8"))
			 .data("pcode", "147239")
			 .data("topuids", "54835224").post();
		
		System.out.println(doc.text());
	}
	
	/**
	 * day_code为每个接口验证码
	 * {"day_code":"-990-1261202213114475-561171248-99401046114212-7313-1514-2015","code":"1"}
	 */
	public static void postLanxin(){
		
		String cus_code="-9101271952-203214-405-226-87-808-17926101911-891253131001411015";
		HashMap map =new HashMap();
		map.put("cus_code", cus_code);
		Map<String,String> header = new HashMap<>();
		header.put("Content-Type","application/json");
		String res = null;
		try {
			System.out.println(JsonUtil.toJson(map));
			res = HttpUtil.post("http://111.47.23.29:9999/idServer/data/dataAction!loadCode.do?cus_code="+cus_code);
			//res = HttpUtil.postJson("http://192.168.31.233:8080/idServer/data/dataAction!loadCode.do",header, JsonUtil.toJson(map));
			System.out.println("结果："+res);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("发送失败");
		}
	}
	
	public static void postLanxinClxx(){
		//950821321831054-415-526-317-838-11491261010611301251133614-6915
		//950821321831054-415-526-317-838-11491261010611301251133614-6915
		String day_code="-670-241-142-523124-39510661127-588649-50102911-39129113-314-6715";
		HashMap map =new HashMap();
		String password = MD5Util.getMD5String("123456").toUpperCase();
	    String loginname = "hydl";
	    map.put("day_code", day_code);
	    map.put("appid", "-990-1111-202-4634741245-846-297-1258609");
	    map.put("json", "{\"loginname\":\""+loginname+"\",\"password\":\""+password+"\"}");
		Map<String,String> header = new HashMap<>();
		header.put("Content-Type","application/json");
		String res = null;
		try {
			
			res = HttpUtil.post("http://111.47.23.29:8888/appServer/clxx/clxxAction!findhphm.do", map);
			System.out.println("结果："+res);
			
		} catch (Exception e) {
			System.out.println("发送失败");
		}
	}
	
	public static void postLanxinPcd(){
		//950821321831054-415-526-317-838-11491261010611301251133614-6915
		//950821321831054-415-526-317-838-11491261010611301251133614-6915
		String day_code="890-1241582133434815556-51708-71911210281132124813-118146415";
		HashMap map =new HashMap();
		String password = MD5Util.getMD5String("123456").toUpperCase();
	    String loginname = "hydl";
	    map.put("day_code", day_code);
	    map.put("appid", "-580-1231-57277359410593611471108-449291");
	    map.put("json", "{\"pageNum\":1,\"pageSize\":20}");
		Map<String,String> header = new HashMap<>();
		header.put("Content-Type","application/json");
		String res = null;
		try {
			
			res = HttpUtil.post("http://111.47.23.29:8888/appServer/clxx/clxxAction!hphmlist.do", map);
			System.out.println("结果："+res);
			
		} catch (Exception e) {
			System.out.println("发送失败");
		}
	}
	
	public static void postLanxinPcdAdd() throws Exception{
		//950821321831054-415-526-317-838-11491261010611301251133614-6915
		//950821321831054-415-526-317-838-11491261010611301251133614-6915
		ObjectMapper mapper = new ObjectMapper();
		String day_code="890-1241582133434815556-51708-71911210281132124813-118146415";
		HashMap data =new HashMap();
		data.put("cph", "鄂A00001");
		data.put("did", "调用人");
		data.put("ycrs", "2");
		data.put("ycrbm", "调用部门");
		data.put("scdd", "出发地");
		data.put("mdd", "目的地");
		data.put("yjycsj", "2019-05-16");
		data.put("yjfhsj", "2019-05-16");
		data.put("ycsy", "用车事由");
		data.put("ycfw", "01");
		data.put("vid", "0122222");
		//data.put("sqr", "申请人");
		
		HashMap map =new HashMap();
	    map.put("day_code", day_code);
	    map.put("appid", "240151-552203118412351266507481029-4610-");
	    map.put("json", mapper.writeValueAsString(data));
	    
		Map<String,String> header = new HashMap<>();
		header.put("Content-Type","application/json");
		String res = null;
		try {
			
			res = HttpUtil.post("http://111.47.23.29:9999/appServer/clxx/clxxAction!hphmadd.do", map);
			System.out.println("结果："+res);
			
		} catch (Exception e) {
			System.out.println("发送失败");
		}
	}
	
	public static void postLanxinPcdMdf() throws Exception{
		//950821321831054-415-526-317-838-11491261010611301251133614-6915
		//950821321831054-415-526-317-838-11491261010611301251133614-6915
		ObjectMapper mapper = new ObjectMapper();
		String day_code="890-1241582133434815556-51708-71911210281132124813-118146415";
		
	
		HashMap map =new HashMap();
	    map.put("day_code", day_code);
	    map.put("appid", "-250-701772563-1234-1045946-977-40812593");
	    map.put("json", "{\"cph\":\"鄂A59180\",\"did\":\"张蕾\",\"mdd\":\"王家湾\",\"scdd\":\"本单位停车场\",\"sqr\":\"4913\",\"sqrname\":\"胡志明\",\"sqsj\":\"2017-11-09\",\"vid\":\"8580\",\"ycfw\":\"01\",\"ycrbm\":\"行政部\",\"ycrs\":\"1\",\"ycsy\":\"办公\",\"yjfhsj\":\"2017-11-09\",\"yjycsj\":\"2017-01-01\"}");
	    
		Map<String,String> header = new HashMap<>();
		header.put("Content-Type","application/json");
		String res = null;
		try {
			
			res = HttpUtil.post("http://111.47.23.29:9999/appServer/clxx/clxxAction!hphmedit.do", map);
			System.out.println("结果："+res);
			
		} catch (Exception e) {
			System.out.println("发送失败");
		}
	}
	
	/**
	 * {"Message":"数据下载成功","code":"1","USERID":"720166550986263"}
	 */
	public static void postLanxinLogin() throws Exception{
		//950821321831054-415-526-317-838-11491261010611301251133614-6915
		//950821321831054-415-526-317-838-11491261010611301251133614-6915
		String day_code="-90091132-573-594665-1026627-498-39-110-14119512-46131414-2215";
		HashMap map =new HashMap();
		
	    String loginname = "manager";
	    String password = HashUtils.encrypt(loginname, "manager", "SHA-1");
	    map.put("day_code", day_code);
	    map.put("appid", "1210421-982423-594-665296-12675080910210");
	    map.put("json", "{\"loginname\":\""+loginname+"\",\"password\":\""+password+"\"}");
	    System.out.println(map);
		Map<String,String> header = new HashMap<>();
		header.put("Content-Type","application/json");
		String res = null;
		try {
			
			res = HttpUtil.post("http://111.47.23.29:9999/appServer/clxx/clxxAction!login.do", map);
			System.out.println("结果："+res);
			
		} catch (Exception e) {
			System.out.println("发送失败");
		}
	}
	
	public static void postCmd(){
		RequestCommonParamsDto dto = new RequestCommonParamsDto();
		dto.setDeviceId("865923030011313");
		//dto.setCmdType("999");
		dto.setCmdType("97");
		dto.setCmd("/storage/emulated/0/TicServer/20190502logs_0.txt");
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
	}
	
	public static void  testBD(){
		Map<String,String> params = new HashMap<>();
		params.put("uid","1884");
		params.put("startTime","2019-4-23");
		params.put("endTime","2019-4-23");
		String res = null;
		try {
			res = HttpUtil.post("http://api.admin.bidostar.com/toB/route_trace/routes.json", params, "UTF-8");
			System.out.println("发送指令，结果："+res);
			
		} catch (Exception e) {
			System.out.println("发送失败");
		}
		/*params.put("name","wh-gl");
		params.put("password","bdldz135");
		String res = null;
		try {
			res = HttpUtil.post("http://api2.admin.bidostar.com/member/token", params, "UTF-8");
			System.out.println("发送指令，结果："+res);
			
		} catch (Exception e) {
			System.out.println("发送失败");
		}*/
	}

}
