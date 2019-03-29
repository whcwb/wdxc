package com.ldz.dao.biz.bean;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/*
 * 司机出车统计条形图模型
 */
@Getter
@Setter
public class CcTjTx {

	private List<String> sjxm;
	
	private List<Integer> count;
	
	private Map<String, Integer>  tjMap;
}
