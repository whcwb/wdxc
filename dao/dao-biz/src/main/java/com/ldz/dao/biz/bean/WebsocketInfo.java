package com.ldz.dao.biz.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class WebsocketInfo {

	private String clid;

	private String cph;

	private String speed;

	private BigDecimal fxj;

	private String eventType;

	private Date  time;

	private String bdjd;

	private String bdwd;

	private String gdjd;

	private String gdwd;

	private String zdbh;


	private String cx;

	private String sjxm;

	private String obdId;

	private String zxzt;

	//离线时长
	private Long lxsc;

	private Long durartion;

	private String xlId;
	private String stationNumber;

	private String zdLx;

	private boolean defaultGps;

	private List<Map<String,BigDecimal>> gpsList;
}
