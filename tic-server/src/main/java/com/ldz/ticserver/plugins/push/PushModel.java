package com.ldz.ticserver.plugins.push;

public class PushModel {
	public static int START_APP = 1;
	public static int OPEN_URL = 2;
	public static int DOWN_URL = 3;
	public static int SILENCE = 4;

	private int pushType = SILENCE; // 推送类型 1 推送点击启动应用  2 推送点击打开网页，使用2 则url必须填写 ； 3 点击通知进行下载操作 url也是必须填写 ； 4 透传通知  默认透传
	private Object pushData;//推送的系统数据，2000字符以内的数据,这里为具体得推送对象
	private String title;//通知栏标题
	private String text;//通知栏内容（通知简介）
	private String[] clientIds;// 同时推送一个消息到多个人的时候用这个
	private String clientId;// 推送消息到某一个人这个填写一个客户端id即可
	private String url;//打开的指定链接，几乎用户到，除非广告
	private Boolean pushAll = false; //是否全部推送 设置为true之后，clientId 和 clientIds将失效
	private Integer runAppState = 2;//1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	
	public int getPushType() {
		return pushType;
	}
	public void setPushType(int pushType) {
		this.pushType = pushType;
	}
	public Object getPushData() {
		return pushData;
	}
	public void setPushData(Object pushData) {
		this.pushData = pushData;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String[] getClientIds() {
		return clientIds;
	}
	public void setClientIds(String[] clientIds) {
		this.clientIds = clientIds;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public PushModel(int pushType,String title, String text, String[] clientIds, String clientId, Object pushData) {
		super();
		this.pushType = pushType;
		this.pushData = pushData;
		this.title = title;
		this.text = text;
		this.clientIds = clientIds;
		this.clientId = clientId;
	}
	public PushModel(int pushType,String title, String text, String clientId, Object pushData) {
		super();
		this.pushType = pushType;
		this.pushData = pushData;
		this.title = title;
		this.text = text;
		this.clientId = clientId;
	}
	
	public PushModel(int pushType, Object pushData, String title, String text, String[] clientIds, String clientId,
			String url, Boolean pushAll) {
		super();
		this.pushType = pushType;
		this.pushData = pushData;
		this.title = title;
		this.text = text;
		this.clientIds = clientIds;
		this.clientId = clientId;
		this.url = url;
		this.pushAll = pushAll;
	}
	public PushModel(){
		super();
	}
	public PushModel(int pushType,String title, String text, Object pushData) {
		super();
		this.pushType = pushType;
		this.pushData = pushData;
		this.title = title;
		this.text = text;
	}
	public PushModel(int pushType, String title, String text) {
		super();
		this.pushType = pushType;
		this.title = title;
		this.text = text;
	}
	public Boolean getPushAll() {
		return pushAll;
	}
	public void setPushAll(Boolean pushAll) {
		this.pushAll = pushAll;
	}
	public Integer getRunAppState() {
		return runAppState;
	}
	public void setRunAppState(Integer runAppState) {
		this.runAppState = runAppState;
	}
	
	
}
