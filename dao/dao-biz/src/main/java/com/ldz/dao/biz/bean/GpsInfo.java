package com.ldz.dao.biz.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 *  用于处理tic-server向业务系统发送gps,云视屏信息模型
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GpsInfo {
	private String id;
	private String deviceId;//设备id（每次都必须上传）
	private String channelId;//用于推送（每次都必须上传）
	private String deviceTag;//设备tag主要也是 用于推送（每次都必须上传）
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String longitude;//经度（参数几乎每个接口都会上传）
	private String latitude;//纬度（参数几乎每个接口都会上传）
	private Integer speed;//速度
	private String eventType;
//事件 10急加速，20急刹车，30急转弯 ，40超速，50点火，60熄火,70不在电子围栏范围,80离线
	private String filePath;//文件相对路径(上传视频或者图片才会使用的参数)
	private String fileLocalPath;//文件本地绝对路径(上传视频或者图片才会使用的参数)
	private String fileRealName;//上传的文件在设备中的名称(上传视频或者图片才会使用的参数)
	private String fileSize;//文件大小(上传视频或者图片才会使用的参数)
	private String filePostfix;//文件后缀（可以用于文件类型）
	private String taskId;//任务id（用于上传服务器下载命令之后，终端上传之后回调给服务器之后的数据使用）
	private String cmdType;//命令类型（推送消息时使用）  01：超速设定 02:灵敏度设定(急加速灵敏度)
	private Boolean pushAll;
	private String starNum;  // 卫星个数
	//11:拍（当前时间前后10s）视频 12:拍图片(实时) 13 合并视频   20 碰撞灵敏度     40 GPS心跳间隔   50上传视屏模式
	/**  90:下发apk更新命令  91:设置上报数据的url   92:下发播报语音
     cmdType=01
      cmd值（ 1~150之间的数字)

    cmdType 02:
	    设置加减速监测灵敏等级，level 1-6，默认为 2 :
       1. 速度从零加到百公里约58秒内，能够被检测到急加速;
       2. 速度从零加到百公里约29秒内，能够被检测到急加速;
       3. 速度从零加到百公里约19秒内，能够被检测到急加速;
       4. 速度从零加到百公里约14秒内，能够被检测到急加速;
       5. 速度从零加到百公里约11秒内，能够被检测到急加速;
       6. 速度从零加到百公里约9秒内，能够被检测到急加速

     cmdType=13
                  合并视频只需要将startTime 2017-10-12 00:00:00 和endTime 2017-10-12 00:00:00赋值即可

     cmdType=20
       cmd值（ 00：低
        10：中
        20： 高)


     cmdType=40:
     	cmd值（ 1~300s之间的数字)

     50:
       10:wifi下上传普通视屏
       20:wifi下不上传视屏
       30:wifi/4g下都上传视屏
	 * cmdType 为11和12的时候使用
	 * 参数格式为分隔式字符串  如:0-10 前一个0 标识要抓拍的摄像头  后一个10标识当前时间点前后十秒
	 * 摄像头参数如下:0,前后都抓拍, 1表示仅前摄像头, 2表示仅仅后摄像头。当cmdType为12的时候，此参数也是一样，只是抓拍前后多少秒参数无效【客户端自动判断，后台传递参数即可】
	 * cmdType 为13的时候参数是0-0 或者1-0  ，特别注意，为13的时候，startTime和endTime必须有值
	 * 摄像头参数如下:0 合并前摄像头  1 合并后摄像头  2 合并内置摄像头【内置摄像头这个暂时无法使用】
	 *
	 * cmdType为11 ，12 ，13的时候这样设定     值用cmdParams 里面

      90:
       cmd:  apk具体下载的url
      91:
       cmd:具体的服务器api地址前缀，tic-server的顶级访问目录。如果部署在root下，直接是http://ip+port/ 如果有项目名。则是http://ip+port+项目名称/
            例如:http://47.98.39.45:8080/tic-server/api
     92:
       cmd: 具体语音内容
	 */
	private String sczt; //该点位的上传状态  10点火上传  20熄火上传
	private String cmd;//具体命令（推送消息时使用）
	private String cmdParams;//其它参数（推送消息时使用）
	private String fxj;//方向角
	private String gpsjd;//GPS精度
}
