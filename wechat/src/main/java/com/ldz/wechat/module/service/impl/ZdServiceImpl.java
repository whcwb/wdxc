package com.ldz.wechat.module.service.impl;

import com.ldz.geo.bean.GeoModel;
import com.ldz.geo.util.GeoUtil;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.wechat.base.BaseServiceImpl;
import com.ldz.wechat.base.LimitedCondition;
import com.ldz.wechat.module.bean.NearbyStation;
import com.ldz.wechat.module.bean.Router;
import com.ldz.wechat.module.mapper.ClZdMapper;
import com.ldz.wechat.module.model.ClXl;
import com.ldz.wechat.module.model.ClXlzd;
import com.ldz.wechat.module.model.ClZd;
import com.ldz.wechat.module.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ZdServiceImpl extends BaseServiceImpl<ClZd,String> implements ZdService {
    @Autowired
    private ClZdMapper entityMapper;
    @Autowired
    private XlzdService xlzdService;
    @Autowired
    private JgService jgService;
    @Autowired
    private ClyxjlService clyxjlService;
    @Autowired
    private ZdService zdService;
    @Autowired
    private XlService xlService;
    @Autowired
    private GeoUtil geoUtil;


    @Override
    protected Mapper<ClZd> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClZd.class;
    }



    private Map<String,Object> stationToMap(ClZd zd,Double distance){
        Map<String,Object> station = new HashMap<>();
        station.put("id",zd.getId());
        station.put("xlId",zd.getXlId());
        station.put("name",zd.getMc());
        station.put("distance",distance.intValue());
        station.put("lng",zd.getJd());
        station.put("lat",zd.getWd());
        station.put("routerList",new ArrayList());
        return station;
    }

    private Map<String,Object> routerToMap(ClXl xl){
        Map<String,Object> router = new HashMap<>();
        router.put("id",xl.getId());
        router.put("name",xl.getXlmc());
        router.put("startTime",xl.getYxkssj());
        router.put("endTime",xl.getYxjssj());
        router.put("endStation",getEndStation(xl.getId()));
        router.put("direct",xl.getYxfs());
        return router;
    }

    private void setStationOrder(ClZd station) {
        if (StringUtils.isEmpty(station.getId()) || StringUtils.isEmpty(station.getXlId())){
            return;
        }
        SimpleCondition condition = new SimpleCondition(ClXlzd.class);
        condition.eq(ClXlzd.InnerColumn.zdId,station.getId());
        condition.eq(ClXlzd.InnerColumn.xlId,station.getXlId());
        List<ClXlzd> xlzds = xlzdService.findByCondition(condition);
        if (xlzds.size() == 0)return;
        station.setRouteOrder(xlzds.get(0).getXh());
    }

    private Short getStationOrder(String xlId,String zdId){
        if (StringUtils.isEmpty(xlId) || StringUtils.isEmpty(zdId)){
            return null;
        }
        SimpleCondition condition = new SimpleCondition(ClXlzd.class);
        condition.eq(ClXlzd.InnerColumn.zdId,zdId);
        condition.eq(ClXlzd.InnerColumn.xlId,xlId);
        List<ClXlzd> xlzds = xlzdService.findByCondition(condition);
        if (xlzds.size() == 0)return null;
        return xlzds.get(0).getXh();
    }
    private ClZd getEndStation(String xlId){
        List<ClXlzd> xlzds = xlzdService.findEq(ClXlzd.InnerColumn.xlId,xlId);
        if (xlzds.size() == 0)return null;
        List<String> zdIds = xlzds.stream().map(ClXlzd::getZdId).collect(Collectors.toList());
        List<ClZd> zds = zdService.findIn(ClZd.InnerColumn.id,zdIds);
        Map<String,ClZd> zdMap = zds.stream().collect(Collectors.toMap(ClZd::getId,p->p));
        if (zds.size() == 0)return null;
        for (ClXlzd xlzd : xlzds) {
            String zdId = xlzd.getZdId();
            if (StringUtils.isEmpty(zdId))continue;
            ClZd zd = zdMap.get(zdId);
            if (zd == null)continue;
            zd.setXlId(xlzd.getXlId());
        }
        for (ClZd zd : zds) {
            setStationOrder(zd);
        }
        zds.sort(Comparator.comparingInt(p-> (int)p.getRouteOrder()));
        return zds.get(zds.size() - 1);
    }

    @Override
    public ApiResponse<Map<String, Object>> getStationInfo(String lng, String lat) {
        Map<String,Object> resultMap = new HashMap<>();
        List<NearbyStation> nearbyStations = getNearbyStations(lng,lat); // 附近站点
        List<Router> nearbyRouters = new ArrayList<>();
        List<Router> otherRouters = new ArrayList<>(); // 其他线路
        List<Router> scheduledBusRouters = new ArrayList<>(); // 班车的列表
        resultMap.put("nearbyStations",nearbyStations);//附近站点
        resultMap.put("otherRouters",otherRouters);//校巴的列表
        resultMap.put("scheduledBusRouters",scheduledBusRouters);//班车的列表

        List<String> nearbyStationIds = nearbyStations.stream().map(NearbyStation::getId).collect(Collectors.toList());

        // 获取线路信息
        LimitedCondition condition1 = new LimitedCondition(ClXl.class);
        List<ClXl> allOrgRouters = xlService.findByCondition(condition1);
        List<ClXlzd> xlzds;
        if (nearbyStationIds.size() == 0){
            xlzds = new ArrayList<>();
        }else{
            xlzds = xlzdService.findIn(ClXlzd.InnerColumn.zdId,nearbyStationIds);
        }
        List<String> nearbyRouterIds;
        if (xlzds.size() != 0){
            nearbyRouterIds = xlzds.stream().map(ClXlzd::getXlId).collect(Collectors.toList());
            setRouterIds(nearbyStations,xlzds);
        }else{
            nearbyRouterIds = new ArrayList<>();
        }
        for (ClXl router : allOrgRouters) {
            Router router1 = new Router(router,getEndStation(router.getId()));
            if (nearbyRouterIds.contains(router.getId())){
                nearbyRouters.add(router1);
            }else{
                if(StringUtils.equals(router.getLx(),"30")){//20班车  30校巴
                    scheduledBusRouters.add(router1);
                }else if(StringUtils.equals(router.getLx(),"20")){
                    otherRouters.add(router1);
                }
            }
        }
        for (Router nearbyRouter : nearbyRouters) {
            String routerId = nearbyRouter.getId();
            if (StringUtils.isEmpty(routerId))continue;
            List<NearbyStation> stationList = getStationsByXlId(routerId,nearbyStations);
            if (stationList.size() == 0)continue;
            for (NearbyStation station : stationList) {
                Short order = getStationOrder(nearbyRouter.getId(),station.getId());
                nearbyRouter.setOrder(order);
                List<Router> routerList = station.getRouterList();
                ApiResponse<List<Integer>> nsRes = xlService.getNextCars(routerId,station.getId());
                if (nsRes.isSuccess() && nsRes.getResult() != null){
                    nearbyRouter.setNextBus(nsRes.getResult());
                }
                routerList.add(nearbyRouter);
            }
        }
        return ApiResponse.success(resultMap);
    }

