package com.ldz.dwqjt.jt808.service.codec;

import com.ldz.dao.dwq.model.TravelData;
import com.ldz.dwqjt.jt808.util.BCD8421Operater;
import com.ldz.dwqjt.jt808.util.BitOperator;
import com.ldz.dwqjt.jt808.util.Consts;
import com.ldz.dwqjt.jt808.vo.PackageData;
import com.ldz.dwqjt.jt808.vo.req.BatchLocationInfoUploadMsg;
import com.ldz.dwqjt.jt808.vo.req.EventMsg;
import com.ldz.dwqjt.jt808.vo.req.LocationInfoUploadMsg;
import com.ldz.dwqjt.jt808.vo.req.TerminalRegisterMsg;
import com.ldz.util.commonUtil.DateUtils;
import io.netty.buffer.ByteBufUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Component
public class MsgDecoder {

	private static final Logger log = LoggerFactory.getLogger(MsgDecoder.class);

	private BitOperator bitOperator;
	private BCD8421Operater bcd8421Operater;

	public MsgDecoder() {
		this.bitOperator = new BitOperator();
		this.bcd8421Operater = new BCD8421Operater();
	}

	public PackageData bytes2PackageData(byte[] data) {
		PackageData ret = new PackageData();

		// 0. 终端套接字地址信息
		// ret.setChannel(msg.getChannel());

		// 1. 16byte 或 12byte 消息头
		PackageData.MsgHeader msgHeader = this.parseMsgHeaderFromBytes(data);
		ret.setMsgHeader(msgHeader);

		int msgBodyByteStartIndex = 12;
		// 2. 消息体
		// 有子包信息,消息体起始字节后移四个字节:消息包总数(word(16))+包序号(word(16))
		if (msgHeader.isHasSubPackage()) {
			msgBodyByteStartIndex = 16;
		}

		byte[] tmp = new byte[msgHeader.getMsgBodyLength()];
		System.arraycopy(data, msgBodyByteStartIndex, tmp, 0, tmp.length);
		ret.setMsgBodyBytes(tmp);

		// 3. 去掉分隔符之后，最后一位就是校验码
		// int checkSumInPkg =
		// this.bitOperator.oneByteToInteger(data[data.length - 1]);
		int checkSumInPkg = data[data.length - 1];
		int calculatedCheckSum = this.bitOperator.getCheckSum4JT808(data, 0, data.length - 1);
		ret.setCheckSum(checkSumInPkg);
		if (checkSumInPkg != calculatedCheckSum) {
			log.warn("检验码不一致,msgid:{},pkg:{},calculated:{}", msgHeader.getMsgId(), checkSumInPkg, calculatedCheckSum);
		}
		return ret;
	}

