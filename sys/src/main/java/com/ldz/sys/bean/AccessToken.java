package com.ldz.sys.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户登陆成功后返回授权bean对象
 * @author 李彬彬
 *
 */
@Getter
@Setter
public class AccessToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -350566562243610439L;

	//登陆用户名
	private String userId;
	//登陆用户名
	private String username;
	//授权token
	private String token;
	//过期时间。
	private long expired_time;
	
}
