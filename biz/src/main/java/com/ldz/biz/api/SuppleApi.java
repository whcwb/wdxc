package com.ldz.biz.api;



import com.ldz.dao.biz.bean.EventModel;
import com.ldz.dao.biz.bean.ResModel;
import com.ldz.dao.biz.bean.SearchModel;
import com.ldz.dao.biz.bean.SuppleModel;
import com.ldz.dao.biz.mapper.ClGpsLsMapper;
import com.ldz.dao.biz.mapper.ClXcMapper;
import com.ldz.dao.biz.mapper.ClyyMapper;
import com.ldz.dao.biz.model.ClGps;
import com.ldz.dao.biz.model.ClGpsLs;
import com.ldz.dao.biz.model.ClXc;
import com.ldz.dao.biz.model.Clyy;
import com.ldz.service.biz.interfaces.GpsService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.commonUtil.HttpUtil;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.commonUtil.SnowflakeIdWorker;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.util.gps.Gps;
import com.ldz.util.gps.PositionUtil;
import com.ldz.util.redis.RedisTemplateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pub")
public class SuppleApi {

    @Autowired
    private ClXcMapper clXcMapper;
    @Autowired
    private ClGpsLsMapper clGpsLsMapper;
    @Autowired
    private GpsService gpsService;
    @Autowired
    private ClyyMapper clyyMapper;
    @Autowired
    private SnowflakeIdWorker idWorker;
    @Autowired
    private RedisTemplateUtil redis;


