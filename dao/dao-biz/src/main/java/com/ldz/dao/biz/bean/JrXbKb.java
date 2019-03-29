package com.ldz.dao.biz.bean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/*
 * 今日校巴开班模型
 */
@Getter
@Setter
public class JrXbKb {

	private  List<String> xlmcList;
	
	private  List<Integer> count;
}
