package com.ldz.biz.config;

import com.alibaba.fastjson.JSON;
import com.ldz.dao.dwq.mapper.MileDwqMapper;
import com.ldz.dao.dwq.model.GpsBean;
import com.ldz.dao.dwq.model.MileDwq;
import com.ldz.dao.obd.mapper.ObdFaultCodeMapper;
import com.ldz.dao.obd.model.GpsObdMessageBean;
import com.ldz.dao.obd.model.ObdFaultCodeBean;
import com.ldz.service.biz.interfaces.ClService;
import com.ldz.service.biz.interfaces.SbyxsjjlService;
import com.ldz.sys.model.SysZdxm;
import com.ldz.sys.service.ZdxmService;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.commonUtil.SnowflakeIdWorker;
import com.ldz.util.redis.RedisTemplateUtil;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备检测信息监听
 */
public class CheckMessageReceiver implements MessageListener {

    Logger errorLog = LoggerFactory.getLogger("error_info");
    private RedisTemplateUtil redisTemplate;

    private SimpMessagingTemplate websocket;

    private SnowflakeIdWorker idWorker;

    private ObdFaultCodeMapper obdFaultCodeMapper;

    private ZdxmService zdxmService;

    private SbyxsjjlService sbyxsjjlService;
    private ClService clService;

    private MileDwqMapper dwqMapper;


    public CheckMessageReceiver(RedisTemplateUtil redisTemplate, SimpMessagingTemplate websocket,SnowflakeIdWorker idWorker,ObdFaultCodeMapper obdFaultCodeMapper,ZdxmService zdxmService,SbyxsjjlService sbyxsjjlService,ClService clService,MileDwqMapper dwqMapper) {
        this.redisTemplate = redisTemplate;
        this.websocket = websocket;
        this.idWorker = idWorker;
        this.obdFaultCodeMapper = obdFaultCodeMapper;
        this.zdxmService = zdxmService;
        this.sbyxsjjlService = sbyxsjjlService;
        this.clService = clService;
        this.dwqMapper = dwqMapper;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        val redisChannel = redisTemplate.getStringSerializer().deserialize(message.getChannel());
        val eventMessage = redisTemplate.getValueSerializer().deserialize(message.getBody());
        String topic = redisChannel;
        if(StringUtils.equals(topic,"engine_fault")){
            ObdFaultCodeBean data = (ObdFaultCodeBean) eventMessage;
            errorLog.info("engine_fault:"+data.toString());
            data.setId(idWorker.nextId()+"");
            obdFaultCodeMapper.insertSelective(data);
            String code = data.getFaultCode();
            List<String> codes = Arrays.asList(code.split(","));
            SimpleCondition condition = new SimpleCondition(SysZdxm.class);
            condition.eq(SysZdxm.InnerColumn.zdlmdm,"ZDCLK1019");
            List<SysZdxm> zdxmList = zdxmService.findByCondition(condition);
            Map<String, String> collect = zdxmList.stream().collect(Collectors.toMap(SysZdxm::getZddm, SysZdxm::getZdmc));
            HashMap<String,String> map = new HashMap<>();
            codes.forEach(s -> {
                if(StringUtils.isNotBlank(s)){
                    map.put(s,collect.get(s));
                }
            });

            String s = JsonUtil.toJson(map);
            errorLog.info("map:"+s);
            websocket.convertAndSend("/topic/check-"+data.getDeviceId(),data.getDeviceId() + "-"+ s);
        }else if(StringUtils.equals(topic,"obd_info")){
            GpsObdMessageBean gpsObdMessageBean = (GpsObdMessageBean) eventMessage;
            redisTemplate.boundValueOps("obdInfo_"+gpsObdMessageBean.getDeviceId()).set(JsonUtil.toJson(gpsObdMessageBean));
            //websocket.convertAndSend("/topic/obdInfo-"+gpsObdMessageBean.getDeviceId(),JsonUtil.toJson(gpsObdMessageBean));
        }/*else if(StringUtils.equals(topic, "travelReport")){
            String data = (String) eventMessage;
            websocket.convertAndSend("/topic/travelReport",data);
        }*/else if(StringUtils.equals(topic , "dwq_info")) {
            GpsBean gpsBean = (GpsBean) eventMessage;
            redisTemplate.boundValueOps("dwqInfo_"+gpsBean.getDeviceId()).set(JsonUtil.toJson(gpsBean));
        }else if(StringUtils.equals(topic, "travelData")) {
            // 点熄火事件改为tic-dwq处理
//            TravelData data = (TravelData) eventMessage;
//            // 点火事件存储
//
//            List<ClCl> cls = clService.findEq(ClCl.InnerColumn.zdbh, data.getDeviceId());
//            if(CollectionUtils.isNotEmpty(cls)) {
//                ClCl clCl = cls.get(0);
//                ClSbyxsjjl sbyxsjjl = new ClSbyxsjjl();
//                String startTime = data.getStartTime();
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                sbyxsjjl.setId(idWorker.nextId() + "");
//                try {
//                    sbyxsjjl.setCjsj(dateFormat.parse(startTime));
//                    sbyxsjjl.setZdbh(data.getDeviceId());
//                    sbyxsjjl.setSjlx("50");
//                    sbyxsjjl.setJd(new BigDecimal(Double.parseDouble(data.getStartLng())));
//                    sbyxsjjl.setWd(new BigDecimal(Double.parseDouble(data.getStartLat())));
//                    sbyxsjjl.setSjjb("10");
//                    sbyxsjjl.setSjxm(clCl.getSjxm());
//                    sbyxsjjl.setCx(clCl.getCx());
//                    sbyxsjjl.setCph(clCl.getCph());
//                    sbyxsjjlService.save(sbyxsjjl);
//                } catch (ParseException e) {
//                    errorLog.info("点火事件存储异常",e);
//                }
//                // 熄火事件
//                String endTime = data.getEndTime();
//                ClSbyxsjjl sbyxsjjl1 = new ClSbyxsjjl();
//                sbyxsjjl1.setId(idWorker.nextId() + "");
//                try {
//                    sbyxsjjl1.setCjsj(dateFormat.parse(endTime));
//                    sbyxsjjl1.setZdbh(data.getDeviceId());
//                    sbyxsjjl1.setSjlx("60");
//                    sbyxsjjl1.setJd(new BigDecimal(Double.parseDouble(data.getEndLng())));
//                    sbyxsjjl1.setWd(new BigDecimal(Double.parseDouble(data.getEndLat())));
//                    sbyxsjjl1.setSjjb("10");
//                    sbyxsjjl1.setSjxm(clCl.getSjxm());
//                    sbyxsjjl1.setCx(clCl.getCx());
//                    sbyxsjjl1.setCph(clCl.getCph());
//                    sbyxsjjlService.save(sbyxsjjl1);
//                } catch (ParseException e) {
//                    errorLog.info("熄火事件存储异常",e);
//                }
//            }
        }else if(StringUtils.equals(topic, "mile_dwq")){
            String dwq = (String) eventMessage;
            MileDwq mileDwq = JSON.parseObject(dwq, MileDwq.class);
            mileDwq.setId(idWorker.nextId() + "");
            dwqMapper.insert(mileDwq);
        }
    }
}
