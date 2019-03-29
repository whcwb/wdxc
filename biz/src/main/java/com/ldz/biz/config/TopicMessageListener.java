package com.ldz.biz.config;

import com.ldz.dao.biz.bean.GpsInfo;
import com.ldz.dao.biz.bean.WebsocketInfo;
import com.ldz.dao.biz.bean.gpsSJInfo;
import com.ldz.dao.biz.model.*;
import com.ldz.service.biz.interfaces.*;
import com.ldz.sys.model.SysZdxm;
import com.ldz.sys.service.ZdxmService;
import com.ldz.util.bean.*;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.HttpUtil;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.gps.DistanceUtil;
import com.ldz.util.redis.RedisTemplateUtil;
import com.ldz.util.yingyan.GuiJIApi;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//@Component
public class TopicMessageListener implements MessageListener {


    private String url;
    private String znzpurl;
    private String bizurl;
    public double distance;
    private double lowSpeed;
    private XcService xcService;
    private ClYyService clYyService;

    private GpsLsService gpsLsService;

    private GpsService gpsService;
    private ZdxmService zdxmService;
    private ZdglService zdglService;




    private ExecutorService pool = Executors.newSingleThreadExecutor();

    private RedisTemplateUtil redisTemplate;

    Logger error = LoggerFactory.getLogger("error_info");

    public TopicMessageListener(ZdxmService zdxmService,XcService xcService, ClYyService clYyService,
                                GpsService gpsService,GpsLsService gpsLsService, ZdglService zdglService, RedisTemplateUtil redisTemplate,String url,String znzpurl,String bizurl,double distance,double lowSpeed) {

        this.zdxmService = zdxmService;
        this.xcService = xcService;
        this.clYyService = clYyService;
        this.gpsService = gpsService;
        this.gpsLsService = gpsLsService;
        this.zdglService = zdglService;
        this.redisTemplate = redisTemplate;
        this.url = url;
        this.znzpurl = znzpurl;
        this.bizurl = bizurl;
        this.distance = distance;
        this.lowSpeed = lowSpeed;
    }

