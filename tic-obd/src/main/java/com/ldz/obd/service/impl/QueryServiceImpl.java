package com.ldz.obd.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.ldz.obd.bean.PackageData;
import com.ldz.obd.service.QueryService;
import com.ldz.obd.service.codec.MsgDecoder;
import com.ldz.obd.util.NettyUtil;
import com.ldz.util.bean.GpsObdMessageBean;
import com.ldz.util.bean.ObdFaultCodeBean;
import com.ldz.util.bean.ObdTravelItineraryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/11.
 */
@Service
public class QueryServiceImpl implements QueryService {
    Logger accessLog = LoggerFactory.getLogger("access_info");
    Map<String, String> params = new HashMap<String, String>(){{
        put("0","P0");
        put("1","P1");
        put("2","P2");
        put("3","P3");
        put("4","C0");
        put("5","C1");
        put("6","C2");
        put("7","C3");
        put("8","B0");
        put("9","B1");
        put("A","B2");
        put("B","B3");
        put("C","U0");
        put("D","U1");
        put("E","U2");
        put("F","U3");
    }};
    MsgDecoder msgDecoder=new MsgDecoder();
    @Autowired
    private NettyUtil nettyUtil;

    @Autowired
    private RedisTemplate redisDao;

    @Value("${gpsObdMessage-key:gpsObdMessage_}")
    private String gpsObdMessage;

    @Value("${obdFaultCodeList-key:obdFaultCodeList_}")
    private String obdFaultCodeListKey;

    @Value("${obdTravelItineraryList-key:obdTravelItineraryList_}")
    private String obdTravelItineraryListKey;