    @RequestMapping(value = "/supple",method = {RequestMethod.GET,RequestMethod.POST})
    public ApiResponse<String> save(String zdbh, String time, @RequestParam(defaultValue = "0") String day){
        DateTimeFormatter pattern = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        RuntimeCheck.ifBlank(zdbh, "请传入终端编号");
        RuntimeCheck.ifBlank(time, "请上传开始时间");
        String startTime = time + " 00:00:00";
        String endTime = "";
        if(StringUtils.equals(day,"0")){
            endTime += time + " 23:59:59";
        }else{
            endTime = DateTime.parse(startTime, pattern).plusDays(Integer.parseInt(day)).toString("yyyy-MM-dd HH:mm:ss");
        }
        // 首先查询当前时间段的所有 gps 点
        Map<String,String> map = new HashMap<>();
        map.put("imei", zdbh);
        map.put("start_time",startTime);
        map.put("end_time", endTime);
        String url = "http://tool.fep.bidostar.com/1/debug/track.json";
        String post = HttpUtil.post(url, map);
        ResModel resModel = JsonUtil.toBean(post, ResModel.class);

        List<SuppleModel> list = resModel.getList();
        list = list.stream().filter(model -> StringUtils.startsWith(model.getGps_time(), time)).collect(Collectors.toList());
        endTime = time + " 23:59:59";
        if(CollectionUtils.isNotEmpty(list)){
             // 结束时间
            // 删除 行程表  gpsLs表  以及 鹰眼表的数据
            clXcMapper.delXc(zdbh, startTime, endTime);

            clGpsLsMapper.delGps(zdbh, DateTime.parse(startTime,pattern).toDate(),DateTime.parse(endTime,pattern).toDate());

            clyyMapper.removeLists(zdbh,startTime,endTime);
            List<ClGpsLs> gpsLsList = new ArrayList<>();
            List<Clyy> yys = new ArrayList<>();
            // 存储所有的gps点
            for (SuppleModel mo : list) {
                ClGpsLs gpsLs = new ClGpsLs();
                gpsLs.setCjsj(DateTime.parse(mo.getGps_time(),pattern).toDate());
                gpsLs.setFxj(new BigDecimal(mo.getDirect()));
                Gps gps84_to_gcj02 = PositionUtil.gps84_To_Gcj02(((double) Integer.parseInt(mo.getLatitude())) / 1000000, ((double) Integer.parseInt(mo.getLongitude())) / 1000000);
                Gps gps = PositionUtil.gcj02_To_Bd09(gps84_to_gcj02.getWgLat(), gps84_to_gcj02.getWgLon());
                gpsLs.setBdjd(new BigDecimal(gps.getWgLon()));
                gpsLs.setBdwd(new BigDecimal(gps.getWgLat()));
                gpsLs.setId(idWorker.nextId() + "");
                gpsLs.setJd(new BigDecimal(gps.getWgLon()));
                gpsLs.setWd(new BigDecimal(gps.getWgLat()));
                gpsLs.setYxsd(new Integer(Integer.parseInt(mo.getSpeed())/10).toString());
                gpsLs.setZdbh(zdbh);
                gpsLsList.add(gpsLs);
                Clyy clyy = new Clyy();
                clyy.setId(idWorker.nextId() + "");
                clyy.setDirection(gpsLs.getFxj()+"");
                clyy.setLatitude(gpsLs.getBdwd());
                clyy.setLongitude(gpsLs.getBdjd());
                clyy.setLoc_time(mo.getGps_time());
                clyy.setSpeed(new BigDecimal(gpsLs.getYxsd()));
                clyy.setZdbh(zdbh);
                yys.add(clyy);
            }
            if(CollectionUtils.isNotEmpty(gpsLsList)){
                clGpsLsMapper.insertList(gpsLsList);
            }
            if(CollectionUtils.isNotEmpty(yys)){
                clyyMapper.insertList(yys);
            }
            // 查询点火事件 和熄火事件 判断
            url = "http://tool.fep.bidostar.com/1/event/search.json";
            map.put("type","203");
            post = HttpUtil.post(url, map);
            SearchModel toBean = JsonUtil.toBean(post, SearchModel.class);
            // 熄火事件
            map.put("type", "204");
            post = HttpUtil.post(url, map);
            SearchModel searchModel = JsonUtil.toBean(post, SearchModel.class);
            // 以点火事件为主  找到下一个对应的熄火
            List<EventModel> eventModels = toBean.getList();
            List<EventModel> models = searchModel.getList();
            eventModels.sort(Comparator.comparing(EventModel::getAlarm_time));
            models.sort(Comparator.comparing(EventModel::getAlarm_time));
            while (CollectionUtils.isNotEmpty(eventModels)){
                EventModel remove = eventModels.remove(0);
                String xcKssj = remove.getAlarm_time();
                String xcJssj = xcKssj;
                for (EventModel eventModel : models) {
                    if(eventModel.getAlarm_time().compareTo(xcKssj) > 0){
                        xcJssj = eventModel.getAlarm_time();
                        break;
                    }
                }
                DateTime parse = DateTime.parse(xcKssj,pattern);
                DateTime parse1 = DateTime.parse(xcJssj,pattern);
                if(parse1.getMillis() - parse.getMillis() >= 60 * 1000){
                    ClXc xc = new ClXc();
                    xc.setClZdbh(zdbh);
                    xc.setId(idWorker.nextId() + "");
                    xc.setXcKssj(xcKssj);
                    xc.setXcJssj(xcJssj);
                    String finalXcJssj = xcJssj;
                    List<ClGpsLs> collect = gpsLsList.stream().filter(model ->
                            model.getCjsj().compareTo(DateTime.parse(xcKssj, pattern).toDate()) >= 0 && model.getCjsj().compareTo(DateTime.parse(finalXcJssj, pattern).toDate()) <= 0
                    ).collect(Collectors.toList());
                    /*ClGpsLs startOne = clGpsLsMapper.getStartOne(zdbh, DateTime.parse(xcKssj, pattern).toDate());

                    ClGpsLs lastOne = clGpsLsMapper.getLastOne(zdbh, DateTime.parse(xcJssj, pattern).toDate());*/
                    if(CollectionUtils.isNotEmpty(collect)){
                        ClGpsLs startOne = collect.get(0);
                        ClGpsLs lastOne = collect.get(collect.size() - 1);
                        String staAddress = getAddress(startOne.getBdwd().toString(), startOne.getBdjd().toString());
                        String endAddress = getAddress(lastOne.getBdwd().toString(), lastOne.getBdjd().toString());
                        xc.setStartAddress(staAddress);
                        xc.setEndAddress(endAddress);
                        long l = parse1.getMillis() - parse.getMillis();
                        xc.setXcSc(l+"");
                        String startEnd = startOne.getBdjd() + "-" + startOne.getBdwd() + "," + lastOne.getBdjd() + "-" + lastOne.getBdwd()  + "," + xcJssj;
                        xc.setXcStartEnd(startEnd);
                        clXcMapper.insert(xc);
                    }
                }

            }

            // 更新实时点位 找到最后一个点与 当前redis 中的实时点位比较 如果时间较新 ， 更新实时点位 否则不更新
            ClGpsLs gpsLs = gpsLsList.get(gpsLsList.size() - 1);
            List<ClGps> gps = gpsService.findEq(ClGps.InnerColumn.zdbh, zdbh);
            if(CollectionUtils.isNotEmpty(gps)){
                ClGps clGps = gps.get(0);
                Date cjsj = clGps.getCjsj();
                if(cjsj.getTime() < gpsLs.getCjsj().getTime()){
                    clGps.setBdjd(gpsLs.getBdjd());
                    clGps.setBdwd(gpsLs.getBdwd());
                    clGps.setGxsj(gpsLs.getCjsj());
                    clGps.setFxj(gpsLs.getFxj());
                    clGps.setYxsd(gpsLs.getYxsd());
                    clGps.setJd(gpsLs.getBdjd());
                    clGps.setWd(gpsLs.getBdwd());
                    Gps gcj02 = PositionUtil.bd09_To_Gcj02(gpsLs.getBdwd().doubleValue(), gpsLs.getBdjd().doubleValue());
                    clGps.setGgjd(new BigDecimal(gcj02.getWgLon()));
                    clGps.setGgwd(new BigDecimal(gcj02.getWgLat()));
                    gpsService.update(clGps);
                    redis.boundValueOps(ClGps.class.getSimpleName() + zdbh).set(JsonUtil.toJson(clGps));
                }
            }



        }else{
            return ApiResponse.fail("未查询到数据");
        }


        // Object list = object.get("list");

        /*List<String> strings = list.toJavaList(String.class);
        JSONObject jsonObject = JSON.parseObject(strings.get(0));*/
       // String id = (String) jsonObject.get("id");
        return ApiResponse.success();
    }

    private static String getAddress(String lat,String lng){
        String url = "http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location="+lat+","+lng+"&output=json&ak=VSMwIsUZjwmXvLQ8Rj5DrEx9PCuGqG2C";
        String res = HttpUtil.get(url);
        int index = res.indexOf("formatted_address");
        if (index < 0) return "";
        String address = res.substring(index + 20);
        address = address.substring(0,address.indexOf("\""));
        return address;
    }




}