	private PackageData.MsgHeader parseMsgHeaderFromBytes(byte[] data) {
		PackageData.MsgHeader msgHeader = new PackageData.MsgHeader();

		// 1. 消息ID word(16)
		// byte[] tmp = new byte[2];
		// System.arraycopy(data, 0, tmp, 0, 2);
		// msgHeader.setMsgId(this.bitOperator.twoBytesToInteger(tmp));
		msgHeader.setMsgId(this.parseIntFromBytes(data, 0, 2));

		// 2. 消息体属性 word(16)=================>
		// System.arraycopy(data, 2, tmp, 0, 2);
		// int msgBodyProps = this.bitOperator.twoBytesToInteger(tmp);
		int msgBodyProps = this.parseIntFromBytes(data, 2, 2);
		msgHeader.setMsgBodyPropsField(msgBodyProps);
		// [ 0-9 ] 0000,0011,1111,1111(3FF)(消息体长度)
		msgHeader.setMsgBodyLength(msgBodyProps & 0x3ff);
		// [10-12] 0001,1100,0000,0000(1C00)(加密类型)
		msgHeader.setEncryptionType((msgBodyProps & 0x1c00) >> 10);
		// [ 13_ ] 0010,0000,0000,0000(2000)(是否有子包)
		msgHeader.setHasSubPackage(((msgBodyProps & 0x2000) >> 13) == 1);
		// [14-15] 1100,0000,0000,0000(C000)(保留位)
		msgHeader.setReservedBit(((msgBodyProps & 0xc000) >> 14) + "");
		// 消息体属性 word(16)<=================

		// 3. 终端手机号 bcd[6]
		// tmp = new byte[6];
		// System.arraycopy(data, 4, tmp, 0, 6);
		// msgHeader.setTerminalPhone(this.bcd8421Operater.bcd2String(tmp));
		msgHeader.setTerminalPhone(this.parseBcdStringFromBytes(data, 4, 6));

		// 4. 消息流水号 word(16) 按发送顺序从 0 开始循环累加
		// tmp = new byte[2];
		// System.arraycopy(data, 10, tmp, 0, 2);
		// msgHeader.setFlowId(this.bitOperator.twoBytesToInteger(tmp));
		msgHeader.setFlowId(this.parseIntFromBytes(data, 10, 2));

		// 5. 消息包封装项
		// 有子包信息
		if (msgHeader.isHasSubPackage()) {
			// 消息包封装项字段
			msgHeader.setPackageInfoField(this.parseIntFromBytes(data, 12, 4));
			// byte[0-1] 消息包总数(word(16))
			// tmp = new byte[2];
			// System.arraycopy(data, 12, tmp, 0, 2);
			// msgHeader.setTotalSubPackage(this.bitOperator.twoBytesToInteger(tmp));
			msgHeader.setTotalSubPackage(this.parseIntFromBytes(data, 12, 2));

			// byte[2-3] 包序号(word(16)) 从 1 开始
			// tmp = new byte[2];
			// System.arraycopy(data, 14, tmp, 0, 2);
			// msgHeader.setSubPackageSeq(this.bitOperator.twoBytesToInteger(tmp));
			msgHeader.setSubPackageSeq(this.parseIntFromBytes(data, 12, 2));
		}
		return msgHeader;
	}

	protected String parseStringFromBytes(byte[] data, int startIndex, int lenth) {
		return this.parseStringFromBytes(data, startIndex, lenth, null);
	}

	private String parseStringFromBytes(byte[] data, int startIndex, int lenth, String defaultVal) {
		try {
			byte[] tmp = new byte[lenth];
			System.arraycopy(data, startIndex, tmp, 0, lenth);
			return new String(tmp, Consts.string_charset);
		} catch (Exception e) {
			log.error("解析字符串出错:{}", e.getMessage());
			e.printStackTrace();
			return defaultVal;
		}
	}

	private String parseBcdStringFromBytes(byte[] data, int startIndex, int lenth) {
		return this.parseBcdStringFromBytes(data, startIndex, lenth, null);
	}

	private String parseBcdStringFromBytes(byte[] data, int startIndex, int lenth, String defaultVal) {
		try {
			byte[] tmp = new byte[lenth];
			System.arraycopy(data, startIndex, tmp, 0, lenth);
			return this.bcd8421Operater.bcd2String(tmp);
		} catch (Exception e) {
			log.error("解析BCD(8421码)出错:{}", e.getMessage());
			e.printStackTrace();
			return defaultVal;
		}
	}

	private int parseIntFromBytes(byte[] data, int startIndex, int length) {
		return this.parseIntFromBytes(data, startIndex, length, 0);
	}



	private int parseIntFromBytes(byte[] data, int startIndex, int length, int defaultVal) {
		try {
			// 字节数大于4,从起始索引开始向后处理4个字节,其余超出部分丢弃
			final int len = length > 4 ? 4 : length;
			byte[] tmp = new byte[len];
			System.arraycopy(data, startIndex, tmp, 0, len);
			return bitOperator.byteToInteger(tmp);
		} catch (Exception e) {
			log.error("解析整数出错:{}", e.getMessage());
			e.printStackTrace();
			return defaultVal;
		}
	}