    /**
     * 请求/上传 GPS+OBD 混合信息
     * @param msg
     */
    public void getGpsObdMessage(PackageData msg){
        GpsObdMessageBean obd=new GpsObdMessageBean();
        byte[] bodyBytes=msg.getBodyBytes();

        /**
         * 数据类型 1 0x00 或者 0x01,其中 0x00 表示盲区数据，0x01 表示实时数据
         * 00表示盲区 10 表示实时数据 20未知状态
         */
        int value=msgDecoder.parseIntFromBytes(bodyBytes, 0, 1);
        String dataType="20";
        if(value==0x00 ){
            dataType="00";
        }else if(value==0x01 ){
            dataType="10";
        }
        obd.setDataType(dataType);

        /**
         * 行程ID 长度：2    该数据属于哪个
         */
        obd.setSchedulingCode(msgDecoder.parseIntFromBytes(bodyBytes,1,2)+"");
        /**
         * 创建日期 长度：3 日月年表示.年省略”20”。比如 2014 年 4 月 28 日为 0x28 0x04 0x14 BCD 码
         */
        String creatorDate=msgDecoder.parseBcdStringFromBytes(bodyBytes,3,3);

        obd.setCreatorDate("20"+creatorDate.substring(4,6)+creatorDate.substring(2,4)+creatorDate.substring(0,2));
        /**
         * 创建时间 长度：3 时分秒表示,为格林威治时间。比如 9 点 0 分 4 秒为 0x09 0x00 0x04 BCD 码
         */
        obd.setCreatortime(msgDecoder.parseBcdStringFromBytes(bodyBytes,6,3));
        /**
         * 纬度  长度:4 实际纬度乘以 10000 的值,DDMM.MMMM 格式，比如 2233.4567     则上传 0x22 0x33 0x45 0x67
         * 纬度。得到的数据需要进行度分转换。22438816/10000=22度43.8816分(43.8816/60)=22.73136度
         */
        String latitude=msgDecoder.parseBcdStringFromBytes(bodyBytes,9,4);
        obd.setLatitude(latitude.substring(0,2)+"."+(Double.parseDouble(latitude.substring(2))/10000/60));
        /**
         * 经度 4.5   实际经度乘以 10000 的值,DDDMM.MMMM 格式11334.5678 则上传 0x11 0x33 0x45 0x67 0x
         * //经度+位指示一共5个字节，最后一个字节97,7表示的是位状态数据
         //经度得到的数据，只要前9个字符，数据需要进行度分转换。113491239/10000=113度49.1239分(49.1239/60)=113.8187316666666667度
         //最后一个字节，表示的是位指示
         */
        String longitude=msgDecoder.parseBcdStringFromBytes(bodyBytes,13,5);
        obd.setLongitude(longitude.substring(0,3)+"."+(Double.parseDouble(longitude.substring(3,9))/10000/60));

        /**
         * 位指示 GPS 是否定位,东西经及南北纬等.请参见
         * 这里不清楚
         */
        String indication=longitude.substring(9);
        /**
         * 速度 1 当前 GPS 速度,以 km/h 为单位
         */
        obd.setGpsTempo(msgDecoder.parseIntFromBytes(bodyBytes,18,1)+"");
        /**
         * 方向 长度：1 当前方向,以度为单位,设备在上传对其进行了除 2 处理,所以,系统需 乘以 2,则还原为实际方向角度
         * 公式 *2
         */
        obd.setDirection(""+(msgDecoder.parseIntFromBytes(bodyBytes,19,1)*2));
        /**
         * GPS 卫星个数 长度：1 当前 GPS 卫星的个数
         */
        obd.setGpsCount(""+msgDecoder.parseIntFromBytes(bodyBytes,20,1));
        /**
         * GSM 信号质量 长度：1 表明当前 GSM 的信号强弱,GSM 信号强度最大为 31
         */
        obd.setGpsSignalIntensity(""+msgDecoder.parseIntFromBytes(bodyBytes,21,1));
        /**
         * 里程 长度：4 当设备能读到原车里程的时候，该里程为仪表盘里程。否则为标定的 里程 + OBD 累加里程,单位 KM
         */
        obd.setMileage(""+msgDecoder.parseIntFromBytes(bodyBytes,22,4));
            /**
             * 设备状态 长度：4 4 个字节，其中第一个字节暂时保留，第二个字节的定义如(表十)
             *  所示，第三个字节的定义如(表十一)所示，第四个字节(表十二)所示 暂时没有处理
             */
//        msgDecoder.parseIntFromBytes(bodyBytes,26,4)

        /**
         * 负荷计算值。 长度：1  先进行10进制转换，然后使用公式：BYTE*100/255（165*100/255）=64（取整）
         */
        double loadVlue=msgDecoder.parseIntFromBytes(bodyBytes,30,1);
        obd.setLoadValue(""+(loadVlue*100/255));
        /**
         * 冷却液温度  长度：1 BYTE – 40，单位℃
         */
        obd.setCoolantTemperature(""+(msgDecoder.parseIntFromBytes(bodyBytes,31,1)-40));
        /**
         * 发动机转速  长度：2 ((BYTE1*256)+BYTE2)/4，(如 0x30 0x08 则 0x30 表示 BYTE1，
         0x08 表示 BYTE2，以下类同，不再重复)，单位 RPM
         */
        double engineSpeedByte1=msgDecoder.parseIntFromBytes(bodyBytes,32,1);
        double engineSpeedByte2=msgDecoder.parseIntFromBytes(bodyBytes,33,1);
        obd.setEngineSpeed(""+((engineSpeedByte1*256)+engineSpeedByte2)/4);
        /**
         * OBD 车速 长度：1 单位 KM/H
         */
        obd.setObdSpeed(""+msgDecoder.parseIntFromBytes(bodyBytes,34,1));
        /**
         * 点火提前角 1 BYTE – 64，单位
         */
        int ignitionAngleByte1=msgDecoder.parseIntFromBytes(bodyBytes,35,1);
        obd.setIgnitionAngle(""+(ignitionAngleByte1-64));

        /**
         * 进气歧管绝对压力 1 BYTE，单位 kpa
         */
        obd.setIntakePressure(""+msgDecoder.parseIntFromBytes(bodyBytes,36,1));

        /**
         * 控制模块电压 1 BYTE/10，单位 V
         */
        double controlVoltage=msgDecoder.parseIntFromBytes(bodyBytes,37,1);
        obd.setControlVoltage(""+(controlVoltage/10));
        /**
         * 进气温度 1 BYTE-40，单位℃
         */
        int intakeTemperature=msgDecoder.parseIntFromBytes(bodyBytes,38,1);
        obd.setIntakeTemperature(""+intakeTemperature);
        /**
         * 空气流量 2 ((BYTE1*256)+BYTE2)/100，单位 g/s
         */
        double intakeFlux1=msgDecoder.parseIntFromBytes(bodyBytes,39,1);
        double intakeFlux2=msgDecoder.parseIntFromBytes(bodyBytes,40,1);
        obd.setIntakeFlux(""+((intakeFlux1*256)+intakeFlux2)/100);
        /**
         * 节气门相对位置 1 BYTE*100/255，单位%
         */
        double jqmxdwz=msgDecoder.parseIntFromBytes(bodyBytes,41,1);
        obd.setJqmxdwz(""+(jqmxdwz*100/255));
        /**
         * 长期燃油修正 1 (BYTE1-128)*100/128，单位%
         */
        double chryxz=msgDecoder.parseIntFromBytes(bodyBytes,42,1);
        obd.setChryxz(""+((chryxz-128)*100/128));
        /**
         * 空燃比系数 2 ((BYTE1*256)+BYTE2)*0.0000305
         */
        double klbxs1=msgDecoder.parseIntFromBytes(bodyBytes,43,1);
        double klbxs2=msgDecoder.parseIntFromBytes(bodyBytes,44,1);
        obd.setKlbxs(""+((klbxs1*256)+klbxs2)*0.0000305);
        /**
         * 节气门绝对位置 1 BYTE*100/255，单位%
         */
        double jqmxjwz=msgDecoder.parseIntFromBytes(bodyBytes,45,1);
        obd.setJqmxjwz(""+(jqmxjwz*100/255));
        /**
         * 燃油压力 1 BYTE*3,单位 kpa
         */
        obd.setRyyl(""+msgDecoder.parseIntFromBytes(bodyBytes,46,1)*3);
        /**
         * 瞬间油耗 L/H 2 ((BYTE1*256)+BYTE2)*0.1,单位 L/H
         */
        double sjyh1=msgDecoder.parseIntFromBytes(bodyBytes,47,1);
        double sjyh2=msgDecoder.parseIntFromBytes(bodyBytes,48,1);
        obd.setSjyh(""+((sjyh1*256)+sjyh2)*0.1);

        /**
         * 剩余油量 2 if((BYTE1&0X80)==0X80)
         ((BYTE1-0X80)*256)+BYTE2)*0.1 单位%
         Else (BYTE1*256+BYTE2)*0.1 单位 L
         */
        double syyl;
        if((bodyBytes[48]&0X80)==0X80){
            syyl = (((bodyBytes[49]-0X80)*256)+bodyBytes[50])*0.1;
        }else{
            syyl = (bodyBytes[49] * 256 + bodyBytes[50]) * 0.1;
        }
        obd.setSyyl(""+syyl);

        /**
         *基站 8 4 字节运营商代码(IMSI 前 5 位),2 字节 LAC,2 字节 CELL
         */
//        String jz=msgDecoder.parseIntFromBytes(bodyBytes,51,8);
        /**
         *耗油量 4 OBD 累计耗油量,单位 L
         */
        obd.setHyl(""+msgDecoder.parseIntFromBytes(bodyBytes,59,4));
        /**
         * 流水号 1 从 0 -255 一直循环累加，可以用来检测是否有包丢失
         */
        obd.setSeq(""+msgDecoder.parseIntFromBytes(bodyBytes,63,1));
        accessLog.debug("    请求/上传 GPS+OBD 混合信息:返回的结果："+obd.toString());
        System.out.println(obd.toString());
        redisDao.opsForValue().set(gpsObdMessage+msg.getEquipmentId(),obd);//写入redis
//        ChannelHandlerContext ctx=msg.getCtx();
//        nettyUtil.sendData(ctx,obd.toString());
    }

