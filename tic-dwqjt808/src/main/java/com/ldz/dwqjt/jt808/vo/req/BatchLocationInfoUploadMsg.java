package com.ldz.dwqjt.jt808.vo.req;


import com.ldz.dwqjt.jt808.vo.PackageData;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 位置信息汇报消息
 *
 * @author hylexus
 *
 */
@Getter
@Setter
@ToString
public class BatchLocationInfoUploadMsg extends PackageData {
	private int gpsNum;

	private List<LocationInfoUploadMsg> gpsList;

	public BatchLocationInfoUploadMsg() {
	}

	public BatchLocationInfoUploadMsg(PackageData packageData) {
		this();
		this.channel = packageData.getChannel();
		this.checkSum = packageData.getCheckSum();
		this.msgBodyBytes = packageData.getMsgBodyBytes();
		this.msgHeader = packageData.getMsgHeader();
	}

}