	public TerminalRegisterMsg toTerminalRegisterMsg(PackageData packageData) {
		TerminalRegisterMsg ret = new TerminalRegisterMsg(packageData);
		byte[] data = ret.getMsgBodyBytes();

		TerminalRegisterMsg.TerminalRegInfo body = new TerminalRegisterMsg.TerminalRegInfo();

		// 1. byte[0-1] 省域ID(WORD)
		// 设备安装车辆所在的省域，省域ID采用GB/T2260中规定的行政区划代码6位中前两位
		// 0保留，由平台取默认值
		body.setProvinceId(this.parseIntFromBytes(data, 0, 2));

		// 2. byte[2-3] 设备安装车辆所在的市域或县域,市县域ID采用GB/T2260中规定的行 政区划代码6位中后四位
		// 0保留，由平台取默认值
		body.setCityId(this.parseIntFromBytes(data, 2, 2));

		// 3. byte[4-8] 制造商ID(BYTE[5]) 5 个字节，终端制造商编码
		// byte[] tmp = new byte[5];
		body.setManufacturerId(this.parseStringFromBytes(data, 4, 5));

		// 4. byte[9-16] 终端型号(BYTE[8]) 八个字节， 此终端型号 由制造商自行定义 位数不足八位的，补空格。
		body.setTerminalType(this.parseStringFromBytes(data, 9, 8));

		// 5. byte[17-23] 终端ID(BYTE[7]) 七个字节， 由大写字母 和数字组成， 此终端 ID由制造 商自行定义
		body.setTerminalId(this.parseStringFromBytes(data, 17, 7));

		// 6. byte[24] 车牌颜色(BYTE) 车牌颜 色按照JT/T415-2006 中5.4.12 的规定
		body.setLicensePlateColor(this.parseIntFromBytes(data, 24, 1));

		// 7. byte[25-x] 车牌(STRING) 公安交 通管理部门颁 发的机动车号牌
		body.setLicensePlate(this.parseStringFromBytes(data, 25, data.length - 25));

		ret.setTerminalRegInfo(body);
		return ret;
	}