    /**
     * 行程报告上传
     * @param msg
     */
    public void uploadTravelItineraryMessage(PackageData msg){
        ObdTravelItineraryBean obd=new ObdTravelItineraryBean();
        byte[] bodyBytes=msg.getBodyBytes();
        /**
         * 1、行程ID 长度：2 表示该报告属于哪个行程
         */
        obd.setSchedulingCode(msgDecoder.parseIntFromBytes(bodyBytes,0,2)+"");
        /**
         * 2、点火日期  长度：3		日月年表示.年省略”20”，BCD 码表示
         */
        String ignitionDate=msgDecoder.parseBcdStringFromBytes(bodyBytes,2,3);
        obd.setIgnitionDate("20"+ignitionDate.substring(4,6)+ignitionDate.substring(2,4)+ignitionDate.substring(0,2));
        /**
         * 3、点火时间		长度：3		时分秒表示,为格林威治时间。BCD 码表示
         */
        obd.setIgnitionTime(msgDecoder.parseBcdStringFromBytes(bodyBytes,5,3));

        /**
         * 4、熄火日期		长度：3  同上(点火时间和熄火时间为 GPS 时间，下面的时间为设备定时器累加的时间，单位 S，每次点火熄火时间相减的时间和下面累加的时间可能有几 S 的误差)
         */
        String flameoutDate=msgDecoder.parseBcdStringFromBytes(bodyBytes,8,3);
        obd.setFlameoutDate("20"+flameoutDate.substring(4,6)+flameoutDate.substring(2,4)+flameoutDate.substring(0,2));

        /**
         * 5、熄火时间		长度：3		同上
         */
        obd.setFlameoutTime(msgDecoder.parseBcdStringFromBytes(bodyBytes,11,3));

        /**
         * 6、该次行驶时间		长度：2		单位 S，该时间为设备定时器累加的时间，当 GPS 时间无效的时候，可用该时间显示行驶的时间
         */
        obd.setTimeConsuming(""+msgDecoder.parseIntFromBytes(bodyBytes,14,2)) ;

        /**
         * 7、该次耗油量		长度：4		单位 ML
         */
        obd.setOilWear(""+msgDecoder.parseIntFromBytes(bodyBytes,16,4));

        /**
         * 8、该次里程		长度：4		单位 M
         */
        obd.setMileage(""+msgDecoder.parseIntFromBytes(bodyBytes,20,4));

        /**
         * 9、最高速度		长度：1		KM/H
         */
        obd.setMaximumSpeed(""+msgDecoder.parseIntFromBytes(bodyBytes,24,1));

        /**
         * 10、发动机最高转速		长度：2
         * ((BYTE1*256)+BYTE2)/4,单位 rpm
         */
        double maximumRev1=msgDecoder.parseIntFromBytes(bodyBytes,25,1);
        double maximumRev2=msgDecoder.parseIntFromBytes(bodyBytes,26,1);
        obd.setMaximumRev(""+((maximumRev1*256)+maximumRev2)/4);

        /**
         * 11、冷却液最高温度		长度：1		BYTE - 40
         */
        int maximumTemperature=msgDecoder.parseIntFromBytes(bodyBytes,27,1);
        obd.setMaximumTemperature(""+(maximumTemperature-64));

        /**
         * 12、急加速次数		长度：1		次
         */
        obd.setRapidAccelerationCount(""+msgDecoder.parseIntFromBytes(bodyBytes,28,1));

        /**
         * 13、急减速次数		长度：1		次
         */
        obd.setRapidDecelerationCount(""+msgDecoder.parseIntFromBytes(bodyBytes,29,1));

        /**
         * 14、超速行驶时间(>120km/h)		长度：2		单位 S
         */
        obd.setOverspeedTime(""+msgDecoder.parseIntFromBytes(bodyBytes,30,2)) ;

        /**
         * 15、超速行驶的里程		长度：4		单位 M
         */
        obd.setOverspeedMileage(""+msgDecoder.parseIntFromBytes(bodyBytes,32,4));

        /**
         * 16、超速行驶的耗油量		长度：4		单位 ML.
         */
        obd.setOverspeedOilWear(""+msgDecoder.parseIntFromBytes(bodyBytes,36,4));

        /**
         * 17、高速行驶的时间(80km/h-120km/h)		长度：2		S
         */
        obd.setHighspeedTime(""+msgDecoder.parseIntFromBytes(bodyBytes,40,2));

        /**
         * 18、高速行驶的里程		长度：4		M
         */
        obd.setHighspeedMileage(""+msgDecoder.parseIntFromBytes(bodyBytes,42,4));

        /**
         * 19、高速行驶的耗油量		长度：4		单位 ML.
         */
        obd.setHighspeedOilWear(""+msgDecoder.parseIntFromBytes(bodyBytes,46,4));

        /**
         * 20、中速行驶的时间(40km/h-80km/h)		长度：2		S
         */
        obd.setIntermediateTime(""+msgDecoder.parseIntFromBytes(bodyBytes,50,2));

        /**
         * 21、中速行驶的里程		长度：4		M
         */
        obd.setIntermediateMileage(""+msgDecoder.parseIntFromBytes(bodyBytes,52,4));

        /**
         * 22、中速行驶的耗油量		长度：4		单位 ML.
         */
        obd.setIntermediateOilWear(""+msgDecoder.parseIntFromBytes(bodyBytes,56,4));

        /**
         * 23、低速行驶的时间(1km/h-40km/h)		长度：2		S
         */
        obd.setLowspeedTime(""+msgDecoder.parseIntFromBytes(bodyBytes,60,2));

        /**
         * 24、低速行驶的里程		长度：4		M
         */
        obd.setLowspeedMileage(""+msgDecoder.parseIntFromBytes(bodyBytes,62,4));

        /**
         * 25、低速行驶的耗油量		长度：4		单位 ML.
         */
        obd.setLowspeedOilWear(""+msgDecoder.parseIntFromBytes(bodyBytes,66,4));

        /**
         * 26、怠速的时间		长度：2		S
         */
        obd.setIdlingTime(""+msgDecoder.parseIntFromBytes(bodyBytes,70,2));

        /**
         * 27、怠速的耗油量		长度：4		ML
         */
        obd.setIdlingOilWear(""+msgDecoder.parseIntFromBytes(bodyBytes,72,4));

        /**
         * 28、急转弯次数		长度：1		次
         */
        obd.setJzwCount(""+msgDecoder.parseIntFromBytes(bodyBytes,76,1));

        redisDao.opsForList().leftPushAll(obdTravelItineraryListKey+msg.getEquipmentId(), obd);

        List<ObdTravelItineraryBean> list=redisDao.opsForList().range(obdTravelItineraryListKey+msg.getEquipmentId(),0,-1);
        for(ObdTravelItineraryBean l:list){
            System.out.println(l.toString());
        }
    }

