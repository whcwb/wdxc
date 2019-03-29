package com.ldz.dao.biz.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DdTjTxReturn {

	private List<String> date;

	private List<String> count;

	private String startTime;
	private String endTime;

}
