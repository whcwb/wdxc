package com.ldz.util.bean;

import java.util.Map;


public class PushModelDto {

	private String title;//推送title
	private String content;//推送内容
	private String[] channelIds;//推送客户端列表
	private Integer openType = 1;//打开类型
	private String url;//跳转的url
	/**
	 * 自定义的key value  用于自定义消息,如果是发送命令，那么这个参数里必须有一个cmdType和cmd参数
	 * cmdType 为指令类型  cmd为特殊指定的辅助命令，用于客户端解析。具体参数，后期定义
	 */
	private Map<String,Object> jsonCustormCont;
	private Integer msgType = 1;//消息类型  0：透传消息， 1：通知，默认值为0
	private Integer deviceType = 3;//设备类型 3：Android 4：IOS
	private String topicId;//类别主题
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String[] getChannelIds() {
		return channelIds;
	}
	public void setChannelIds(String[] channelIds) {
		this.channelIds = channelIds;
	}
	public Integer getOpenType() {
		return openType;
	}
	public void setOpenType(Integer openType) {
		this.openType = openType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Map<String,Object> getJsonCustormCont() {
		return jsonCustormCont;
	}
	public void setJsonCustormCont(Map<String,Object> jsonCustormCont) {
		this.jsonCustormCont = jsonCustormCont;
	}
	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	public Integer getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
}