	public TravelData toTravelDataReq(PackageData packageData){
		byte[] data = packageData.getMsgBodyBytes();
		TravelData req = new TravelData();
		String sTime = this.parseBcdStringFromBytes(data,0,6);
		sTime = "20"+sTime.substring(0,2)+"-"+sTime.substring(2,4)+"-"+sTime.substring(4,6)+" "+sTime.substring(6,8)+":"+sTime.substring(8,10)+":"+sTime.substring(10,12);
		DateTime sDate = DateTime.parse(sTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
		sDate.plusHours(8);
		req.setStartTime(sDate.plusHours(8).toString("yyyy-MM-dd HH:mm:ss"));

		String eTime = this.parseBcdStringFromBytes(data,6,6);
		eTime = "20"+eTime.substring(0,2)+"-"+eTime.substring(2,4)+"-"+eTime.substring(4,6)+" "+eTime.substring(6,8)+":"+eTime.substring(8,10)+":"+eTime.substring(10,12);
		DateTime eDate = DateTime.parse(eTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
		eDate.plusHours(8);
		req.setEndTime(eDate.plusHours(8).toString("yyyy-MM-dd HH:mm:ss"));
		req.setMilage(this.parseIntFromBytes(data,12,4));
		req.setStartLat(""+(float)this.parseIntFromBytes(data,16,4)/1000000);
		req.setStartLng(""+(float)this.parseIntFromBytes(data,20,4)/1000000);
		req.setEndLat(""+(float)this.parseIntFromBytes(data,24,4)/1000000);
		req.setEndLng(""+(float)this.parseIntFromBytes(data,28,4)/1000000);
		req.setDeviceId(packageData.getMsgHeader().getTerminalPhone());
		return req;
	}

	public static void  main(String[] args){
		PackageData packageData = new PackageData();
		//02 00 00 30 24 70 20 01 57 47 00 38
		//10 00 00 00 00 04 00 02 01 D1 FC 9A 06 D0 D3 A3 00 0E 00 00 00 20 19 01 18134927 05 04 00 00 00 01 060200080702006408040000000A51
		packageData.setMsgBodyBytes(new byte[]{
				0x10, 0x00, 0x00, 0x00,
				0x00, 0x04, 0x00, 0x02,
				0x01, (byte)0xD1, (byte)0xFC, (byte)0x9A,
				0x06, (byte)0xD0, (byte)0xD3, (byte)0xA3,
				0x00, 0x0E,
				0x00, 0x00,
				0x00, 0x20,
				0x19, 0x01, 0x18, 0x13, 0x49, 0x27,
				0x05,
				0x04,
				0x00, 0x00, 0x00,0x01});

		MsgDecoder d = new MsgDecoder();
		d.toLocationInfoUploadMsg(packageData);
	}


	/**
	 * 020000302470200157470022000000000004000201D1FCC906D0D459002A00000033190118151657060400000001060200090702006408040000000AEA
	 * 0200
	 * 0030
	 * 247020015747
	 * 0022
	 * 000000000004000201D1FCC906D0D459002A00000033190118151657050400000001060200090702006408040000000AEA
	 */
	public LocationInfoUploadMsg toLocationInfoUploadMsg(PackageData packageData) {
		LocationInfoUploadMsg ret = new LocationInfoUploadMsg(packageData);
		final byte[] data = ret.getMsgBodyBytes();
		// 1. byte[0-3] 报警标志(DWORD(32))
		ret.setWarningFlagField(this.parseIntFromBytes(data, 0, 4));
		// 2. byte[4-7] 状态(DWORD(32))
		ret.setStatusField(this.parseIntFromBytes(data, 4, 4));
		// 3. byte[8-11] 纬度(DWORD(32)) 以度为单位的纬度值乘以10^6，精确到百万分之一度
		ret.setLatitude((float)(Long.parseLong(ByteBufUtil.hexDump(data, 8, 4), 16) / 1000000.0));
		// 4. byte[12-15] 经度(DWORD(32)) 以度为单位的经度值乘以10^6，精确到百万分之一度
		ret.setLongitude((float)(Long.parseLong(ByteBufUtil.hexDump(data, 12, 4), 16) / 1000000.0));
		// 5. byte[16-17] 高程(WORD(16)) 海拔高度，单位为米（ m）
		ret.setElevation(this.parseIntFromBytes(data, 16, 2));
		// byte[18-19] 速度(WORD) 1/10km/h
		ret.setSpeed(this.parseIntFromBytes(data, 18, 2)/10);
		// byte[20-21] 方向(WORD) 0-359，正北为 0，顺时针
		ret.setDirection(this.parseIntFromBytes(data, 20, 2));
		// byte[22-x] 时间(BCD[6]) YY-MM-DD-hh-mm-ss
		// GMT+8 时间，本标准中之后涉及的时间均采用此时区

		byte[] tmp = new byte[6];
		System.arraycopy(data, 22, tmp, 0, 6);
		String time = this.parseBcdStringFromBytes(data, 22, 6);
		try {
			ret.setTime(DateUtils.getDate(time, "yyMMddHHmmss"));
		} catch (ParseException e) {
		}
		if (ret.getMsgHeader().getMsgBodyLength() > 30){
			String extraInfoId = ByteBufUtil.hexDump(data, 28, 1);
			if (extraInfoId.equals("06")){
				int extraInfoLength = Integer.parseInt(ByteBufUtil.hexDump(data, 29, 1));
				String extraInfoData = ByteBufUtil.hexDump(data, 30, extraInfoLength);
				int starNum = Integer.parseInt(extraInfoData.substring(0,2));
				ret.setStarNum(starNum);
			}
		}
		return ret;
	}
	public LocationInfoUploadMsg toLocationInfoUploadMsg(PackageData packageData,byte[] data,int starNum) {
	    log.info("data:" + new String(data));
		LocationInfoUploadMsg ret = new LocationInfoUploadMsg(packageData);
		// 1. byte[0-3] 报警标志(DWORD(32))
		ret.setWarningFlagField(this.parseIntFromBytes(data, 0, 4));
		// 2. byte[4-7] 状态(DWORD(32))
		ret.setStatusField(this.parseIntFromBytes(data, 4, 4));
		// 3. byte[8-11] 纬度(DWORD(32)) 以度为单位的纬度值乘以10^6，精确到百万分之一度
		ret.setLatitude((float)(Long.parseLong(ByteBufUtil.hexDump(data, 8, 4), 16) / 1000000.0));
		// 4. byte[12-15] 经度(DWORD(32)) 以度为单位的经度值乘以10^6，精确到百万分之一度
		ret.setLongitude((float)(Long.parseLong(ByteBufUtil.hexDump(data, 12, 4), 16) / 1000000.0));
		// 5. byte[16-17] 高程(WORD(16)) 海拔高度，单位为米（ m）
		ret.setElevation(this.parseIntFromBytes(data, 16, 2));
		// byte[18-19] 速度(WORD) 1/10km/h
		ret.setSpeed(this.parseIntFromBytes(data, 18, 2)/10);
		// byte[20-21] 方向(WORD) 0-359，正北为 0，顺时针
		ret.setDirection(this.parseIntFromBytes(data, 20, 2));
		// byte[22-x] 时间(BCD[6]) YY-MM-DD-hh-mm-ss
		// GMT+8 时间，本标准中之后涉及的时间均采用此时区

		ret.setStarNum(starNum);
		byte[] tmp = new byte[6];
		System.arraycopy(data, 22, tmp, 0, 6);
		String time = this.parseBcdStringFromBytes(data, 22, 6);
		try {
			ret.setTime(DateUtils.getDate(time, "yyMMddHHmmss"));
		} catch (ParseException e) {
		}
		return ret;
	}

	public BatchLocationInfoUploadMsg toBatchLocationInfoUploadMsg(PackageData packageData) {
		BatchLocationInfoUploadMsg ret = new BatchLocationInfoUploadMsg(packageData);
		final byte[] data = ret.getMsgBodyBytes();
		ret.setGpsNum(this.parseIntFromBytes(data, 0, 1));
		List<LocationInfoUploadMsg> gpsList = new ArrayList<>(ret.getGpsNum());
		int starNum = 0;
		if (data.length > 28 * ret.getGpsNum()){
			boolean hasNext = true;
			int extraInfoStart = 28 * ret.getGpsNum() + 1;
			int extraInfoLength = data.length - extraInfoStart;
			String extraInfo = ByteBufUtil.hexDump(data, extraInfoStart, extraInfoLength);
			int currentIndex = 0;
			while(hasNext){
				String infoId = extraInfo.substring(currentIndex,currentIndex + 2);
				String lengthStr = extraInfo.substring(currentIndex + 2,currentIndex + 4);
				int length = Integer.parseInt(lengthStr) * 2;
				if (infoId.equals("06")){
					String body = extraInfo.substring(currentIndex + 4,currentIndex + 4 + length);
					starNum = Integer.parseInt(body);
					hasNext = false;
				}
				currentIndex += length + 4;
				if (currentIndex >= extraInfoLength){
					hasNext = false;
				}
			}
//			byte[] ttps = new byte[]{0x05,0x04,0x00,0x00,0x00,0x03,0x06,0x02,0x00,0x64,0x07,0x04,0x01,0x00,0x00,0x0A};
//			for (int i=0; i<ttps.length; ){
//				int id = (int)ttps[i];
//				if (id == 1){
//					i += 6;
//				}else if (id == 2){
//					i += 4;
//				}else if (id == 3){
//					i += 4;
//				}else if (id == 4){
//					i += 4;
//				}else if (id == 5){
//					i += 6;
//				}else if (id == 6){
//					byte[] tmp = new byte[2];
//					System.arraycopy(ttps, i+2, tmp, 0, 2);
//					System.out.println(ByteBufUtil.hexDump(tmp));
//					starNum = Integer.parseInt(ByteBufUtil.hexDump(tmp));
//					break;
//				}else if (id == 7){
//					i += 4;
//				}else if (id == 8){
//					i += 6;
//				}else if (id == 9){
//					i += 4;
//				}
//			}
		}
		for (int i = 0;i<ret.getGpsNum();i++){
			byte[] bytes = new byte[28];
			System.arraycopy(data,i*28 + 1,bytes,0,28);
			LocationInfoUploadMsg gps = toLocationInfoUploadMsg(packageData,bytes,starNum);
			gpsList.add(gps);
		}
		ret.setGpsList(gpsList);
		return ret;
	}

	private float parseFloatFromBytes(byte[] data, int startIndex, int length) {
		return this.parseFloatFromBytes(data, startIndex, length, 0f);
	}

	private float parseFloatFromBytes(byte[] data, int startIndex, int length, float defaultVal) {
		try {
			// 字节数大于4,从起始索引开始向后处理4个字节,其余超出部分丢弃
			final int len = length > 4 ? 4 : length;
			byte[] tmp = new byte[len];
			System.arraycopy(data, startIndex, tmp, 0, len);
			return bitOperator.byte2Float(tmp);
		} catch (Exception e) {
			log.error("解析浮点数出错:{}", e.getMessage());
			e.printStackTrace();
			return defaultVal;
		}
	}

	public EventMsg toEventMsg(PackageData packageData) {
		EventMsg eventMsg = new EventMsg(packageData);
		byte[] data = eventMsg.getMsgBodyBytes();
		int event = this.parseIntFromBytes(data, 0, 2);
		eventMsg.setEventType(event);
		return eventMsg;
	}
}
