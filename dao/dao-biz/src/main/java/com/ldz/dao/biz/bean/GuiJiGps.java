package com.ldz.dao.biz.bean;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GuiJiGps {
    private String  id;
	
	private BigDecimal bdjd;
	
	private BigDecimal bdwd;
	
	private String yxsd;
	
	private Double fxj;
	
	private  Date cjsj;
	
    private String gdjd;
	
	private String gdwd;
	
    private BigDecimal jd;
	
	private BigDecimal wd;
}
