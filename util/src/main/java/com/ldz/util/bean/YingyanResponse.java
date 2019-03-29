package com.ldz.util.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class YingyanResponse {
	/*
	 * 返回状态 0:成功
	 */
   private String status; 
   /*
	 * 返回信息
	 */
   private String message;
}
