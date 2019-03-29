package com.ldz.sys.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户登陆信息表单Bean对象
 * @author 李彬彬
 *
 */
@Getter
@Setter
public class UserPassCredential implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -350566562243610439L;

	//登陆用户名
	private String username;
	//登陆密码
	private String password;
	//验证码
	private String captcha;
	//验证码前台生成Key
	private String codeID;
}
