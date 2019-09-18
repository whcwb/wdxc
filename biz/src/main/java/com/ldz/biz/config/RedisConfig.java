package com.ldz.biz.config;

import com.ldz.dao.dwq.mapper.DwqGpsMapper;
import com.ldz.dao.dwq.mapper.MileDwqMapper;
import com.ldz.dao.obd.mapper.ObdFaultCodeMapper;
import com.ldz.service.biz.interfaces.*;
import com.ldz.sys.service.ZdxmService;
import com.ldz.util.commonUtil.SnowflakeIdWorker;
import com.ldz.util.redis.RedisTemplateUtil;
import com.ldz.util.spring.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * redis配置
 * @author 李彬彬
 *
 */
@Configuration
@Order(1)
public class RedisConfig {

	@Value("${apiurl}")
	public  String url;
	@Value("${znzpurl}")
	public  String znzpurl;
	@Value("${biz_url: }") // : http://47.98.39.45:8080
	public  String bizurl;
	@Value("${distance}")
	public double distance;
	@Value("${lowSpeed}")
	private double lowSpeed;
	@Autowired
	private ZdxmService zdxmService;
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;
	@Autowired
	private DwqGpsMapper dwqGpsMapper;


	private RedisTemplateUtil redisTemplateUtil;



	/**
	 * 本项目缓存Redis对象
	 * @return
	 */
	@Bean
	@Primary
	public RedisTemplateUtil redisTemplateDefault(){
		redisTemplateUtil = new RedisTemplateUtil(redisConnectionFactory);
		return redisTemplateUtil;
	}
	/**
	 * redis消息监听器容器
	 * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
	 * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
	 * @param connectionFactory
	 * @return
	 */
	@Bean //相当于xml中的bean
	public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
		System.out.println("container");
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);

		//订阅了一个叫chat 的通道
		List<Topic> topics = new ArrayList<>();
		topics.add(new PatternTopic("gps"));
		topics.add(new PatternTopic("spk"));
		topics.add(new PatternTopic("gpsList"));


		// 设备检测topic
		List<Topic> topicList = new ArrayList<>();
		PatternTopic travelTopic = new PatternTopic("travelReport");
		PatternTopic checkTopic = new PatternTopic("engine_fault");
		PatternTopic obdTopic = new PatternTopic("obd_info");
		PatternTopic dataTopic = new PatternTopic("travelData");
		PatternTopic mileTopic = new PatternTopic("mile_dwq");
		topicList.add(checkTopic);
		topicList.add(obdTopic);
		topicList.add(travelTopic);
		topicList.add(dataTopic);
		topicList.add(mileTopic);
		SimpMessagingTemplate websocket = SpringContextUtil.getBean(SimpMessagingTemplate.class);
		SnowflakeIdWorker idWorker = SpringContextUtil.getBean(SnowflakeIdWorker.class);
		SbyxsjjlService sbyxsjjlService = SpringContextUtil.getBean(SbyxsjjlService.class);
		ObdFaultCodeMapper obdFaultCodeMapper = SpringContextUtil.getBean(ObdFaultCodeMapper.class);
		ZdxmService zdxmService = SpringContextUtil.getBean(ZdxmService.class);
		ClService clService = SpringContextUtil.getBean(ClService.class);
		MileDwqMapper dwqMapper = SpringContextUtil.getBean(MileDwqMapper.class);
		CheckMessageReceiver checkMessageReceiver = new CheckMessageReceiver(redisTemplateUtil,websocket,idWorker,obdFaultCodeMapper,zdxmService,sbyxsjjlService,clService,dwqMapper);


		// 终端数据统计事件
		List<Topic> zdTopicList = new ArrayList<>();
		zdTopicList.add(new PatternTopic("zdReport"));
		ClZdglReportService reportService = SpringContextUtil.getBean(ClZdglReportService.class);

		XcService xcService = SpringContextUtil.getBean(XcService.class);
		GpsLsService gpsLsService = SpringContextUtil.getBean(GpsLsService.class);
		ZdglService zdglService = SpringContextUtil.getBean(ZdglService.class);
		ClYyService clYyService = SpringContextUtil.getBean(ClYyService.class);
		ZdReportListener reportListener = new ZdReportListener(redisTemplateUtil,reportService,xcService,sbyxsjjlService,gpsLsService,zdglService,idWorker,clYyService);


		// 订阅过期 topic
		// 设置监听的Topic
		PatternTopic channelTopic = new PatternTopic("__keyevent@*__:expired");
		SpkService spkService = SpringContextUtil.getBean(SpkService.class);
		GpsService gpsservice = SpringContextUtil.getBean(GpsService.class);
		MessageReceiver messageReceiver = new MessageReceiver(spkService,gpsservice,redisTemplateUtil,dwqGpsMapper);




		//topicMessageListener.setRedisTemplate(redisTemplateUtil);
		container.addMessageListener(messageReceiver, topics);
		container.addMessageListener(new TopicMessageListener(zdxmService,xcService,clYyService,gpsservice,gpsLsService,zdglService,redisTemplateUtil,url,znzpurl,bizurl,distance,lowSpeed) , channelTopic);
		container.addMessageListener(checkMessageReceiver,topicList);
		container.addMessageListener(reportListener,zdTopicList);
		//这个container 可以添加多个 messageListener
		return container;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
