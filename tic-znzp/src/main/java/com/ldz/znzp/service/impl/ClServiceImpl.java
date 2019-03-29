package com.ldz.znzp.service.impl;

import com.ldz.geo.bean.GeoModel;
import com.ldz.geo.util.GeoUtil;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.commonUtil.SnowflakeIdWorker;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.util.gps.DistanceUtil;
import com.ldz.util.gps.Gps;
import com.ldz.util.gps.PositionUtil;
import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.bean.GpsInfo;
import com.ldz.znzp.bean.ReportData;
import com.ldz.znzp.mapper.*;
import com.ldz.znzp.model.*;
import com.ldz.znzp.service.*;
import com.ldz.znzp.util.NettyUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClServiceImpl extends BaseServiceImpl<ClCl,String> implements ClService{
    @Autowired
    private ClClMapper entityMapper;
    @Autowired
    private ClPbMapper clPbMapper;
    @Autowired
    private ClZdMapper zdMapper;
    @Autowired
    private ClXlzdMapper xlzdMapper;
    @Autowired
    private ClClyxjlMapper clyxjlMapper;
    @Autowired
    private ZdService zdService;
    @Autowired
    private GpsService gpsService;
    @Autowired
    private XlService xlService;
    @Autowired
    private ZnzpService znzpService;
    @Autowired
    private NettyUtil nettyUtil;
    @Autowired
    private ClyxjlService clyxjlService;
    @Autowired
    public SnowflakeIdWorker idGenerator;
    @Autowired
    private XlzdService xlzdService;
    @Autowired
    private GeoUtil geoUtil;
    @Override
    protected Mapper<ClCl> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClCl.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClCl entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }

    @Override
    public ApiResponse<String> report(String tid,ClPb clPb,ClCl car,ClXl xl,ClClyxjl clClyxjl) {
        RuntimeCheck.ifNull(clClyxjl,"未找到车辆运行记录");
        RuntimeCheck.ifBlank(clPb.getXlId(),"未找到线路");
        List<ClZnzp> znzps = znzpService.getByXlId(clPb.getXlId());
        RuntimeCheck.ifEmpty(znzps,"未找到通道");
        List<String> zpIds = znzps.stream().map(ClZnzp::getZdbh).collect(Collectors.toList());

        ReportData reportData = new ReportData();
        reportData.setRouteId(xl.getId());
        reportData.setRouteName(xl.getXlmc());
        reportData.setDirect(clClyxjl.getYxfx());
        reportData.setStationNo(clClyxjl.getZdbh());
        reportData.setType(clClyxjl.getZt());
        reportData.setBus_plate(clClyxjl.getCphm());
        reportData.setDirect("up");
        Map<String,Object> channelMap = nettyUtil.getChannelByTids(zpIds);
        for (Map.Entry<String, Object> entry : channelMap.entrySet()) {
            reportData.setTid(entry.getKey());
            log.info(JsonUtil.toJson(reportData));
            writeResult((Channel)entry.getValue(),reportData);
        }
        return ApiResponse.success();
    }


    public ApiResponse<String> updateGpsNew(GpsInfo gpsInfo, ClPb pb,ClCl car,ClXl route, ClClyxjl record){

        if(ObjectUtils.isEmpty(route)){
            return ApiResponse.notFound("未找到车辆线路");
        }
        // 判断当前线路站点是否缓存至redis
        if(!geoUtil.hasKey(route.getId()+"_stations")){
            initXlZd(route.getId());
        }
        // 判断设备在线状态
        String zt = "running";
        Date now = new Date();
        if ("80".equals(gpsInfo.getEventType())
                || "20".equals(gpsInfo.getSczt())
                || gpsInfo.getLatitude().equals("-1")
                || gpsInfo.getLongitude().equals("-1")
                || StringUtils.isEmpty(gpsInfo.getLongitude())
                || StringUtils.isEmpty(gpsInfo.getLatitude())
                ){
            zt = "off";
            if (record == null){
                log.info("gps为 -1");
                record = new ClClyxjl();
                record.setCjsj(now);
                record.setClId(car.getClId());
                record.setCphm(car.getCph());
                record.setId(""+idGenerator.nextId());
                record.setZt(zt);
                if(route != null){
                    record.setXlId(route.getId());
                    record.setXlmc(route.getXlmc());
                }
                clyxjlMapper.insertSelective(record);
            }else{
                record.setZt(zt);
                clyxjlMapper.updateByPrimaryKeySelective(record);
            }
            return ApiResponse.success("设备已离线");
        }

        // 经纬度转换
        ClGps clGps = gpsService.changeCoordinates(gpsInfo);
        Gps gps = new Gps(Double.parseDouble(gpsInfo.getLatitude()),Double.parseDouble(gpsInfo.getLongitude()));
        boolean hasRecord = record != null && StringUtils.isNotEmpty(record.getZdId());
        // 根据传入进来的线路id ,经纬度 找到最近的两个站点 （或者找到当前线路的所有站点距离排序 取最近两个站点），
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoLocationGeoResults = geoUtil.getRadius(route.getId() + "_stations", gps.getWgLon(), gps.getWgLat(), 20000000, true, "ASC", 2);
        List<String> stationIds = new ArrayList<>();
        Map<String,Double> stationDistanceMap = new HashMap<>();
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoLocationGeoResult : geoLocationGeoResults.getContent()) {
            String stationId = geoLocationGeoResult.getContent().getName();
            stationIds.add(stationId);
            stationDistanceMap.put(stationId,geoLocationGeoResult.getDistance().getValue());
        }
        List<ClZd> clZds = zdService.findIn(ClZd.InnerColumn.id, stationIds);

        List<ClZd> currentStations = new ArrayList<>();
        ClZd  currentStation = null;
        // 对当前最近的两个点进行比较
        for (ClZd clZd : clZds) {
            Short fw = clZd.getFw();
            Double distance = stationDistanceMap.get(clZd.getId());
            if(distance < fw){ // 所在距离在该站点的范围内
                zt = "inStation";
                currentStations.add(clZd);
            }
        }

        if(CollectionUtils.isEmpty(currentStations)) {// 当前站点空 说明车辆正在行驶的路上 取相近站点中序号较小的一个站
            SimpleCondition condition = new SimpleCondition(ClXlzd.class);
            condition.eq(ClXlzd.InnerColumn.zdId,clZds.get(0).getId());
            condition.eq(ClXlzd.InnerColumn.xlId,route.getId());
            List<ClXlzd> xlzds = xlzdService.findByCondition(condition);
            short i = xlzds.get(0).getXh();

            SimpleCondition condition1 = new SimpleCondition(ClXlzd.class);
            condition1.eq(ClXlzd.InnerColumn.zdId,clZds.get(1).getId());
            condition1.eq(ClXlzd.InnerColumn.xlId,route.getId());
            List<ClXlzd> xlzds1 = xlzdService.findByCondition(condition1);
            short k = xlzds1.get(0).getXh();

            if (i < k) {
                currentStation = clZds.get(0);
            } else {
                currentStation = clZds.get(1);
            }

        }else if(CollectionUtils.size(currentStations) == 2){
            // 取距离近的
            Double distan1 = stationDistanceMap.get(currentStations.get(0).getId());
            Double distan2 = stationDistanceMap.get(currentStations.get(1).getId());
            if(distan1 <= distan2){
                currentStation = currentStations.get(0);
            }else{
                currentStation = currentStations.get(1);
            }
        }else{
            currentStation = currentStations.get(0);
        }

        currentStation.setXlId(route.getId());
        zdService.setStationOrder(currentStation);
        record.setZdbh(currentStation.getRouteOrder());
        record.setZdmc(currentStation.getMc());
        record.setZdId(currentStation.getId());
        record.setCjsj(now);
        record.setJd(clGps.getBdjd());
        record.setWd(clGps.getBdwd());
        record.setZt(zt);
		//2018年9月20日。新增设备ID
        record.setSzdbh(gpsInfo.getDeviceId());
        if (hasRecord){
            clyxjlMapper.updateByPrimaryKeySelective(record);
        }else{
            clyxjlMapper.insertSelective(record);
        }
        if ("80".equals(gpsInfo.getSczt()) || "20".equals(gpsInfo.getSczt())){
            return ApiResponse.success("设备已离线");
        }

        return ApiResponse.success();
    }


    /**
     * 初始化每条线路的站点
     */
    public void initXlZd(String xlId){
        List<ClZd> clZds = zdService.getByXlId(xlId); // 根据线路id 获取到当前线路的线路站点
        List<GeoModel> geoModels = new ArrayList<>(clZds.size());
        // 缓存至redis
        for (ClZd zd : clZds) {
            GeoModel geoModel = zdToGeoModel(zd);
            geoModels.add(geoModel);
        }
        geoUtil.add(xlId + "_stations", geoModels, 1, TimeUnit.DAYS);
    }

    private GeoModel zdToGeoModel(ClZd zd){
        GeoModel model = new GeoModel();
        model.setName(zd.getId());
        // 将站点的百度坐标转换为 wgs84
        Gps gps = PositionUtil.bd09_To_Gcj02(zd.getWd(), zd.getJd());
        model.setLng(gps.getWgLon());
        model.setLat(gps.getWgLat());
        return model;
    }


    @Override
    public ApiResponse<String> updateGps(GpsInfo gpsInfo, ClPb pb,ClCl car,ClXl route, ClClyxjl record) {// 站点存储百度位置
        // 30.5411624384,114.3167198864（原始点,谷歌地球）
        // 30.5371683904,114.3242740669（原始点,谷歌地球）
        /**
         * 谷歌地图：30.5288716874,114.4212491503
         * 百度地图：30.5345399945,114.4278552156
         * 腾讯高德：30.5288493080,114.4212341309
         * 图吧地图：30.5271335880,114.4230130709
         * 谷歌地球：30.5311635880,114.4157130709
         * 北纬N30°31′52.19″ 东经E114°24′56.57″
         */

        // 原始gps转百度gps
        log.info("转换前-经度："+gpsInfo.getLongitude()+",纬度："+gpsInfo.getLatitude());
        Date now = new Date();
        if ("80".equals(gpsInfo.getEventType())
                || "20".equals(gpsInfo.getSczt())
                || gpsInfo.getLatitude().equals("-1")
                || gpsInfo.getLongitude().equals("-1")
                || StringUtils.isEmpty(gpsInfo.getLongitude())
                || StringUtils.isEmpty(gpsInfo.getLatitude())
                ){
            if (record == null){
                log.info("gps为 -1");
                record = new ClClyxjl();
                record.setCjsj(now);
                record.setClId(car.getClId());
                record.setCphm(car.getCph());
                record.setId(""+idGenerator.nextId());
                record.setZt("off");
                if(route != null){
                    record.setXlId(route.getId());
                    record.setXlmc(route.getXlmc());
                }
                clyxjlMapper.insertSelective(record);
            }else{
                record.setZt("off");
                clyxjlMapper.updateByPrimaryKeySelective(record);
            }
            return ApiResponse.success("设备已离线");
        }

        ClGps clGps = gpsService.changeCoordinates(gpsInfo);
        log.info("转换后-经度："+clGps.getBdjd()+",纬度："+clGps.getBdwd());

        // 获取车辆运行记录
        // 获取当前站点
        boolean hasRecord = record != null && StringUtils.isNotEmpty(record.getZdId());
        ClZd currentStation = null;
        String zt = null;
        Gps gps = new Gps(clGps.getBdjd().doubleValue(),clGps.getBdwd().doubleValue());
        if (record == null){
            log.info("没有运行记录");
            currentStation = findCurrentZd("",gps,car,pb);
            record = new ClClyxjl();
            record.setCjsj(now);
            record.setClId(car.getClId());
            record.setCphm(car.getCph());
            record.setId(""+idGenerator.nextId());
            if(route != null){
                record.setXlId(route.getId());
                record.setXlmc(route.getXlmc());
            }
        }else {
            log.info("已有运行记录："+record.toString());
            String stationId = record.getZdId();
            currentStation = zdService.findById(stationId);
            double distance = DistanceUtil.getShortDistance(currentStation.getJd(),currentStation.getWd(),clGps.getBdjd().doubleValue(),clGps.getBdwd().doubleValue());
            log.info("distance:"+distance);
            if (distance < currentStation.getFw()){
                if ("80".equals(gpsInfo.getEventType())){
                    zt = "off";
                }else{
                    zt = "inStation"; // 进站
                }
            }else{
                currentStation = findCurrentZd(record.getZdId(),gps,car,pb);
            }
        }
        if (zt == null){
            if ("80".equals(gpsInfo.getEventType())){
                zt = "off";
            }else{
                double distance = DistanceUtil.getLongDistance(currentStation.getJd(),currentStation.getWd(),clGps.getBdjd().doubleValue(),clGps.getBdwd().doubleValue());
                log.info("distance:"+distance);
                if (distance < currentStation.getFw()){
                    zt = "inStation"; // 进站
                }else{
                    zt = "runing"; // 正常
                }
            }
        }
        currentStation.setXlId(route.getId());
        zdService.setStationOrder(currentStation);
        record.setZdbh(currentStation.getRouteOrder());
        record.setZdmc(currentStation.getMc());
        record.setZdId(currentStation.getId());
        record.setCjsj(new Date());
        record.setJd(clGps.getBdjd());
        record.setWd(clGps.getBdwd());
        record.setZt(zt);
        //2018年9月20日。新增设备ID
        record.setSzdbh(gpsInfo.getDeviceId());
        if (hasRecord){
            clyxjlMapper.updateByPrimaryKeySelective(record);
        }else{
            clyxjlMapper.insertSelective(record);
        }
        if ("80".equals(gpsInfo.getSczt()) || "20".equals(gpsInfo.getSczt())){
            return ApiResponse.success("设备已离线");
        }
        return ApiResponse.success();
    }

    private List<ClZd> getStationList(ClPb pb){
        // 获取车辆当天线路信息
        String xlId = pb.getXlId();
        // 获取线路站点
        SimpleCondition condition = new SimpleCondition(ClXlzd.class);
        condition.eq(ClXlzd.InnerColumn.xlId,xlId);
        List<ClXlzd> xlzds = xlzdMapper.selectByExample(condition);
        if (xlzds.size() == 0){
            return null;
        }
        Map<String,Short> stationXhMap = xlzds.stream().collect(Collectors.toMap(ClXlzd::getZdId,ClXlzd::getXh));

        List<String> stationIds = xlzds.stream().map(ClXlzd::getZdId).collect(Collectors.toList());
        condition = new SimpleCondition(ClZd.class);
        condition.in(ClZd.InnerColumn.id,stationIds);
        List<ClZd> stations = zdMapper.selectByExample(condition);
        for (ClZd station : stations) {
            station.setXlId(xlId);
            Short xh = stationXhMap.get(station.getId());
            if (xh != null)station.setRouteOrder(xh);
        }
        return stations;
    }

    @Override
    public ClZd getCurrentZd(BigDecimal jd,BigDecimal wd,ClCl car,String currentZdId,ClPb pb){
        // 判断是否有当前站点信息
        // 如果有当前站点，再判断与当前站点距离
        Gps currentGps = new Gps(jd.doubleValue(),wd.doubleValue());
        ClZd currentZd = zdMapper.selectByPrimaryKey(currentZdId);
        if (currentZd == null)return null;
        Gps stationGps = new Gps(currentZd.getJd(),currentZd.getWd());
        double distance = DistanceUtil.getShortDistance(currentGps,stationGps);
        if (distance < currentZd.getFw()){ // 如果在站点范围之内，则这就是当前站点
            return currentZd;
        }else{ // 如果不在站点范围之内，查找最近的站点
            return findCurrentZd(currentZdId,currentGps,car,pb);
        }
    }

    @Override
    public ApiResponse<String> report(String tid) {
        ClCl car = getByDeviceId(tid);
        if (car == null)return ApiResponse.fail("未找到车辆");
        ClPb pb = getCarPb(car.getClId());
        if (pb == null)return ApiResponse.fail("未找到车辆排班");
        ClXl route = xlService.getByCarId(car.getClId());
        if (route == null)return ApiResponse.fail("未找到车辆线路");
        List<ClClyxjl> clClyxjls = clyxjlService.findEq(ClClyxjl.InnerColumn.clId,car.getClId());
        return report(tid,pb,car,route,clClyxjls.size() == 0 ? null : clClyxjls.get(0));
    }

    @Override
    public ClZd findCurrentZd(String prvStationId,Gps currentGps,ClCl car,ClPb pb){
        List<ClZd> stations = getStationList(pb);
        if (stations == null)return null;
        if (stations.size() == 1)return stations.get(0);
        Map<String,ClZd> stationMap = stations.stream().collect(Collectors.toMap(ClZd::getId,p->p));

        Map<String,Double> distanceMap = new HashMap<>();
        short maxStationOrder = 0;
        for (ClZd station : stations) {
            Gps gps = new Gps(station.getJd(),station.getWd());
            Double d = DistanceUtil.getShortDistance(currentGps,gps);
            // 如果距离小于站点范围，则直接返回当前站点
            if (d < station.getFw()) return station;
            if (station.getRouteOrder() > maxStationOrder){
                maxStationOrder = station.getRouteOrder();
            }
            distanceMap.put(station.getId(),d);
        }
        List<Map.Entry<String,Double>> entryList = new ArrayList<>(distanceMap.entrySet());
        entryList.sort((o1, o2) -> {
            Double v1 = o1.getValue();
            Double v2 = o2.getValue();
            return new Double(v1 - v2).intValue();
        });
        String id0 = entryList.get(0).getKey();
        String id1 = entryList.get(1).getKey();
        ClZd station0 = stationMap.get(id0);
        ClZd station1 = stationMap.get(id1);
        ClZd stationB = null;
        // 如果比对站点包含
        if (station0.getRouteOrder() > station1.getRouteOrder()){
            stationB = station0;
        }else{
            stationB = station1;
        }
        if (stationB.getRouteOrder() == maxStationOrder){
            // 如果上一次站点是最后一个站点，并且状态为到站，则设置当前站点为离线
            if (prvStationId.equals(stationB.getId())){
                return stationB;
            }
        }
        ClZd currentStation = station0.getRouteOrder() < station1.getRouteOrder() ? station0 : station1;
//        zdService.setStationOrders(station0,station1);
        return currentStation;
    }

    @Override
    public ClPb getCarPb(String carId)  {
        SimpleCondition condition = new SimpleCondition(ClPb.class);
        condition.eq(ClPb.InnerColumn.clId,carId);
        try {
            condition.eq(ClPb.InnerColumn.pbsj,DateUtils.getDateToday("yyyy-MM-dd"));
        } catch (ParseException e) {
            log.error("时间转换出错");
        }
        List<ClPb> clPbs = clPbMapper.selectByExample(condition);
        if (clPbs.size() == 0){
            return null;
        }
        return clPbs.get(0);
    }

    @Override
    public ClPb getCarPbByDeviceId(String deviceId) {
        // 获取车辆信息
        List<ClCl> cars = findEq(ClCl.InnerColumn.zdbh,deviceId);
        if (cars.size() == 0){
            return null;
        }
        return getCarPb(cars.get(0).getClId());
    }

    @Override
    public ClCl getByDeviceId(String deviceId) {
        // 获取车辆信息
        List<ClCl> cars = findEq(ClCl.InnerColumn.zdbh,deviceId);
        if (cars.size() == 0){
            return null;
        }
        return cars.get(0);
    }

    private void addStation(){
        ClZd station = new ClZd();
        station.setId("3");
        station.setJd(new Double("30.5371683904"));
        station.setWd(new Double("114.3242740669"));
        station.setFw(new Short("10"));
        station.setMc("name");
        zdMapper.insertSelective(station);

        ClXlzd xlzd = new ClXlzd();
        xlzd.setId("3");
        xlzd.setXh(new Short("3"));
        xlzd.setXlId("1");
        xlzd.setZdId("3");
        xlzdMapper.insertSelective(xlzd);



    }

    private void writeResult(Channel channel,ReportData reportData){
        /**
         *
         "command":"reporting",
         "tid":"1234567890ab",
         "tid":"1234567890ab",
         "routeName":"环2",
         "routeId":"123",
         "direct":"up",
         "type":"in",
         "stationNo":"7",
         */
        if (channel == null)return;
        nettyUtil.sendData(channel,reportData);
    }
    private void writeResult(List<Channel> channels,ReportData reportData){
        for (Channel channel : channels) {
            log.info(channel.id().asShortText());
            writeResult(channel,reportData);
        }
    }
}
