package com.ldz.service.biz.impl;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.ldz.dao.biz.bean.GpsInfo;
import com.ldz.dao.biz.bean.SendGpsEvent;
import com.ldz.dao.biz.bean.WebsocketInfo;
import com.ldz.dao.biz.constant.DeviceStatus;
import com.ldz.dao.biz.constant.EventType;
import com.ldz.dao.biz.mapper.*;
import com.ldz.dao.biz.model.*;
import com.ldz.dao.obd.model.GpsObdMessageBean;
import com.ldz.service.biz.interfaces.*;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.sys.model.SysYh;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.bean.TrackPoint;
import com.ldz.util.bean.YingyanResponse;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.util.gps.DistanceUtil;
import com.ldz.util.gps.Gps;
import com.ldz.util.gps.PositionUtil;
import com.ldz.util.redis.RedisTemplateUtil;
import com.ldz.util.yingyan.GuiJIApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.common.Mapper;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GpsServiceImpl extends BaseServiceImpl<ClGps, String> implements GpsService {

	Logger errorLog = LoggerFactory.getLogger("error_info");
    @Autowired
    private ClGpsMapper entityMapper;
    @Autowired
    private RedisTemplateUtil redis;
    @Autowired
    private ClClMapper clclmapper;
    @Autowired
    private ClService clService;
    @Autowired
    private ZdglService zdglservice;
    @Autowired
    private PbService pbService;
    @Autowired
    private ClyxjlService clyxjlService;
    @Autowired
    private ClZdglMapper zdglMapper;
    @Autowired
    private ZdYhMapper zdYhMapper;
    @Autowired
    private SimpMessagingTemplate websocket;
    @Autowired
    private SbyxsjjlService sbyxsjjlService;
    @Autowired
    private GpsLsService gpsLsService;
    @Autowired
    private ClGpsLsMapper clGpsLsMapper;
    AsyncEventBus eventBus = new AsyncEventBus(Executors.newFixedThreadPool(1));

    private ExecutorService singlePool = Executors.newSingleThreadExecutor();

    public GpsServiceImpl() {
        eventBus.register(this);
    }

    @Override
    protected Mapper<ClGps> getBaseMapper() {
        return entityMapper;
    }

    /**
     *
     * 如果eventType == null 是心跳包
     * 有eventType 记录事件表（此类型数据不多，可直接插入数据库）
     * 收到eventType == 80 表示离线
     * 如果终端有电子围栏，则判断是否在电子围栏之内，如果不在电子围栏，则记录异常事件
     * 判断与上一个点的距离，如果距离过近，则不记录gps
     *
     * websocket推送
     * 写轨迹段
     * @param gpsInfo
     * @return
     */
    @Override
    public ApiResponse<String> onReceiveGps(GpsInfo gpsInfo) {
        ClZdgl zd = zdglservice.findById(gpsInfo.getDeviceId());
        if(zd == null){
            return ApiResponse.fail("终端不存在");
        }
//        errorLog.error("gpsInfo:{}",JsonUtil.toJson(gpsInfo));
        // 只要上传点位信息 且不为离线事件 则为在线状态
        // 过期时间
        String  expireTime = (String)redis.boundValueOps("expire_" + gpsInfo.getDeviceId()).get();
        String nowTime = DateUtils.getDateStr(new Date(), "yyyy-MM-dd");
        // 首先查询该设备是否有有效期
        if(StringUtils.isNotBlank(expireTime)){

            if(StringUtils.equals(zd.getJhzt(), "10")){
                if(StringUtils.isNotBlank(zd.getFwnx())){
                    if(StringUtils.equals(zd.getFwnx(),"#")){
                        // 永久激活
                        zd.setZdKssj(DateUtils.getDateStr(new Date(),"yyyy-MM-dd"));
                        zd.setZdJssj("#");
                    }else{
                        zd.setZdKssj(DateUtils.getDateStr(new Date(), "yyyy-MM-dd"));
                        zd.setZdJssj(DateUtils.plusYear(LocalDateTime.now(),Integer.parseInt(zd.getFwnx())));
                    }

                }else{
                    return ApiResponse.fail("上传终端没有设置服务年限");
                }
                zd.setJhzt("20");
                expireTime = zd.getZdJssj();
                zdglservice.update(zd);
                redis.boundValueOps("expire_" + gpsInfo.getDeviceId()).set(zd.getZdJssj());
            }
            if (!expireTime.equals("#") && nowTime.compareTo(expireTime) > 0 ) {
                return ApiResponse.fail("上传终端不在有效期内");
            }

        }else{
            if(!ObjectUtils.isEmpty(zd)){
                if(StringUtils.equals(zd.getJhzt(),"30")){
                    redis.boundValueOps("expire_" + gpsInfo.getDeviceId()).set("0");
                    return ApiResponse.fail(zd.getZdbh()+"-已到期");
                }
                // 激活设备
                if(StringUtils.equals(zd.getJhzt(),"10")){
                    if(StringUtils.isNotBlank(zd.getFwnx())){
                        if(StringUtils.equals(zd.getFwnx(),"#")){
                            // 永久激活
                            zd.setZdKssj(DateUtils.getDateStr(new Date(),"yyyy-MM-dd"));
                            zd.setZdJssj("#");
                        }else{
                            zd.setZdKssj(DateUtils.getDateStr(new Date(), "yyyy-MM-dd"));
                            zd.setZdJssj(DateUtils.plusYear(LocalDateTime.now(),Integer.parseInt(zd.getFwnx())));
                        }

                    }else{
                        return ApiResponse.fail("上传终端没有设置服务年限");
                    }
                    zd.setJhzt("20");
                    zdglservice.update(zd);
                }
                redis.boundValueOps("expire_" + gpsInfo.getDeviceId()).set(zd.getZdJssj());
                if (!zd.getZdJssj().equals("#") && nowTime.compareTo(zd.getZdJssj()) > 0 ) {
                    return ApiResponse.fail("上传终端不在有效期内");
                }
            }
        }
        if(!StringUtils.equals(gpsInfo.getEventType(),"80")) {

            if (zd.getZdLx().equals("10")){
                redis.boundValueOps("offline-" + gpsInfo.getDeviceId()).set(1, 10, TimeUnit.MINUTES);
            }else if (zd.getZdLx().equals("20")){
                redis.boundValueOps("obd-offline-" + gpsInfo.getDeviceId()).set(1, 10, TimeUnit.MINUTES);
            }else if (zd.getZdLx().equals("30")){
                redis.boundValueOps("dwq-offline-" + gpsInfo.getDeviceId()).set(1, 10, TimeUnit.MINUTES);
            }
        }
        if (StringUtils.isEmpty(gpsInfo.getLatitude()) || StringUtils.isEmpty(gpsInfo.getLongitude())
                || StringUtils.isEmpty(gpsInfo.getDeviceId())) {
            return ApiResponse.fail("上传的数据中经度,纬度,或者终端编号为空");
        }

        //如果接收的速度值超过每小时180公里，认为无效进行忽略
        if (gpsInfo.getSpeed() != null && gpsInfo.getSpeed().intValue() >= 150){
            errorLog.error("["+gpsInfo.getDeviceId()+"]速度值异常:"+gpsInfo.getSpeed()+"KM/H");
            return ApiResponse.success();
        }



        //2018年11月23日。将行程计算提前到GPS点存储之前
//        clXc(gpsInfo);

        boolean change = false;
        try{
            change = handleEvent(gpsInfo);
        }catch(Exception e){
        	errorLog.error("解析GPS事件异常", e);
        }
		saveVersionInfoToRedis(gpsInfo);
        if (!change) return ApiResponse.success();

        if (!gpsInfo.getLatitude().equals("-1") && !gpsInfo.getLongitude().equals("-1")) {
            // 只有已录入的设备才上传到鹰眼
            if (isDeviceExist(gpsInfo.getDeviceId())){
                eventBus.post(new SendGpsEvent(gpsInfo));
            }else{
                log.info("终端编号不存在，不上传鹰眼-"+gpsInfo.getDeviceId());
            }
        }

        return ApiResponse.success();
    }

    @Override
    public ApiResponse<String> onReceiveGpsList(List<GpsInfo> gpsInfo) {
        for (GpsInfo info : gpsInfo) {
            onReceiveGps(info);
        }
        return ApiResponse.success();
    }

    private boolean isDeviceExist(String deviceId){
        long c = zdglservice.countEq(ClZdgl.InnerColumn.zdbh.name(),deviceId);
        return c > 0;
    }


    private boolean handleEvent(GpsInfo gpsInfo){
        String eventType = gpsInfo.getEventType();
        String deviceId = gpsInfo.getDeviceId();

        String newStatus = "";

        boolean change = false;
        boolean statusChange = false;
        boolean positionChange = false;

        if (StringUtils.isNotEmpty(eventType)) {
            EventType type = EventType.toEmun(eventType);
            if (type == null){
                log.error("未知事件类型："+eventType);
                return false;
            }
            switch (type){
                case IGNITION:
                    newStatus = DeviceStatus.IGNITION.getCode();
                    break;
                case FLAMEOUT:
                    newStatus = DeviceStatus.FLAMEOUT.getCode();
                    break;
                case OFFLINE:
                    newStatus = DeviceStatus.OFFLINE.getCode();
                    break;
                default:
            }
        }

        ClGps newGps = changeCoordinates(gpsInfo);

        String sczt = gpsInfo.getSczt();
        if ("20".equals(sczt)){
            newStatus = DeviceStatus.FLAMEOUT.getCode();
        }else if ("10".equals(sczt)){
            newStatus = DeviceStatus.IGNITION.getCode();
        }
        String gpsJson = (String) redis.boundValueOps(ClGps.class.getSimpleName() + deviceId).get();
        if (StringUtils.isEmpty(gpsJson)){
            statusChange = true;
        }else{
            ClGps gps = JsonUtil.toBean(gpsJson, ClGps.class);
            if (gps == null){
                statusChange = true;
            }else{
                if (!newStatus.equals(gps.getStatus())){
                    statusChange = true;
                }else if (gps.getBdwd() == null || gps.getBdjd() == null){
                    statusChange = true;
                }else{
                    if (newGps != null && newGps.getJd() != null && newGps.getWd() != null){
                        // 比较redis(实时gps点位)历史数据和这次接收到的数据距离
                        double shortDistance = DistanceUtil.getShortDistance(gps.getBdwd().doubleValue(),
                                gps.getBdjd().doubleValue(), newGps.getBdwd().doubleValue(), newGps.getBdjd().doubleValue());
                        positionChange = shortDistance > 10;
                    }
                }
            }
        }

        ClCl car = null;
        List<ClCl> carList = clService.findEq(ClCl.InnerColumn.zdbh,gpsInfo.getDeviceId());
        if (carList.size() != 0) {
            car = carList.get(0);
        }
        if (statusChange || positionChange){
            change = true;
            ClGpsLs gpsls = new ClGpsLs(newGps);
            newGps.setStatus(newStatus);
            gpsls.setId(genId());
            gpsls.setZdbh(deviceId);
            redis.boundListOps(ClGpsLs.class.getSimpleName() + deviceId).leftPush(JsonUtil.toJson(gpsls));
            // 更新存入redis(实时点位)
            redis.boundValueOps(ClGps.class.getSimpleName() + deviceId).set(JsonUtil.toJson(newGps));
            singlePool.submit(new XcThread(gpsInfo));
//            clXc(gpsInfo);
            String xlId = "";
            if (car != null){
                SimpleCondition condition = new SimpleCondition(ClPb.class);
                condition.eq(ClPb.InnerColumn.clId,car.getClId());
                List<ClPb> pbList = pbService.findByCondition(condition);
                if (pbList.size() != 0){
                    ClPb pb = pbList.get(0);
                    if(pb != null) {
                        xlId = pb.getXlId();
                    }
                }
            }
            if (newGps.getBdjd() == null){
            	newGps.setBdjd(new BigDecimal(-1));
            }
            if (newGps.getBdwd() == null ){
            	newGps.setBdwd(new BigDecimal(-1));
            }
            WebsocketInfo websocketInfo = changeSocketNew(gpsInfo, newGps, xlId);
            if (websocketInfo != null){
                redis.boundValueOps(WebsocketInfo.class.getSimpleName() + deviceId).set(JsonUtil.toJson(websocketInfo));
                sendWebsocket(websocketInfo);
            }
        }
        // clXc(gpsInfo);
        saveClSbyxsjjl(gpsInfo, newGps, car);
        return change;
    }

    /**
     * 如果在线状态发生改变，
     * 或者位置发生改变，
     * 就发送websocket到前台
     * @param websocketInfo
     */
    private void sendWebsocket(WebsocketInfo websocketInfo){
        errorLog.error("sendWebsocket websocketInfo:"+websocketInfo.toString());
        String socket = JsonUtil.toJson(websocketInfo);
        log.info("推送前端的数据为" + socket);
        websocket.convertAndSend("/topic/sendgps-" + websocketInfo.getZdbh(), socket);
    }


    private void saveVersionInfoToRedis(GpsInfo gpsInfo) {
        if (StringUtils.isEmpty(gpsInfo.getCmdParams()) || !gpsInfo.getCmdParams().contains("versionCode")) {
            return;
        }
        Map<String, Object> map = JsonUtil.toMap(gpsInfo.getCmdParams());
        if (map == null) {
            return;
        }
        if (!map.containsKey("versionCode") || !map.containsKey("versionName")) {
            return;
        }
        // 根据设备号查找
        ClZdgl clZdgl = zdglservice.findById(gpsInfo.getDeviceId());
        String versionCode = map.get("versionCode").toString();
        String versionName = map.get("versionName").toString();
        if(StringUtils.isBlank(clZdgl.getVersion()) || !StringUtils.equals(versionCode + "-" + versionName,clZdgl.getVersion())){
            clZdgl.setVersion(versionCode + "-" + versionName);
            zdglMapper.updateByPrimaryKeySelective(clZdgl);
        }
        redis.boundValueOps("versionInfo-" + gpsInfo.getDeviceId()).set(versionCode + "-" + versionName);
    }

    @Subscribe
    public void sendGps(SendGpsEvent event) {
        ClGps entity = changeCoordinates(event.getGpsInfo());
        ClGpsLs gpsls = new ClGpsLs(genId(), entity.getZdbh(), entity.getCjsj(), entity.getJd(), entity.getWd(),
                entity.getGgjd(), entity.getGgwd(), entity.getBdjd(), entity.getBdwd(), entity.getGdjd(), entity.getGdwd(),
                entity.getLx(), entity.getDwjd(), entity.getFxj(), entity.getYxsd(),entity.getStartNum());
        YingyanResponse addPoints = GuiJIApi.addPoint(changeModel(gpsls), GuiJIApi.addPointURL);
        if (addPoints.getStatus().equals("0")){
            log.info(entity.getZdbh()+"-"+addPoints.toString());
        }else{
            log.info(entity.getZdbh()+"-addFailed-"+addPoints.toString());
        }
    }


    public TrackPoint changeModel(ClGpsLs clgps) {
        TrackPoint tracktPoint = new TrackPoint();
        tracktPoint.set_object_key(clgps.getYxsd());
        tracktPoint.setAk(GuiJIApi.AK);
        tracktPoint.setCoord_type_input("bd09ll");
        tracktPoint.setEntity_name(clgps.getZdbh());
        tracktPoint.setLatitude(clgps.getBdwd());
        tracktPoint.setLoc_time((clgps.getCjsj().getTime()) / 1000);
        tracktPoint.setLongitude(clgps.getBdjd());
        tracktPoint.setService_id(GuiJIApi.SERVICE_ID);
        tracktPoint.setSpeed(Double.valueOf(clgps.getYxsd()));
        tracktPoint.setDirection((int) Math.ceil(clgps.getFxj().doubleValue()));
        return tracktPoint;
    }


    @Override
    public ClDzwl JudgePoint(ClGps gps, ClCl clcl) {


        List<ClDzwl> seleByZdbh = clcl.getClDzwl();

        if (CollectionUtils.isEmpty(seleByZdbh)) {
            log.info("该终端暂未设置电子围栏");
            return null;
        }

        for (ClDzwl CL : seleByZdbh) {
            String latlngs = CL.getDlxxzb();
            String[] arrays = latlngs.split(";");
            List<Gps> areas = new ArrayList<Gps>();
            for (int i = 0; i < arrays.length; i++) {
                areas.add(new Gps(Double.parseDouble(arrays[i].split(",")[0]),
                        Double.parseDouble(arrays[i].split(",")[1])));
            }
            ArrayList<Double> polygonXA = new ArrayList<Double>();
            ArrayList<Double> polygonYA = new ArrayList<Double>();
            for (int i = 0; i < areas.size(); i++) {
                Gps area = areas.get(i);
                polygonXA.add(area.getWgLon());
                polygonYA.add(area.getWgLat());
            }
            // 判断位置点是否在电子围栏内
            Boolean flag = PositionUtil.isPointInPolygon(gps.getBdwd().doubleValue(),
                    gps.getBdjd().doubleValue(), polygonXA, polygonYA);

            if (flag == false) {

                return CL;
            }
        }

        return null;
    }

    @Override
    public ClGps changeCoordinates(GpsInfo entity) {
        ClGps clGps = new ClGps();
        clGps.setStartNum(entity.getStarNum());
        if (entity.getLatitude() != null) {
            if ("-1".equals(entity.getLatitude())) return clGps;
            clGps.setWd(new BigDecimal(entity.getLatitude()));
        }
        if (entity.getLongitude() != null) {
            if ("-1".equals(entity.getLongitude())) return clGps;
            clGps.setJd(new BigDecimal(entity.getLongitude()));
        }
        // 设备记录时间
        if (StringUtils.isNotEmpty(entity.getStartTime())) {
            clGps.setCjsj(simpledate(entity.getStartTime()));
        }
        if (entity.getGpsjd() != null && entity.getGpsjd().length() <= 3) {
            String jd = entity.getGpsjd();
            if (jd.contains(".")){
                jd = jd.substring(0,jd.indexOf("."));
            }
            clGps.setDwjd(Short.valueOf(jd));
        }
        if (entity.getFxj() != null) {
            clGps.setFxj(new BigDecimal(entity.getFxj()));
        }
        clGps.setZdbh(entity.getDeviceId());
        if (entity.getSpeed() != null) {
            clGps.setYxsd(String.valueOf(entity.getSpeed()));
        }
        // 将收到的gps转换成火星坐标系(谷歌)
        Gps gps84_To_Gcj02 = PositionUtil.gps84_To_Gcj02(clGps.getWd().doubleValue(), clGps.getJd().doubleValue());
        if (gps84_To_Gcj02 == null){
            log.error("outOfChina:",entity.toString());
            return clGps;
        }

        // 将火星系坐标转换成百度坐标
        Gps gcj02_To_Bd09 = PositionUtil.gcj02_To_Bd09(gps84_To_Gcj02.getWgLat(), gps84_To_Gcj02.getWgLon());
        // 保存gps对象
        clGps.setBdjd(new BigDecimal(gcj02_To_Bd09.getWgLon()));
        clGps.setBdwd(new BigDecimal(gcj02_To_Bd09.getWgLat()));

        clGps.setGgjd(new BigDecimal(gps84_To_Gcj02.getWgLon()));
        clGps.setGgwd(new BigDecimal(gps84_To_Gcj02.getWgLat()));

        clGps.setGdjd(clGps.getGgjd());
        clGps.setGdwd(clGps.getGgwd());
        return clGps;
    }

    @Override
    public ClSbyxsjjl saveClSbyxsjjl(GpsInfo entity, ClGps clgps, ClCl clcl) {
        // 封装设备事件记录表表
        ClSbyxsjjl clsbyxsjjl = new ClSbyxsjjl();
        clsbyxsjjl.setJd(clgps.getBdjd());
        clsbyxsjjl.setWd(clgps.getBdwd());
        if (clcl != null){
            clsbyxsjjl.setCph(clcl.getCph());
            clsbyxsjjl.setCx(clcl.getCx());
            if (StringUtils.isNotEmpty(clcl.getSjxm())) {
                clsbyxsjjl.setSjxm(clcl.getSjxm());
            }
        }else{  // 设备没有绑定车辆时，默认保存终端信息，后续绑定车辆后可根据终端信息替换
            clsbyxsjjl.setCph(entity.getDeviceId());
        }
        // 获取设备的记录时间
        if (StringUtils.isNotEmpty(entity.getStartTime())) {
            clsbyxsjjl.setCjsj(simpledate(entity.getStartTime()));
        }

        if (StringUtils.isNotEmpty(entity.getGpsjd())) {
            clsbyxsjjl.setJid(new BigDecimal(entity.getGpsjd()));
        }
        if (StringUtils.isNotEmpty(entity.getFxj())) {
            clsbyxsjjl.setYxfx(new Double(entity.getFxj()));
        }
        clsbyxsjjl.setZdbh(entity.getDeviceId());
        clsbyxsjjl.setSjjb("20");
        // 封装设备终端管理
        ClZdgl zdgl = zdglservice.findById(entity.getDeviceId());
        if (zdgl != null){
            // 根据传入的sczt判断终段在线状态
            if (StringUtils.equals(entity.getSczt(), "10")) {
                if(!StringUtils.equals(zdgl.getZt(),"00") || !StringUtils.equals(zdgl.getZxzt(),"00")){
                    zdgl.setZt("00");
                    zdgl.setZxzt("00");
                    zdglservice.update(zdgl);
                }
            }else if (StringUtils.equals(entity.getSczt(), "20")) {
                if(!StringUtils.equals(zdgl.getZt(),"00") || !StringUtils.equals(zdgl.getZxzt(),"10")) {
                    zdgl.setZt("00");
                    zdgl.setZxzt("10");
                    zdglservice.update(zdgl);
                }
            }
        }


        // 判断该点位是否在电子围栏里面
        if (clcl != null){
            ClDzwl judgePoint = JudgePoint(clgps, clcl);
            if (judgePoint != null) {
                clsbyxsjjl.setId(genId());
                clsbyxsjjl.setSjlx("70");
                clsbyxsjjl.setBz(judgePoint.getId());
                redis.boundListOps(ClSbyxsjjl.class.getSimpleName()).leftPush(JsonUtil.toJson(clsbyxsjjl));
                log.info("该点位不在电子围栏里面,事件表存储成功");
            }
        }

        // 没有携带事件类型
        if (StringUtils.isEmpty(entity.getEventType())) {
            return null;
        }


        // 事件类型为点火
        if (StringUtils.equals(entity.getEventType(), "50") || StringUtils.equals(entity.getEventType(), "60") ) {
            clsbyxsjjl.setSjjb("10");
        }


        // 其余异常类型
        clsbyxsjjl.setSjlx(entity.getEventType());
        clsbyxsjjl.setId(genId());
        // 判断此事件是否重复上传  30 内
        // 首先查询 redis 中的 终端事件上一次的时间
        String time = (String)redis.boundValueOps(entity.getDeviceId() + "sjlx_" + entity.getEventType()).get();
        if(StringUtils.isBlank(time)){
            redis.boundValueOps(entity.getDeviceId() + "sjlx_" + entity.getEventType()).set(entity.getStartTime());
            log.info("clsbyxsjjl.save");
            redis.boundListOps(ClSbyxsjjl.class.getSimpleName()).leftPush(JsonUtil.toJson(clsbyxsjjl));
        }else {
            // 最近时间 与 当前事件的时间比较 若大于30s 再存储
                Date recentTime = simpledate(time);
                if(clsbyxsjjl.getCjsj().getTime() - recentTime.getTime() > 30 * 1000){
                    log.info("clsbyxsjjl.save");
                    redis.boundListOps(ClSbyxsjjl.class.getSimpleName()).leftPush(JsonUtil.toJson(clsbyxsjjl));
                    // 更新最新时间
                    redis.boundValueOps(entity.getDeviceId() + "sjlx_" + entity.getEventType()).set(entity.getStartTime());
                }
        }
        return clsbyxsjjl;
    }

    @Override
    public WebsocketInfo changeSocketNew(GpsInfo gpsinfo, ClGps clpgs, String xlId) {
        errorLog.error("changeSocketNew gpsinfo:"+gpsinfo.toString());
        ClZdgl zdgl = zdglservice.findById(gpsinfo.getDeviceId());
        List<ClCl> cars = clService.findEq(ClCl.InnerColumn.zdbh,gpsinfo.getDeviceId());
        if (cars.size() == 0){
            return null;
        }

        ClCl seleByZdbh = cars.get(0);
        // 通过终端id获取车辆信息
        WebsocketInfo info = new WebsocketInfo();
        if(zdgl != null ){
            info.setZdLx(zdgl.getZdLx());
        }
        if (clpgs != null) {
            info.setSpeed(clpgs.getYxsd());
            info.setFxj(clpgs.getFxj());
            if (StringUtils.isNotEmpty(gpsinfo.getSczt())) {
                if (StringUtils.equals(gpsinfo.getSczt(), "10")) {
                    if (StringUtils.isNotEmpty(gpsinfo.getEventType())) {
                        info.setEventType(gpsinfo.getEventType());
                    }
                    info.setZxzt("00");
                    info.setBdjd(clpgs.getBdjd().toString());
                    info.setBdwd(clpgs.getBdwd().toString());
                    info.setTime(simpledate(gpsinfo.getStartTime()));
                }
                if (StringUtils.equals(gpsinfo.getSczt(), "20")) {
                    info.setZxzt("10");
                    if (StringUtils.isNotEmpty(gpsinfo.getEventType())) {
                        info.setEventType(gpsinfo.getEventType());
                    }
                    info.setBdjd(clpgs.getBdjd().toString());
                    info.setBdwd(clpgs.getBdwd().toString());
                    info.setTime(simpledate(gpsinfo.getStartTime()));
                }
            }
            if (StringUtils.isNotEmpty(gpsinfo.getEventType())) {
                if (StringUtils.equals(gpsinfo.getEventType(), EventType.OFFLINE.getCode())) {
                    info.setZxzt("20");
                    info.setBdjd(clpgs.getBdjd().toString());
                    info.setBdwd(clpgs.getBdwd().toString());
                    info.setTime(clpgs.getCjsj());
                }
            }
        }
        if(seleByZdbh !=null) {
            info.setXlId(xlId);
            info.setClid(seleByZdbh.getClId());
            info.setCph(seleByZdbh.getCph());
            info.setZdbh(seleByZdbh.getZdbh());
            info.setSjxm(seleByZdbh.getSjxm());
            info.setCx(seleByZdbh.getCx());
            info.setSjxm(seleByZdbh.getSjxm());
        }
        Date today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        SimpleCondition simpleCondition = new SimpleCondition(ClClyxjl.class);
        simpleCondition.eq(ClClyxjl.InnerColumn.zdbh,gpsinfo.getDeviceId());
        simpleCondition.gte(ClClyxjl.InnerColumn.cjsj,today);
        List<ClClyxjl> clyxjls = clyxjlService.findByCondition(simpleCondition);
        if (clyxjls.size() != 0){
            ClClyxjl clyxjl = clyxjls.get(0);
            info.setStationNumber(clyxjl.getZdbh());
        }
        if (!ObjectUtils.isEmpty(seleByZdbh) && StringUtils.isNotEmpty(seleByZdbh.getObdCode())) {
            info.setObdId(seleByZdbh.getObdCode());
        }


        Set<Object> keys = redis.keys("CX," + gpsinfo.getDeviceId()+"*");
        //查询上一次记录的行程
        if (keys.size() > 0){
            String tmpKey = keys.iterator().next().toString();
            String prevTime = tmpKey.split(",")[2];
            DateTime startTime = DateTime.parse(prevTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
            Date now = new Date();
            long duration = now.getTime() - startTime.toDate().getTime();
            info.setDurartion(duration);
        }
        return info;
    }

    @Override
    public ApiResponse<List<WebsocketInfo>> inintGps() {
        ApiResponse<List<WebsocketInfo>> apiResponse = new ApiResponse<>();
        List<WebsocketInfo> list = new ArrayList<>();

        SimpleCondition carCondition = new SimpleCondition(ClCl.class);
        SimpleCondition deviceCondition = new SimpleCondition(ClZdgl.class);

        String cphLike = getRequestParamterAsString("cphLike");
        if (StringUtils.isNotEmpty(cphLike)) {
            carCondition.like(ClCl.InnerColumn.cph, cphLike);
        }
        HttpServletRequest request = getRequset();
        List<String> filterDeviceIds = null;
        if (request.getAttribute("appUserId") != null){// app用户
            String appUserId = (String) request.getAttribute("appUserId");
            SimpleCondition condition1 = new SimpleCondition(ClZdYh.class);
            condition1.eq(ClZdYh.InnerColumn.userId,appUserId);
            List<ClZdYh> zdYhs = zdYhMapper.selectByExample(condition1);
            if (zdYhs.size() == 0){
                return apiResponse;
            }
            filterDeviceIds = zdYhs.stream().map(ClZdYh::getDeviceId).collect(Collectors.toList());
            carCondition.in(ClCl.InnerColumn.zdbh,filterDeviceIds);
        }else{ // pc端用户
            SysYh currentUser = getCurrentUser();
            String jgdmLike = currentUser.getJgdm()+"%";
            carCondition.and().andLike(ClCl.InnerColumn.jgdm.name(),jgdmLike);
            deviceCondition.and().andLike(ClZdgl.InnerColumn.jgdm.name(),jgdmLike);
        }

        // 将终端编号,车辆信息缓存
        List<ClCl> carList = clclmapper.selectByExample(carCondition);
        Map<String, ClCl> zdbhClMap = carList.stream().filter(s -> StringUtils.isNotEmpty(s.getZdbh()))
                .collect(Collectors.toMap(ClCl::getZdbh, p->p));

        if (StringUtils.isNotEmpty(cphLike)) {
            List<String> zdbhs = carList.stream().map(ClCl::getZdbh).collect(Collectors.toList());
            if (filterDeviceIds == null){
                filterDeviceIds = zdbhs;
            }else{
                filterDeviceIds.retainAll(zdbhs);
            }
        }
        if (filterDeviceIds != null){
            deviceCondition.in(ClZdgl.InnerColumn.zdbh,filterDeviceIds);
        }

        // 获取终端状态
//        condition.startWith(ClZdgl.InnerColumn.jgdm,currentUser.getJgdm());
        String zdLx = getRequestParamterAsString("zdLx");
        if(StringUtils.isNotBlank(zdLx)){
            deviceCondition.eq(ClZdgl.InnerColumn.zdLx,zdLx);
        }
        String type = getRequestParamterAsString("type");
        if (StringUtils.isNotEmpty(type)){
            deviceCondition.startWith(ClZdgl.InnerColumn.zdbh,type);
        }
        List<ClZdgl> deviceList = zdglservice.findByCondition(deviceCondition);
        if (deviceList.size() == 0){
            apiResponse.setResult(new ArrayList<>());
            return apiResponse;
        }

        // 获取实时点位gps信息
        List<String> deviceIds = deviceList.stream().map(ClZdgl::getZdbh).collect(Collectors.toList());
        SimpleCondition gpsCondition = new SimpleCondition(ClGps.class);
        gpsCondition.in(ClGps.InnerColumn.zdbh,deviceIds);
        List<ClGps> gpsList = entityMapper.selectByExample(gpsCondition);
        Map<String,ClGps> gpsMap = gpsList.stream().collect(Collectors.toMap(ClGps::getZdbh,p->p));

        for (ClZdgl device : deviceList) {
            // 如果redis中有websocketInfo，则优先取redis中的数据
            String key = WebsocketInfo.class.getSimpleName() + device.getZdbh();
            Iterator<Object> keys = redis.keys(key).iterator();

            //行程Redis key
            String clxcKey = "CX," + device.getZdbh();
            //上一次的GPS点时间
            Date t = null;
            try{
                Set<Object> keyXc = redis.keys(clxcKey+"*");
                //查询上一次记录的行程
                if (keyXc.size() > 0){
                    String tmpKey = keyXc.iterator().next().toString();
                    t = DateTime.parse(tmpKey.split(",")[2], DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
                }
            }catch(Exception e){
                ClSbyxsjjl online = sbyxsjjlService.getOneForOnline(device.getZdbh());
                t = online.getCjsj();

                errorLog.error("redis.keys异常:",e);
            }

            if (keys.hasNext()){

                String json = (String) redis.boundValueOps(key).get();
                WebsocketInfo websocketInfo = JsonUtil.toBean(json, WebsocketInfo.class);

                /*if(t != null){
                    *//*SimpleCondition condition1 = new SimpleCondition(ClGpsLs.class);
                    condition1.eq(ClGpsLs.InnerColumn.zdbh , device.getZdbh());
                    condition1.gte(ClGpsLs.InnerColumn.cjsj , t);
                    condition1.setOrderByClause(" cjsj asc");*//*
                    // 从最近的点火时间 到现在
                    List<Map<String,BigDecimal>> ls = gpsLsService.getJdAndWd(device.getZdbh(),t);
                    websocketInfo.setGpsList(ls);
                }*/
                list.add(websocketInfo);
                continue;
            }

            WebsocketInfo websocketInfo = new WebsocketInfo();
            websocketInfo.setZdLx(device.getZdLx());
            websocketInfo.setZdbh(device.getZdbh());
            ClGps gps = gpsMap.get(device.getZdbh());


            /*if(t != null){
               *//* SimpleCondition condition1 = new SimpleCondition(ClGpsLs.class);
                condition1.eq(ClGpsLs.InnerColumn.zdbh , device.getZdbh());
                condition1.gte(ClGpsLs.InnerColumn.cjsj , t);
                condition1.setOrderByClause(" cjsj asc");*//*
                // 从最近的点火时间 到现在
                List<Map<String,BigDecimal>> ls = gpsLsService.getJdAndWd(device.getZdbh(),t);
                websocketInfo.setGpsList(ls);
            }*/

            if (gps != null){
                websocketInfo.setBdjd(gps.getBdjd().toString());
                websocketInfo.setBdwd(gps.getBdwd().toString());
                websocketInfo.setGdjd(gps.getGgjd().toString());
                websocketInfo.setGdwd(gps.getGgwd().toString());
                websocketInfo.setFxj(gps.getFxj());
                websocketInfo.setTime(gps.getCjsj());
                websocketInfo.setSpeed(gps.getYxsd());

                Set<Object> keysCx = redis.keys("CX," + gps.getZdbh()+"*");
                //查询上一次记录的行程
                if (keysCx.size() > 0){
                    String tmpKey = keysCx.iterator().next().toString();
                    String prevTime = tmpKey.split(",")[2];
                    DateTime startTime = DateTime.parse(prevTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
                    Date now = new Date();
                    long duration = now.getTime() - startTime.toDate().getTime();
                    websocketInfo.setDurartion(duration);
                }
            }else{
                /**
                 * 谷歌地图：39.9088596409,116.3975157338
                 * 百度地图：39.9152108931,116.4039006839
                 * 腾讯高德：39.9088230000,116.3974700000
                 * 图吧地图：39.9082647400,116.3867222800
                 * 谷歌地球：39.9074647400,116.3912822800
                 * 北纬N39°54′26.87″ 东经E116°23′28.62″
                 *
                 * 靠近：中国北京市北京市东城区
                 */
                websocketInfo.setDefaultGps(true);
                websocketInfo.setBdjd("116.4039006839");
                websocketInfo.setBdwd("39.9152108931");
                websocketInfo.setGdjd("116.3974700000");
                websocketInfo.setGdwd("39.9088230000");
                websocketInfo.setFxj(new BigDecimal(0));
                websocketInfo.setTime(new Date());
                websocketInfo.setSpeed("0");
                websocketInfo.setDurartion(0L);
            }

            ClCl car = zdbhClMap.get(device.getZdbh());
            if (car != null){
                websocketInfo.setClid(car.getClId());
                websocketInfo.setCph(car.getCph());
                websocketInfo.setCx(car.getCx());
                websocketInfo.setSjxm(car.getSjxm());
                if (StringUtils.isNotEmpty(car.getObdCode())) {
                    websocketInfo.setObdId(car.getObdCode());
                }
            }

            if (StringUtils.equals(device.getZxzt(), "20")) {
                websocketInfo.setZxzt("10");
                if (gps != null){
                    websocketInfo.setLxsc(nowTime(gps.getCjsj()));
                }
            }else {
                websocketInfo.setZxzt(device.getZxzt());
            }
            String obdInfo = (String) redis.boundValueOps("obdInfo_" + device.getZdbh()).get();
            if(StringUtils.isNotBlank(obdInfo)) {
                GpsObdMessageBean gpsObdMessageBean = JsonUtil.toBean(obdInfo, GpsObdMessageBean.class);
                device.setObdInfo(gpsObdMessageBean);
            }
            list.add(websocketInfo);
        }

        apiResponse.setResult(list);
        return apiResponse;
    }

    @Override
    public ApiResponse<List<String>> getWebsocketInfo(String deviceIds) {
        RuntimeCheck.ifBlank(deviceIds,"请选择终端");
        String[] deviceIdArray = deviceIds.split(",");
        RuntimeCheck.ifEmpty(deviceIdArray,"请选择终端");

        LimitedCondition condition = new LimitedCondition(ClZdgl.class);
        List<ClZdgl> deviceList = zdglMapper.selectByExample(condition);

        List<String> resList = new ArrayList<>(deviceIdArray.length);
        if (deviceList.size() == 0) ApiResponse.success(resList);

        List<String> deviceIdList = deviceList.stream().map(ClZdgl::getZdbh).collect(Collectors.toList());
        Map<String, ClZdgl> collect = deviceList.stream().collect(Collectors.toMap(ClZdgl::getZdbh, p -> p));
        for (String deviceId : deviceIdArray) {
            if (!deviceIdList.contains(deviceId)) continue;

            String key = WebsocketInfo.class.getSimpleName() + deviceId;
            Iterator<Object> keys = redis.keys(key).iterator();
            if (!keys.hasNext()){
                continue;
            }
            String json = (String) redis.boundValueOps(key).get();
            WebsocketInfo info = JsonUtil.toBean(json, WebsocketInfo.class);
            /*ClZdgl clZdgl = collect.get(deviceId);
            List<Map<String,BigDecimal>> list = new ArrayList<>();
            List<ClGpsLs> list1 = new ArrayList<>();
            if(StringUtils.equals(clZdgl.getZxzt(), "00")){


                String gpsKey =  ClGpsLs.class.getSimpleName() + deviceId;

                BoundListOperations<Object, Object> boundListOps = redis.boundListOps(gpsKey);
                String index = (String) boundListOps.index(0);
                if (StringUtils.isNotEmpty(index)) {
                    Long length = boundListOps.size();
                    for (int i = 0; i < length; i++) {
                        String clgpsls = (String) boundListOps.rightPop();
                        ClGpsLs gpssss = JsonUtil.toBean(clgpsls, ClGpsLs.class);
                        list1.add(gpssss);
                        Map<String,BigDecimal> map = new HashMap<>();
                        map.put("bdjd",gpssss.getBdjd());
                        map.put("bdwd",gpssss.getBdwd());
                        list.add(map);
                    }
                }
                if(CollectionUtils.isNotEmpty(list)){
                    errorLog.info("getWebSocketInfo gps 点 ： " + JsonUtil.toJson(list1));
                    clGpsLsMapper.insertList(list1);
                }
            }
            errorLog.info("getWebSocketInfo gps 点  为空 " );
            info.setGpsList(list);*/
            json = JsonUtil.toJson(info);
            resList.add(json);
        }
        return ApiResponse.success(resList);
    }

    @Override
    public ApiResponse<List<WebsocketInfo>> getWebsocketInfoToApp(String deviceIds) {
        RuntimeCheck.ifBlank(deviceIds,"请选择终端");
        String[] deviceIdArray = deviceIds.split(",");
        RuntimeCheck.ifEmpty(deviceIdArray,"请选择终端");

        List<WebsocketInfo> resList = new ArrayList<>(deviceIdArray.length);
        for (String deviceId : deviceIdArray) {
            String key = WebsocketInfo.class.getSimpleName() + deviceId;
            Iterator<Object> keys = redis.keys(key).iterator();
            if (!keys.hasNext()){
                continue;
            }
            String json = (String) redis.boundValueOps(key).get();

            WebsocketInfo websocketInfo = JsonUtil.toBean(json, WebsocketInfo.class);
            Gps gcj02 = PositionUtil.bd09_To_Gcj02(Double.parseDouble(websocketInfo.getBdwd()), Double.parseDouble(websocketInfo.getBdjd()));
            websocketInfo.setGdjd(String.valueOf(gcj02.getWgLon()));
            websocketInfo.setGdwd(String.valueOf(gcj02.getWgLat()));
            resList.add(websocketInfo);
        }
        return ApiResponse.success(resList);
    }

    public ApiResponse<List<WebsocketInfo>> inintGps1() {
        ApiResponse<List<WebsocketInfo>> apiResponse = new ApiResponse<>();
        List<WebsocketInfo> list = new ArrayList<>();
//        SysYh currentUser = getCurrentUser();
        SimpleCondition condition = new SimpleCondition(ClCl.class);
        String cphLike = getRequestParamterAsString("cphLike");
        String type = getRequestParamterAsString("type");
        if (StringUtils.isNotEmpty(cphLike)) {
            condition.like(ClCl.InnerColumn.cph, cphLike);
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request.getAttribute("appUserId") != null){
            String appUserId = (String) request.getAttribute("appUserId");
            SimpleCondition condition1 = new SimpleCondition(ClZdYh.class);
            condition1.eq(ClZdYh.InnerColumn.userId,appUserId);
            List<ClZdYh> zdYhs = zdYhMapper.selectByExample(condition1);
            if (zdYhs.size() == 0){
                return apiResponse;
            }
            List<String> zdbhs = zdYhs.stream().map(ClZdYh::getDeviceId).collect(Collectors.toList());
            condition.in(ClCl.InnerColumn.zdbh,zdbhs);
        }

        // 将终端编号,车辆信息缓存
        List<ClCl> selectAll = clclmapper.selectByExample(condition);
        Map<String, ClCl> zdbhClMap = selectAll.stream().filter(s -> StringUtils.isNotEmpty(s.getZdbh()))
                .collect(Collectors.toMap(ClCl::getZdbh, ClCl -> ClCl));


        // 获取终端状态
        condition = new LimitedCondition(ClZdgl.class);
//        condition.startWith(ClZdgl.InnerColumn.jgdm,currentUser.getJgdm());
        String zdLx = getRequestParamterAsString("zdLx");
        if(StringUtils.isNotBlank(zdLx)){
            condition.eq(ClZdgl.InnerColumn.zdLx,zdLx);
        }
        if (StringUtils.isNotEmpty(type)){
            condition.startWith(ClZdgl.InnerColumn.zdbh,type);
        }
        List<ClZdgl> zds = zdglservice.findByCondition(condition);

        Map<String,ClZdgl> deviceMap = zds.stream().collect(Collectors.toMap(ClZdgl::getZdbh,p->p));


        // 获取实时点位gps信息
        List<String> deviceIds = zds.stream().map(ClZdgl::getZdbh).collect(Collectors.toList());
        condition = new SimpleCondition(ClGps.class);
        condition.in(ClGps.InnerColumn.zdbh,deviceIds);
        List<ClGps> gpsInit = entityMapper.selectByExample(condition);
        // 将终端状态数据缓存
        Map<String, ClZdgl> zdglmap = zds.stream().filter(s -> StringUtils.isNotEmpty(s.getZdbh()))
                .collect(Collectors.toMap(ClZdgl::getZdbh, ClZdgl -> ClZdgl));

        // 获取不在线的终端id集合
        List<String> lostZD = new ArrayList<>();

        for (ClZdgl clZdgl : zds) {

            String obdInfo = (String) redis.boundValueOps("obdInfo_" + clZdgl.getZdbh()).get();
            if(StringUtils.isNotBlank(obdInfo)) {
                GpsObdMessageBean gpsObdMessageBean = JsonUtil.toBean(obdInfo, GpsObdMessageBean.class);
                clZdgl.setObdInfo(gpsObdMessageBean);
            }

            if (StringUtils.equals(clZdgl.getZxzt(), "20")) {
                lostZD.add(clZdgl.getZdbh());
            }

        }

        if (CollectionUtils.isNotEmpty(gpsInit)) {
            for (ClGps clgps : gpsInit) {
                if (StringUtils.isNotEmpty(clgps.getZdbh())) {
                    ClCl clCl = zdbhClMap.get(clgps.getZdbh());
                    if (clCl != null) {
                        WebsocketInfo websocketInfo = new WebsocketInfo();
                        websocketInfo.setBdjd(clgps.getBdjd().toString());
                        websocketInfo.setBdwd(clgps.getBdwd().toString());
                        websocketInfo.setGdjd(clgps.getGgjd().toString());
                        websocketInfo.setGdwd(clgps.getGgwd().toString());
                        websocketInfo.setFxj(clgps.getFxj());
                        websocketInfo.setClid(clCl.getClId());
                        websocketInfo.setCph(clCl.getCph());
                        websocketInfo.setTime(clgps.getCjsj());
                        websocketInfo.setZdbh(clgps.getZdbh());
                        websocketInfo.setCx(clCl.getCx());
                        websocketInfo.setSjxm(clCl.getSjxm());
                        websocketInfo.setSpeed(clgps.getYxsd());
                        ClZdgl device = deviceMap.get(clgps.getZdbh());
                        if (device != null){
                            websocketInfo.setZdLx(device.getZdLx());
                        }
                        if (StringUtils.isNotEmpty(clCl.getObdCode())) {
                            websocketInfo.setObdId(clCl.getObdCode());
                        }


                        Set<Object> keys = redis.keys("CX," + clgps.getZdbh()+"*");
                        //查询上一次记录的行程
                        if (keys.size() > 0){
                            String tmpKey = keys.iterator().next().toString();
                            String prevTime = tmpKey.split(",")[2];
                            DateTime startTime = DateTime.parse(prevTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
                            Date now = new Date();
                            long duration = now.getTime() - startTime.toDate().getTime();
                            websocketInfo.setDurartion(duration);
                        }

                        if (lostZD.contains(clgps.getZdbh())) {
                            websocketInfo.setZxzt("20");
                            websocketInfo.setLxsc(nowTime(clgps.getCjsj()));
                            list.add(websocketInfo);
                        } else {
                            websocketInfo.setZxzt(zdglmap.get(clgps.getZdbh()).getZxzt());
                            list.add(websocketInfo);
                        }
                    }
                }
            }
        }
        apiResponse.setResult(list);
        return apiResponse;
    }

    public Date simpledate(String date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date2;
    }


    public long nowTime(Date time) {

        Date now = new Date();
        long time2 = now.getTime();
        long time3 = time.getTime();
        return time2 - time3;
    }



    /**
     * 记录行程的开始个结束
     * @param gpsInfo
     */
    private void clXc(GpsInfo gpsInfo){
        errorLog.error("clXc:"+gpsInfo.toString());
    	if(StringUtils.equals(gpsInfo.getEventType(),"80")) {
            errorLog.error("接收到离线事件，直接退出:");
        	//如果是接收到离线事件，直接退出
            return;
        }
    	// 终端编号
    	String zdbh = gpsInfo.getDeviceId();
    	//行程Redis key
        String clxcKey = "CX," + zdbh;
    	// 当前GPS点时间
        String currentTime = gpsInfo.getStartTime();
        //上一次的GPS点时间
    	String prevTime = null;
    	try{
    		Set<Object> keys = redis.keys(clxcKey+"*");
            //查询上一次记录的行程
            if (keys.size() > 0){
            	String tmpKey = keys.iterator().next().toString();
            	prevTime = tmpKey.split(",")[2];
            }
    	}catch(Exception e){
            errorLog.error("redis.keys异常:",e);
        }

    	if (StringUtils.isBlank(prevTime)){
            errorLog.error("StringUtils.isBlank(prevTime)");
    		DateTime startTime = DateTime.parse(currentTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
            DateTime endTime = DateTime.now();
            Period per = new Period(startTime, endTime, PeriodType.minutes());
            int minute = per.getMinutes();
            //如果新的GPS点数据和上一次缓存的GPS点数据相差20分钟，认为上一次行程已经结束，新传入的GPS点作为开始时间
            if (minute >= 20){
            	redis.delete(clxcKey+"*");

                errorLog.error("minute >= 20");
            	//prevTime = endTime.toString("yyyy-MM-dd HH:mm:ss");
            	return;
            }else{
            	prevTime = startTime.toString("yyyy-MM-dd HH:mm:ss");
            }
        }
        errorLog.error("xcMainKey"+clxcKey + "," + prevTime);
        redis.boundValueOps(clxcKey + "," + prevTime).set(currentTime, 5, TimeUnit.MINUTES);
    }

    class XcThread implements Runnable{
        GpsInfo gpsInfo;
        public XcThread(GpsInfo gpsInfo){
            this.gpsInfo = gpsInfo;
        }
        @Override
        public void run() {
            errorLog.error("clXc:"+gpsInfo.toString());
            if(StringUtils.equals(gpsInfo.getEventType(),"80")) {
                errorLog.error("接收到离线事件，直接退出:");
                //如果是接收到离线事件，直接退出
                return;
            }
            // 终端编号
            String zdbh = gpsInfo.getDeviceId();
            //行程Redis key
            String clxcKey = "CX," + zdbh;
            // 当前GPS点时间
            String currentTime = gpsInfo.getStartTime();
            DateTime startTime = DateTime.parse(currentTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
            DateTime endTime = DateTime.now();
            Period per = new Period(startTime, endTime, PeriodType.minutes());
            int minute = per.getMinutes();
            //如果新的GPS点数据和上一次缓存的GPS点数据相差20分钟，认为上一次行程已经结束，新传入的GPS点作为开始时间
            if (minute >= 20){
                errorLog.error("minute >= 20");
                //prevTime = endTime.toString("yyyy-MM-dd HH:mm:ss");
                return;
            }
            //上一次的GPS点时间
            String prevTime = null;

                try{
                    Set<Object> keys = redis.keys(clxcKey+"*");
                    //查询上一次记录的行程
                    if (keys.size() > 0){
                        String tmpKey = keys.iterator().next().toString();
                        prevTime = tmpKey.split(",")[2];
                    }
                    if ("50".equals(gpsInfo.getEventType())){
                        Object o = redis.boundValueOps(clxcKey + "," + prevTime).get();
                        if(o != null){
                            redis.delete(clxcKey + "," + prevTime);
                            // 点火事件 结束当前轨迹
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            redis.boundValueOps("endNow,"+zdbh+","+prevTime).set(currentTime, 1, TimeUnit.MILLISECONDS);
                        }

                        prevTime = gpsInfo.getStartTime();
                        redis.boundValueOps(clxcKey + "," + prevTime).set(currentTime, 5, TimeUnit.MINUTES);
                        return;
                    }
                }catch(Exception e){
                    errorLog.error("redis.keys异常:",e);
                }

            if(StringUtils.equals(gpsInfo.getEventType(), "60") && StringUtils.isNotBlank(prevTime)){
                redis.boundValueOps(clxcKey + "," + prevTime).set(currentTime, 1, TimeUnit.MILLISECONDS);
                return;
            }
            if (StringUtils.isBlank(prevTime)){
                errorLog.error("StringUtils.isBlank(prevTime)");
                startTime = DateTime.parse(currentTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
                 endTime = DateTime.now();
                 per = new Period(startTime, endTime, PeriodType.minutes());
                 minute = per.getMinutes();
                //如果新的GPS点数据和上一次缓存的GPS点数据相差20分钟，认为上一次行程已经结束，新传入的GPS点作为开始时间
                if (minute >= 20){
                    redis.delete(clxcKey+"*");

                    errorLog.error("minute >= 20");
                    //prevTime = endTime.toString("yyyy-MM-dd HH:mm:ss");
                    return;
                }else{
                    prevTime = startTime.toString("yyyy-MM-dd HH:mm:ss");
                }
            }
            errorLog.error("xcMainKey"+clxcKey + "," + prevTime);
            redis.boundValueOps(clxcKey + "," + prevTime).set(currentTime, 5, TimeUnit.MINUTES);
        }
    }


}