    /**
     * 发动机故障码上传
     * 0x01 0x23 0x01 为指令内容，每个故障码由 3 个字节组成，所以该指令的内容长度一定是 3
     * 个倍数。第 1 字节为故障码高位,第 2 字节为故障码低位,第 3 字节 01 表示已决，02 表示未
     * 决，该字节可以不理会。
     * @param msg
     */
    @Override
    public void uploadFaultCodeMessage(PackageData msg) {
        byte[] bodyBytes=msg.getBodyBytes();
        int bodyLength=msg.getBodyLength();
        for (int i=0;i<bodyLength/3;i++){
            ObdFaultCodeBean obj=new ObdFaultCodeBean();
            String faultCode1=msgDecoder.parseBcdStringFromBytes(bodyBytes,i+0,1);
            String faultCode2=msgDecoder.parseBcdStringFromBytes(bodyBytes,i+1,1);
            /**
             * 第 3 字节 01 表示已决，02 表示未决
             * 故障处理状态
             * 10 已解决 20未解决  00未知状态
             */
            int value=msgDecoder.parseIntFromBytes(bodyBytes, i+2, 1);
            String faultType="00";
            if(value==0x01 ){
                faultType="10";
            }else if(value==0x02 ){
                faultType="20";
            }
            obj.setFaultType(faultType);
            obj.setCreationTime(new Date());
            obj.setFaultCode(params.get(faultCode1.substring(0,1).toUpperCase())+faultCode1.substring(1)+faultCode2);
            redisDao.opsForList().leftPushAll(obdFaultCodeListKey+msg.getEquipmentId(), obj);
        }

//        System.out.println(redisDao.opsForList().range(obdFaultCodeListKey+msg.getEquipmentId(),0,-1).toString());

        List<ObdFaultCodeBean> lists=redisDao.opsForList().range(obdFaultCodeListKey+msg.getEquipmentId(),0,-1);
        for(ObdFaultCodeBean l:lists){
            System.out.println(l.toString());
        }

    }