    /**
     * 根据 redis 失效时间存储行程
     *
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String itemValue = new String(message.getBody());
        error.error("TopicMessageListener.onMessage:"+itemValue);
        String topic =  new String(message.getChannel());
        error.error("topic:"+topic);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
        	if (StringUtils.contains(topic, "expired")) {
                if((StringUtils.contains(itemValue,"CX") && !StringUtils.contains(itemValue,"xc")) || StringUtils.contains(itemValue,"compencate")){
                    // 过期事件存储车辆行程
                    saveClXc(itemValue, simpleDateFormat);
                }else if(StringUtils.contains(itemValue,"offline")){
                    // 车辆离线状态更新
                    updateClZt(itemValue);
                }
            }else if (topic.equals("connect_timeout")){
                ClZdgl zdgl = zdglService.findById(itemValue);
                zdgl.setZxzt("20");
                zdglService.update(zdgl);
            }
        }catch(Exception e){
        	error.error("执行Redis事件异常", e);
        }   
    }


    public static void main(String[] args) {
        long s = DateTime.parse("2019-03-06 15:17:32", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().getTime();
        long e = DateTime.parse("2019-03-06 08:48:32", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().getTime();
        TrackJiuPian guijis = new TrackJiuPian();
        guijis.setAk(GuiJIApi.AK);
        guijis.setService_id(GuiJIApi.SERVICE_ID);
        guijis.setEntity_name("865923030038951");
        guijis.setEnd_time(String.valueOf(e/1000));
        guijis.setStart_time(String.valueOf(s/1000));
        guijis.setIs_processed("1");
        // 查询 去燥 ，抽希 ， 绑路 的坐标点
        guijis.setProcess_option("need_denoise=1,need_vacuate=1,need_mapmatch=1,transport_mode=driving");
        guijis.setSupplement_mode("driving");
        guijis.setSort_type("asc");
        guijis.setCoord_type_output("bd09ll");
        guijis.setPage_size("5000");
        guijis.setLow_speed_threshold(20);
        //  error.error(" guijis  +++++++ " + JsonUtil.toJson(guijis));
        // 查询 小时内的轨迹坐标点
        TrackPointsForReturn points = GuiJIApi.getPoints(guijis, GuiJIApi.getPointsURL);
        List<TrackPointsForReturn.Point> points2 = points.getPoints();
        for (TrackPointsForReturn.Point point : points2) {
            System.out.println("---------" + point.getSpeed());
            System.out.println("++6+++++" + BigDecimal.valueOf(point.getSpeed()));
        }

        String s1 = JsonUtil.toJson(points);
        System.out.println(points);
        GuiJiAnalyse analysis = GuiJIApi.getAnalysis(guijis);
        String ss = JsonUtil.toJson(analysis);
        System.out.println(ss);
    }

    /**
     * 更新车辆离线状态
     * @param itemValue
     */
    private void updateClZt(String itemValue) {
        List<String> zdbhs = Arrays.asList(itemValue.split("-"));
        String zdbh = zdbhs.get(1);
        ApiResponse<String> bean = null;
        if (itemValue.indexOf("dwq-") != -1 || itemValue.indexOf("obd-") != -1){
        	zdbh = zdbhs.get(2);
        	//定位器或OBD。保证定位器redis和biz的redis在同一个库中
        	try{
        		boolean isOnline = redisTemplate.keys(zdbh+"-*-ONLINE").iterator().hasNext();
        		error.error(itemValue+"离线状态["+isOnline+"]");
            	if (isOnline){
            		bean = ApiResponse.success();
            	}else{
            		bean = ApiResponse.fail();
            	}
        	}catch(Exception e){
        		
        	}
        }else{
        	//后视镜
        	try {
                String string = HttpUtil.get(url +"/push/checkOnlin/" + zdbh);
                bean = JsonUtil.toBean(string, ApiResponse.class);
            } catch (Exception e) {
                error.error("验证客户端是否在线接口异常：", e);
            }
        }
        
        redisTemplate.boundValueOps(itemValue).set(1,10,TimeUnit.MINUTES);
        
        if(ObjectUtils.isEmpty(bean)||bean.getCode() !=200) {
            boolean isOnline = false;
            //2018年10月24日。终端的版本号（版本号比对全部字符串）是字典配置的版本，这样就只把设备修改为熄火，速度值之类的更新为0，不做更新时间处理
            ClZdgl zdgl = zdglService.findById(zdbh);
            error.error("验证客户端是否在线接口异常："+zdgl);
            if (!ObjectUtils.isEmpty(zdgl)) {
                //字典项如果不存在，也不比较
                SimpleCondition condition = new SimpleCondition(SysZdxm.class);
                condition.eq(SysZdxm.InnerColumn.zdlmdm,"bbh");
                condition.eq(SysZdxm.InnerColumn.zddm,"value");
                List<SysZdxm> zdxms = zdxmService.findByCondition(condition);
                if (zdxms.size() != 0){
                    String value = zdxms.get(0).getZdmc();
                    if (value.equals(zdgl.getVersion())){
                        //如果一直把设备修改为熄火，速度值之类的更新为0，不做更新时间处理
                        isOnline = true;
                    }
                }
            }

            if (isOnline){
                zdgl.setZxzt("10");
                ClGps gps = gpsService.findById(zdbh);
                upateWebsocketInfo(zdbh,"10");
                if (gps != null){
                    gps.setYxsd("0");
                    gps.setFxj(new BigDecimal(0));
                    gpsService.update(gps);
                }
            }else{
                zdgl.setZxzt("20");
                upateWebsocketInfo(zdbh,"10");
                //独立线程通知其他服务器离线消息
                // 并将离线消息通知到gps上传
                //senML(zdbh, bizurl +"/pub/gps/save");
            }
            error.error("验证客户端是否在线接口异常："+zdgl);
            zdglService.update(zdgl);
        }else{
            ClZdgl zdgl = zdglService.findById(zdbh);
            error.error("zdgl："+zdgl);
            if (!ObjectUtils.isEmpty(zdgl)) {
                zdgl.setZxzt("10");
                zdglService.update(zdgl);
                upateWebsocketInfo(zdbh,"10");

                ClGps gps = gpsService.findById(zdbh);
                if (gps != null){
                    gps.setYxsd("0");
                    gps.setFxj(new BigDecimal(0));
                    gps.setGxsj(new Date());
                    gpsService.update(gps);
                }
            }
        }

    }

