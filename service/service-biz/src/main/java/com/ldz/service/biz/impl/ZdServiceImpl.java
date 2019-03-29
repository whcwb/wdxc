package com.ldz.service.biz.impl;

import com.ldz.dao.biz.bean.ClClyxjlModel;
import com.ldz.dao.biz.bean.DdClModel;
import com.ldz.dao.biz.mapper.ClZdMapper;
import com.ldz.dao.biz.model.*;
import com.ldz.service.biz.interfaces.*;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.sys.model.SysJg;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.service.JgService;
import com.ldz.sys.util.RedisUtil;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.util.gps.DistanceUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.common.Mapper;

import java.util.*;
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
    private ZnzpService znzpService;
    @Value("${znzpurl}")
    private String deleteZnzpRedisKeyUrl;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected Mapper<ClZd> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClZd.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClZd entity) {
    	 SysYh user = getCurrentUser();
         SysJg org = jgService.findByOrgCode(user.getJgdm());
         Date now = new Date();
         entity.setCjr(getOperateUser());
         entity.setCjsj(now);
         entity.setId(genId());
         entity.setJgdm(user.getJgdm());
         entity.setJgmc(org.getJgmc());
         save(entity);
        redisUtil.deleteRedisKey(deleteZnzpRedisKeyUrl + "/deleteRedisKey","com.ldz.znzp.mapper.ClXlMapper");
        return ApiResponse.saveSuccess();
    }

    @Override
    public List<ClZd> getByXlId(String xlId) {
        return getByXlIds(Collections.singletonList(xlId));
    }

    @Override
    public List<ClZd> getByXlIds(List<String> xlIds) {
        List<ClXlzd> xlzds = xlzdService.findIn(ClXlzd.InnerColumn.xlId,xlIds);
        if (xlzds.size() == 0){
            return new ArrayList<>();
        }
        List<String> routeIds = xlzds.stream().map(ClXlzd::getZdId).collect(Collectors.toList());

        Map<String,Short> orderMap = xlzds.stream().collect(Collectors.toMap(ClXlzd::getZdId,ClXlzd::getXh));
        List<ClZd> stations = findIn(ClZd.InnerColumn.id,routeIds);
        for (ClZd station : stations) {
            station.setRouteOrder(orderMap.get(station.getId()));
        }
        stations.sort(Comparator.comparingInt(ClZd::getRouteOrder));
        return stations;
    }

    @Override
    public void setStationOrder(ClZd station) {
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

    @Override
    public ApiResponse<List<DdClModel>> getBySiteVehicleList(String xlId) {
        List<DdClModel> clZds=entityMapper.getBySiteVehicleList(xlId);
        if(clZds!=null){
            SimpleCondition condition = new SimpleCondition(ClClyxjl.class);
            condition.eq(ClClyxjl.InnerColumn.xlId,xlId);
            Date today = new Date();
            today.setHours(0);
            today.setMinutes(0);
            today.setSeconds(0);
            condition.gte(ClClyxjl.InnerColumn.cjsj,today);
            condition.eq(ClClyxjl.InnerColumn.xlId,xlId);
            condition.and().andNotEqualTo(ClClyxjl.InnerColumn.zt.name(),"off");
            condition.setOrderByClause(ClClyxjl.InnerColumn.cjsj.asc());
            List<ClClyxjl> xlzds = clyxjlService.findByCondition(condition);
            if(xlzds!=null&&xlzds.size()>0){
                for(DdClModel clZd:clZds){
                    //到站车辆
                    long entryCount=0;
                    //出站车辆
                    long exportCount=0;
                    Iterator<ClClyxjl> iter = xlzds.iterator();
                    if(clZd.getVehicleCount()>0){
                        List<ClClyxjlModel> list=new ArrayList<ClClyxjlModel>();
                        while (iter.hasNext()) {
                            ClClyxjl item = iter.next();
                            //判断车辆是否在当前站点
                           if(StringUtils.equals(item.getZdId(),clZd.getZdId())){
                               ClClyxjlModel model=new ClClyxjlModel();
                               model.setCphm(item.getCphm());//车牌号码
                               model.setCjsj(item.getCjsj());//创建时间
                               //车辆出站状态  0未出站 1、已出站
                               String clczzt="1";//
                               if(item.getZdjl()!=null&& clZd.getVehicleScope()<item.getZdjl()){
                                   clczzt="0";
                                   entryCount++;
                               }else{
                                   exportCount++;
                               }
                               model.setClczzt(clczzt);//车辆出站状态  0未出站
                               model.setCjsj(item.getCjsj());
                               model.setZdjl(item.getZdjl());
                               list.add(model);
//                               iter.remove();
                            }
                        }
                        clZd.setVehicleList(list);
                    }
                    clZd.setEntryCount(entryCount);
                    clZd.setExportCount(exportCount);
                }
            }
        }
        ApiResponse<List<DdClModel>> apiResponse=new ApiResponse<List<DdClModel>>();
        apiResponse.setResult(clZds);
        return apiResponse;
    }

    @Override
    public List<ClZd> getNearbyStations(String lng, String lat) {
        // 获取当前机构所有站点
        LimitedCondition condition = new LimitedCondition(ClZd.class);
        List<ClZd> stationList = zdService.findByCondition(condition);
        Map<String,Double> distanceMap = new HashMap<>(stationList.size());
        double maxDistance = 100D;
        List<ClZd> result = new ArrayList<>();
        for (ClZd zd : stationList) {
            double distance = DistanceUtil.getShortDistance(zd.getJd(),zd.getWd(),new Double(lng),new Double(lat));
            distanceMap.put(zd.getId(),distance);
            if (distance < maxDistance){
                result.add(zd);
            }
        }

        if (result.size() != 0){
            return result;
        }
        Map<String,ClZd> stationMap = stationList.stream().collect(Collectors.toMap(ClZd::getId,p->p));

        List<Map.Entry<String,Double>> entryList = new ArrayList<>(distanceMap.entrySet());
        entryList.sort((o1, o2) -> {
            Double v1 = o1.getValue();
            Double v2 = o2.getValue();
            return new Double(v1 - v2).intValue();
        });
        String id = entryList.get(0).getKey();
        result.add(stationMap.get(id));
        return result;
    }

    @Override
    public List<ClXl> getNearbyRoutes(String lng, String lat) {
        List<ClZd> stations = getNearbyStations(lng,lat);
        return getNearbyRoutes(stations);
    }
    public List<ClXl> getNearbyRoutes(List<ClZd> stations) {
        if (CollectionUtils.isEmpty(stations))return new ArrayList<>();
        List<String> stationIds = stations.stream().map(ClZd::getId).collect(Collectors.toList());
        List<ClXlzd> xlzds = xlzdService.findIn(ClXlzd.InnerColumn.zdId,stationIds);
        if (xlzds.size() == 0)return new ArrayList<>();
        List<String> routeIds = xlzds.stream().map(ClXlzd::getXlId).collect(Collectors.toList());
        List<ClXl> routes = xlService.findIn(ClXl.InnerColumn.id,routeIds);
        return routes;
    }

    @Override
    public Map<String, Object> getNearbyRoutesAndStations(String lng, String lat) {
        List<ClZd> zds = getNearbyStations(lng,lat);
        List<ClXl> xls = getNearbyRoutes(zds);
        Map<String,Object> map = new HashMap<>();
        map.put("xls",xls);
        map.put("zds",zds);
        return map;
    }

    @Override
    public ApiResponse<List<ClZd>> getNotBindList(String stationId) {
        LimitedCondition condition = new LimitedCondition(ClZnzp.class);
        List<ClZnzp> znzpList = znzpService.findByCondition(condition);
        List<String> zdIds = znzpList.stream().filter(p->StringUtils.isNotEmpty(p.getZdId())).map(ClZnzp::getZdId).collect(Collectors.toList());
        zdIds.remove(stationId);
        condition = new LimitedCondition(ClZd.class);
        condition.notIn(ClZd.InnerColumn.id,zdIds);
        List<ClZd> stations = findByCondition(condition);
        return ApiResponse.success(stations);
    }

    private Map<String,Object> stationToMap(ClZd zd,Double distance){
        Map<String,Object> station = new HashMap<>();
        station.put("id",zd.getId());
        station.put("name",zd.getMc());
        station.put("distance",distance.intValue());
        station.put("lng",zd.getJd());
        station.put("lat",zd.getWd());
        return station;
    }

    private Map<String,Object> routerToMap(ClXl xl){
        Map<String,Object> router = new HashMap<>();
        router.put("id",xl.getId());
        router.put("name",xl.getXlmc());
        router.put("startTime",xl.getYxkssj());
        router.put("endTime",xl.getYxjssj());
        return router;
    }

    @Override
    public ApiResponse<Map<String, Object>> getStationInfo(String lng, String lat) {
        Map<String,Object> resultMap = new HashMap<>();
        List<Map<String,Object>> nearbyStations = new ArrayList<>(); // 附近站点
        List<Map<String,Object>> nearbyRouters = new ArrayList<>();
        List<Map<String,Object>> otherRouters = new ArrayList<>(); // 其他线路
        resultMap.put("nearbyStations",nearbyStations);
        resultMap.put("nearbyRouters",nearbyRouters);
        resultMap.put("otherRouters",otherRouters);

        // 查找附近站点
        if (StringUtils.isNotEmpty(lng) && StringUtils.isNotEmpty(lat)){
            LimitedCondition condition = new LimitedCondition(ClZd.class);
            List<ClZd> stationList = zdService.findByCondition(condition);
            Map<String,Double> distanceMap = new HashMap<>(stationList.size());
            double maxDistance = 100D;
            for (ClZd zd : stationList) {
                double distance = DistanceUtil.getShortDistance(zd.getJd(),zd.getWd(),new Double(lng),new Double(lat));
                distanceMap.put(zd.getId(),distance);
                if (distance < maxDistance){
                    Map<String,Object> station = stationToMap(zd, distance);
                    nearbyStations.add(station);
                }
            }

            // 如果没有附近站点，则选取最近的一个站点
            if (nearbyStations.size() == 0){
                Map<String,ClZd> stationMap = stationList.stream().collect(Collectors.toMap(ClZd::getId,p->p));
                List<Map.Entry<String,Double>> entryList = new ArrayList<>(distanceMap.entrySet());
                entryList.sort((o1, o2) -> {
                    Double v1 = o1.getValue();
                    Double v2 = o2.getValue();
                    return new Double(v1 - v2).intValue();
                });
                String id = entryList.get(0).getKey();
                Map<String,Object> station = stationToMap(stationMap.get(id),distanceMap.get(id));
                nearbyStations.add(station);
            }
        }

        List<String> nearbyStationIds = new ArrayList<>();
        for (Map<String, Object> station : nearbyStations) {
            String id = (String) station.get("id");
            nearbyStationIds.add(id);
        }

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
        }else{
            nearbyRouterIds = new ArrayList<>();
        }
        for (ClXl router : allOrgRouters) {
            if (nearbyRouterIds.contains(router.getId())){
                Map<String,Object> map = routerToMap(router);
                nearbyRouters.add(map);
            }else{
                Map<String,Object> map = routerToMap(router);
                otherRouters.add(map);
            }
        }
        return ApiResponse.success(resultMap);
    }
//
//    private void setEndStation(List<ClXl> list){
//        List<String> xlIds = list.stream().map(ClXl::getId).collect(Collectors.toList());
//        List<ClZd> stations = zdService.findIn(ClZd.InnerColumn.,xlIds)
//
//    }


    @Override
	public ApiResponse<String> updateEntity(ClZd entity) {
		    ClZd findById = findById(entity.getId());
	        RuntimeCheck.ifNull(findById,"未找到记录");
	        entity.setXgr(getOperateUser());
	        entity.setXgsj(new Date());
	        update(entity);
        redisUtil.deleteRedisKey(deleteZnzpRedisKeyUrl + "/deleteRedisKey","com.ldz.znzp.mapper.ClXlMapper");
		return ApiResponse.success();
	}
}