    /**
     * 设备在线(开机)状态
     * @param msg
     */
    @Override
    public void deviceOnLineType(PackageData msg) {
        Map<String,String> map=new HashMap<String,String>();
        byte[] bodyBytes=msg.getBodyBytes();
        double onLineType=msgDecoder.parseIntFromBytes(bodyBytes,0,1);
        if(onLineType==0x01){//在线

        }else if(onLineType==0x00){//离线

        }
        map.put("onLineType",onLineType+"");
        //唤醒方式 0、上电 1、电压波动唤醒 2、电话短信唤醒  3、震动唤醒  4、RTC唤醒（定时启动）
        double onLineTypeMode=msgDecoder.parseIntFromBytes(bodyBytes,1,1);
        map.put("onLineTypeMode",onLineTypeMode+"");
        String creatorDate=msgDecoder.parseBcdStringFromBytes(bodyBytes,2,3);
        String creatortime=msgDecoder.parseBcdStringFromBytes(bodyBytes,5,3);
        map.put("creatorDate","20"+creatorDate.substring(4,6)+creatorDate.substring(2,4)+creatorDate.substring(0,2)+" "+creatortime);
        redisDao.opsForValue().set("gpsObdOnLine_"+msg.getEquipmentId(),map);//写入redis
        redisDao.opsForValue().set("gpsObdOnLineJSON_"+msg.getEquipmentId(),JSONUtils.toJSONString(map));//写入redis
        System.out.println(JSONUtils.toJSONString(map));
    }
}