//    public ApiResponse<Map<String, Object>> getStationInfo(String lng, String lat) {
//        Map<String,Object> resultMap = new HashMap<>();
//        List<Map<String,Object>> nearbyStations = new ArrayList<>(); // 附近站点
//        List<Map<String,Object>> nearbyRouters = new ArrayList<>();
//        List<Map<String,Object>> otherRouters = new ArrayList<>(); // 其他线路
//        resultMap.put("nearbyStations",nearbyStations);
//        resultMap.put("otherRouters",otherRouters);
//
//        // 查找附近站点
//        if (StringUtils.isNotEmpty(lng) && StringUtils.isNotEmpty(lat)){
//            LimitedCondition condition = new LimitedCondition(ClZd.class);
//            List<ClZd> stationList = zdService.findByCondition(condition);
//            Map<String,Double> distanceMap = new HashMap<>(stationList.size());
//            double maxDistance = 100D;
//            for (ClZd zd : stationList) {
//                setStationOrder(zd);
//                double distance = DistanceUtil.getShortDistance(zd.getJd(),zd.getWd(),new Double(lng),new Double(lat));
//                distanceMap.put(zd.getId(),distance);
//                if (distance < maxDistance){
//                    Map<String,Object> station = stationToMap(zd, distance);
//                    nearbyStations.add(station);
//                }
//            }
//
//            // 如果没有附近站点，则选取最近的一个站点
//            if (nearbyStations.size() == 0){
//                Map<String,ClZd> stationMap = stationList.stream().collect(Collectors.toMap(ClZd::getId,p->p));
//                List<Map.Entry<String,Double>> entryList = new ArrayList<>(distanceMap.entrySet());
//                entryList.sort((o1, o2) -> {
//                    Double v1 = o1.getValue();
//                    Double v2 = o2.getValue();
//                    return new Double(v1 - v2).intValue();
//                });
//                String id = entryList.get(0).getKey();
//                Map<String,Object> station = stationToMap(stationMap.get(id),distanceMap.get(id));
//                nearbyStations.add(station);
//            }
//        }
//
//        List<String> nearbyStationIds = new ArrayList<>();
//        for (Map<String, Object> station : nearbyStations) {
//            String id = (String) station.get("id");
//            nearbyStationIds.add(id);
//        }
//
//        // 获取线路信息
//        LimitedCondition condition1 = new LimitedCondition(ClXl.class);
//        List<ClXl> allOrgRouters = xlService.findByCondition(condition1);
//        List<ClXlzd> xlzds;
//        if (nearbyStationIds.size() == 0){
//            xlzds = new ArrayList<>();
//        }else{
//            xlzds = xlzdService.findIn(ClXlzd.InnerColumn.zdId,nearbyStationIds);
//        }
//        List<String> nearbyRouterIds;
//        if (xlzds.size() != 0){
//            nearbyRouterIds = xlzds.stream().map(ClXlzd::getXlId).collect(Collectors.toList());
//            setRouterIds(nearbyStations,xlzds);
//        }else{
//            nearbyRouterIds = new ArrayList<>();
//        }
//        for (ClXl router : allOrgRouters) {
//            Map<String,Object> map = routerToMap(router);
//            if (nearbyRouterIds.contains(router.getId())){
//                nearbyRouters.add(map);
//            }else{
//                otherRouters.add(map);
//            }
//        }
//        for (Map<String, Object> nearbyRouter : nearbyRouters) {
//            String routerId = (String) nearbyRouter.get("id");
//            if (StringUtils.isEmpty(routerId))continue;
//            List<Map<String,Object>> stationList = getStationsByXlId(routerId,nearbyStations);
//            if (stationList.size() == 0)continue;
//            for (Map<String, Object> station : stationList) {
//                Map<String, Object> router = new HashMap<>(nearbyRouter);
//                Short order = getStationOrder(((String) router.get("id")),station.get("id").toString());
//                router.put("order",order);
//                List<Map<String,Object>> routerList = (List<Map<String, Object>>) station.get("routerList");
//                ApiResponse<List<Integer>> nsRes = xlService.getNextCars(routerId,station.get("id").toString());
//                if (nsRes.isSuccess() && nsRes.getResult() != null){
//                    router.put("nextBus",nsRes.getResult());
//                }
//                routerList.add(router);
//            }
//        }
//        nearbyStations.sort((o1, o2) -> {
//            int distance1 = (int) o1.get("distance");
//            int distance2 = (int) o2.get("distance");
//            return distance1 - distance2;
//        });
//        return ApiResponse.success(resultMap);
//    }

    public List<NearbyStation> getNearbyStations(String lng, String lat) {
        if (!geoUtil.hasKey("stations")){
            initGeo();
        }
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoUtil.getRadius("stations",new Double(lng),new Double(lat),100,true,"ASC",0);
        Map<String,Double> stationDistanceMap = new HashMap<>();
        if(CollectionUtils.isEmpty(results.getContent())){
            return new ArrayList<>();
        }
        List<String> stationIds = new ArrayList<>();
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results.getContent()) {
            String stationId = result.getContent().getName();
            stationIds.add(stationId);
            stationDistanceMap.put(stationId,result.getDistance().getValue());
        }
        List<ClZd> stationList = findIn(ClZd.InnerColumn.id,stationIds);
        if (stationList.size() == 0){
            return  new ArrayList<>();
        }

        List<NearbyStation> nearbyStations = new ArrayList<>(stationList.size());
        for (ClZd zd : stationList) {
            String stationId = zd.getId();
            NearbyStation station = new NearbyStation(zd,stationDistanceMap.get(stationId));
            nearbyStations.add(station);
        }
        return nearbyStations;
    }

    public void initGeo(){
        LimitedCondition limitedCondition = new LimitedCondition(ClZd.class);
        List<ClZd> stationList = zdService.findByCondition(limitedCondition);
        List<GeoModel> geoModels = new ArrayList<>(stationList.size());
        for (ClZd zd : stationList) {
            GeoModel model = convertToGeoModel(zd);
            geoModels.add(model);
        }
        geoUtil.add("stations",geoModels,1,TimeUnit.DAYS);
    }

    private GeoModel convertToGeoModel(ClZd zd){
        GeoModel model = new GeoModel();
        model.setName(zd.getId());
        model.setLng(zd.getJd());
        model.setLat(zd.getWd());
        return model;
    }

    private List<NearbyStation> getStationsByXlId(String xlId,List<NearbyStation> nearbyStations){
        List<NearbyStation> stationList = new ArrayList<>();
        for (NearbyStation station : nearbyStations) {
            if (xlId.equals(station.getXlId())){
                stationList.add(station);
            }
        }
        return stationList;
    }

    private void setRouterIds(List<NearbyStation> list,List<ClXlzd> xlzds){
        for (NearbyStation station : list) {
            String xlId = getXlId(xlzds,station.getId());
            station.setXlId(xlId);
        }
    }

    private String getXlId(List<ClXlzd> xlzds,String zdId){
        for (ClXlzd xlzd : xlzds) {
            if (zdId.equals(xlzd.getZdId())){
                return xlzd.getXlId();
            }
        }
        return null;
    }
}

