package com.ldz.znzp.bean;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 智能站牌在线bean结构
 * @author Lee
 *
 */
@Getter
@Setter
public class ZnzpOnlineBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2975934295202949313L;
	//智能站牌终端ID
	private String tid;
	//智能站牌连接通道ID
	private String cid;
	//最后在线时间
	private long olTime;

}