    private void upateWebsocketInfo(String deviceId,String zxzt){
        error.error("upateWebsocketInfo:"+deviceId+"-"+zxzt);
        String key = WebsocketInfo.class.getSimpleName() + deviceId;
        Iterator<Object> keys = redisTemplate.keys(key).iterator();
        if (!keys.hasNext()){
            return;
        }
        String json = (String) redisTemplate.boundValueOps(key).get();
        WebsocketInfo websocketInfo = JsonUtil.toBean(json,WebsocketInfo.class);
        if (websocketInfo == null)return;
        websocketInfo.setZxzt(zxzt);
        redisTemplate.boundValueOps(WebsocketInfo.class.getSimpleName() + deviceId).set(JsonUtil.toJson(websocketInfo));
    }

    @SuppressWarnings("unchecked")
     public ApiResponse<String> senML(String zdbh, String url) {
        String bean2 = (String) redisTemplate.boundValueOps(ClGps.class.getSimpleName() + zdbh).get();
        ClGps object2 = JsonUtil.toBean(bean2, ClGps.class);
        GpsInfo gpsinfo = new GpsInfo();
        gpsinfo.setDeviceId(zdbh);
        gpsinfo.setEventType("80");
        //百度经纬度
        gpsinfo.setLatitude(object2.getWd().toString());
        gpsinfo.setLongitude(object2.getJd().toString());
        gpsinfo.setFxj(object2.getFxj().toString());
        gpsinfo.setGpsjd(object2.getJd().toString());

        String starttime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
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


    /**
     * 存储过期车辆行程
     * @param itemValue
     * @param simpleDateFormat
     */
    private void saveClXc(String itemValue, SimpleDateFormat simpleDateFormat) {
        error.error("saveClXc:"+itemValue);
        String[] vals = itemValue.split(",");
        String type = vals[0];
        String zdbh = vals[1];
        String startTime = vals[2];
        String endTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
        error.error("clXcinfo:" + type + "," + zdbh + "," + startTime + "," + endTime);
        if (StringUtils.isBlank(endTime)){
        	return;
        }

        long s = DateTime.parse(startTime,DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().getTime();
        long e = DateTime.parse(endTime,DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().getTime();
        error.error("clXcinfo:" + s + " -- > " + e);

        if((e - s) < 60000 ){
            error.error("(e - s) < 60000 return");
            return;
        }
        if((e - s) > 60000 * 60 * 24 ){
            return;
        }
        /*if((e - s) < 60000 ){ // 开始时间与结束时间小于1分钟 ， 行程短 ， 过滤
            // 轨迹点不存储
            return;
        }*/
        TrackJiuPian guijis = new TrackJiuPian();
        guijis.setAk(GuiJIApi.AK);
        guijis.setService_id(GuiJIApi.SERVICE_ID);
        guijis.setEntity_name(zdbh);
        guijis.setEnd_time(String.valueOf(e/1000));
        guijis.setStart_time(String.valueOf(s/1000));
        error.error(" guijis: " + JsonUtil.toJson(guijis));
        String start_end =null;
        try {
             //start_end =  newGuiJiJiuPian(zdbh, startTime, endTime);  // 新纠偏接口
            //error.error("新纠偏接口:"+start_end);

            error.error(" guijis --- > " + JsonUtil.toJson(guijis));
            start_end = guiJiJiuPian(guijis); // 鹰眼纠偏接口
            error.error("["+zdbh+"]新纠偏接口:"+start_end);
        }catch (Exception e2){
            if(StringUtils.equals(type,"CX")) {
                // 百度轨迹点异常 ， 存储异常行程 ， 等待第二次纠偏
                redisTemplate.boundValueOps("compencate," + zdbh + "," + startTime).set("1", 1, TimeUnit.MINUTES);
                return;
            }else if(StringUtils.equals(type,"compencate")){
                // 存储当前原始轨迹点
                start_end=  saveGps(zdbh,startTime,endTime);
            }
        }

        if(StringUtils.isNotBlank(start_end)) {
            ClXc clXc = new ClXc();
            clXc.setClZdbh(zdbh);
            clXc.setXcKssj(startTime);
            try{
            	String[] arrs = start_end.split(",");
                clXc.setXcJssj(arrs[3]);

                error.error("解析起点终点地址");
            	String[] startPoint = arrs[0].split("-");
                String startAddress = getAddress(startPoint[1],startPoint[0]);
            	String[] endPoint = arrs[1].split("-");
                String endAddress = getAddress(endPoint[1],endPoint[0]);
                clXc.setStartAddress(startAddress);
                clXc.setEndAddress(endAddress);
            }catch(Exception ex){
            	clXc.setXcJssj(endTime);
            }
            clXc.setXcStartEnd(start_end);

			try {
                Date startDate = DateUtils.getDate(startTime,"yyyy-MM-dd HH:mm:ss");
                Date endDate = DateUtils.getDate(clXc.getXcJssj(),"yyyy-MM-dd HH:mm:ss");
                long subTime = endDate.getTime() - startDate.getTime();
                int vaildMinute = 1000 * 60 ;
                if (subTime < vaildMinute){
                	error.error("["+zdbh+"]-["+startTime+"="+clXc.getXcJssj()+"]行程时长小于1分钟，不存储");
                	return;
                }
                
                if (subTime > 1000 * 60 * 60 * 2){
                	// 超过2小时认为是疲劳驾驶
                	clXc.setPljs("1");
                }
            } catch (ParseException e1) {
                error.error("日期转换异常",e1);
            }
            String[] split = start_end.split(",");
            if(split.length >= 2 ){
                clXc.setXcLc(split[2]);
                clXc.setXcSc(((e -s ) / 1000 ) + "");
            }
            List<ClXc> entity = xcService.findByEntity(clXc);
            if(CollectionUtils.isEmpty(entity)) {
                // 轨迹分析
                GuiJiAnalyse fx = getFx(guijis);
                if(fx == null){
                    error.info(" 行程轨迹分析出错 ， 不存储轨迹分析 ， 只存储行程");
                }else {
                    // todo 添加行程的字段 存储
                    Map<String, Double> mSpeedAndAvgSpeed = gpsLsService.getMSpeedAndAvgSpeed(zdbh, startTime, endTime);
                    clXc.setXcZgss(mSpeedAndAvgSpeed.get("max")+"");
                    clXc.setXcPjss(mSpeedAndAvgSpeed.get("avg") + "");
                    /*clXc.setXcPjss(fx.getAverage_speed()+"");
                    clXc.setXcZgss(fx.getMax_speed()+"");*/
                    clXc.setXcCscs(fx.getSpeeding_num()+"");
                    clXc.setXcJjscs(fx.getHarsh_acceleration_num()+"");
                    clXc.setXcJsccs(fx.getHarsh_breaking_num()+"");
                    clXc.setXcJzwcs(fx.getHarsh_steering_num()+"");
                }
                xcService.saveEntity(clXc);
                redisTemplate.delete("start_end," + zdbh + "xc" + startTime);
                // 发送统计事件
                redisTemplate.convertAndSend("zdReport",clXc);
            }else{
                error.info("行程已存在，不存储");
            }
        }
    }

    private static String getAddress(String lat,String lng){
        String url = "http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location="+lat+","+lng+"&output=json&ak=evDHwrRoILvlkrvaZEFiGp30";
        String res = HttpUtil.get(url);
        int index = res.indexOf("formatted_address");
        if (index < 0) return "";
        String address = res.substring(index + 20);
        address = address.substring(0,address.indexOf("\""));
        return address;
    }


    /**
     * 百度新的纠偏接口
     */
    public String newGuiJiJiuPian(String zdbh, String startTime, String endTime){

        gpsSJInfo gpsSJInfo = new gpsSJInfo();
        gpsSJInfo.setStartTime(startTime);
        gpsSJInfo.setEndTime(endTime);
        gpsSJInfo.setZdbh(zdbh);
        List<ClGpsLs> gpsLs = gpsLsService.getGpsLs(gpsSJInfo);
        List<PointListBean> listBeans = new ArrayList<>();
        gpsLs.stream().forEach(clGpsLs -> {
            PointListBean pointListBean = new PointListBean();
            pointListBean.setCoord_type_input("bd09ll");
            pointListBean.setDirection(clGpsLs.getFxj().doubleValue());
            pointListBean.setLatitude(clGpsLs.getBdwd().doubleValue());
            pointListBean.setLongitude(clGpsLs.getBdjd().doubleValue());
            pointListBean.setLoc_time(clGpsLs.getCjsj().getTime() / 1000);
            pointListBean.setSpeed(Double.parseDouble(clGpsLs.getYxsd()));
            listBeans.add(pointListBean);
        });
        String point = GuiJIApi.trackPoint(listBeans);
        NewTrackPointReturn newTrackPointReturn = JsonUtil.toBean(point, NewTrackPointReturn.class);
        if(newTrackPointReturn.getDistance() <= distance){ // 里程小于100 m 过滤
            return null;
        }
        List<Clyy> yyList = new ArrayList<>();
        List<Point> points = newTrackPointReturn.getPoints() ;
        if(CollectionUtils.isNotEmpty(points)) {
            points.forEach(point1 -> {
                Clyy clyy = new Clyy();
                clyy.setDirection(point1.getDirection() + "");
                clyy.setZdbh(zdbh);
                clyy.setLatitude(BigDecimal.valueOf(point1.getLatitude()));
                clyy.setLongitude(BigDecimal.valueOf(point1.getLongitude()));
                clyy.setSpeed(BigDecimal.valueOf(point1.getSpeed()));
                clyy.setLoc_time(parse(point1.getLoc_time()));
                yyList.add(clyy);
            });
        }

        String start_end = yyList.get(0).getLongitude() + "-" + yyList.get(0).getLatitude() + "," + yyList.get(yyList.size()-1).getLongitude()+"-"+yyList.get(yyList.size()-1).getLatitude();
        if(StringUtils.equals( yyList.get(0).getLongitude() + "-" + yyList.get(0).getLatitude() , yyList.get(yyList.size()-1).getLongitude()+"-"+yyList.get(yyList.size()-1).getLatitude())){ // 开始点位和结束点位相同 ，不存储
            return null;
        }
        clYyService.saveBatch(yyList);

        return start_end;

    }





    /**
     * 存储百度鹰眼GPS
     */
    public String guiJiJiuPian(TrackJiuPian guijis) {
        error.error("guiJiJiuPian zdbh:"+guijis.getEntity_name());

        guijis.setIs_processed("1");
        // 查询 去燥 ，抽希 ， 绑路 的坐标点
        guijis.setProcess_option("need_denoise=1,need_vacuate=1,need_mapmatch=1,transport_mode=driving");
        guijis.setSupplement_mode("driving");
        guijis.setSort_type("asc");
        guijis.setCoord_type_output("bd09ll");
        guijis.setPage_size("5000");
        guijis.setLow_speed_threshold(lowSpeed);
        error.error(" guijis  +++++++ " + JsonUtil.toJson(guijis));
        // 查询 小时内的轨迹坐标点
        TrackPointsForReturn points = GuiJIApi.getPoints(guijis, GuiJIApi.getPointsURL);
        if(points.getDistance() <= distance){ // 里程小于100 m 过滤
            error.error("里程小于100 m 过滤");
            return null;
        }
        List<TrackPointsForReturn.Point> points2 = points.getPoints();
        error.error(" pointslength: " + points2.size());
        if (CollectionUtils.isNotEmpty(points2)) {
            List<Clyy> yyList = new ArrayList<>();

            for (TrackPointsForReturn.Point point : points2) {
                Clyy clyy = new Clyy();
                clyy.setDirection(point.getDirection() + "");
                clyy.setLatitude(BigDecimal.valueOf(point.getLatitude()));
                clyy.setLoc_time(parse(point.getLoc_time()));
                clyy.setLongitude(BigDecimal.valueOf(point.getLongitude()));
                clyy.setSpeed(BigDecimal.valueOf(point.getSpeed()));
                clyy.setZdbh(guijis.getEntity_name());
                List<Clyy> clyys = clYyService.findByEntity(clyy);
                if(CollectionUtils.isEmpty(clyys)) {
                    yyList.add(clyy);
                }
            }
            String start_end = yyList.get(0).getLongitude() + "-" + yyList.get(0).getLatitude() + "," + yyList.get(yyList.size()-1).getLongitude()+"-"+yyList.get(yyList.size()-1).getLatitude();
            if(StringUtils.equals( yyList.get(0).getLongitude() + "-" + yyList.get(0).getLatitude() , yyList.get(yyList.size()-1).getLongitude()+"-"+yyList.get(yyList.size()-1).getLatitude())){ // 开始点位和结束点位相同 ，不存储
                error.error("开始点位和结束点位相同 ，不存储");
                return null;
            }
            if(CollectionUtils.isEmpty(yyList)){
                error.error("yyList is empty");
                return null;
            }
            clYyService.saveBatch(yyList);

            return start_end + "," + points.getDistance() + "," + yyList.get(yyList.size()-1).getLoc_time();
        } else {
            error.error("points2 is empty");
            return null;
        }

    }

    /**
     * 存储原始轨迹点
     */
    public String saveGps(String zdbh,String startTime, String endTime){

        SimpleCondition simpleCondition = new SimpleCondition(ClGpsLs.class);
        simpleCondition.and().andBetween(ClGpsLs.InnerColumn.cjsj.name(),startTime,endTime);
        simpleCondition.eq(ClGpsLs.InnerColumn.zdbh.name(),zdbh);
        List<Clyy> yyList = new ArrayList<>();
        List<ClGpsLs> gpsLsList = gpsLsService.findByCondition(simpleCondition);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        gpsLsList.stream().forEach(clGpsLs -> {
            Clyy clyy = new Clyy();
            clyy.setDirection(clGpsLs.getFxj()+"");
            clyy.setLatitude(clGpsLs.getBdwd());
            clyy.setLoc_time(simpleDateFormat.format(clGpsLs.getCjsj()));
            clyy.setLongitude(clGpsLs.getBdjd());
            clyy.setSpeed(new BigDecimal(clGpsLs.getYxsd()));
            clyy.setZdbh(zdbh);
            yyList.add(clyy);
        });
        /*if(StringUtils.equals( yyList.get(0).getLongitude() + "-" + yyList.get(0).getLatitude() , yyList.get(yyList.size()-1).getLongitude()+"-"+yyList.get(yyList.size()-1).getLatitude())){ // 开始点位和结束点位相同 ，不存储
            return null;
        }*/
        String start_end = yyList.get(0).getLongitude() + "-" + yyList.get(0).getLatitude() + "," + yyList.get(yyList.size()-1).getLongitude()+"-"+yyList.get(yyList.size()-1).getLatitude();
        // 计算两点之间的最短距离
        double distance = DistanceUtil.getShortDistance(yyList.get(0).getLongitude().doubleValue(), yyList.get(0).getLatitude().doubleValue(), yyList.get(yyList.size() - 1).getLongitude().doubleValue(), yyList.get(yyList.size() - 1).getLatitude().doubleValue());
        clYyService.saveBatch(yyList);
        return start_end+","+distance+yyList.get(yyList.size() - 1).getLoc_time();
    }



    public String parse(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long aLong = time * 1000;
        Date date = new Date(aLong);
        String format = simpleDateFormat.format(date);
        return format;
    }

    public GuiJiAnalyse getFx(TrackJiuPian guijis){
        GuiJiAnalyse analysis = GuiJIApi.getAnalysis(guijis);
        if(analysis.getStatus() == 0){
            // 查询成功 ， 返回数据
            return analysis;
        }else {
            // 失败 返回空
            return null;
        }
    }




}
