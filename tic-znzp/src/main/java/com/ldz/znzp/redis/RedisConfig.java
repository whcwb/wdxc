package com.ldz.znzp.redis;

import com.ldz.util.redis.RedisTemplateUtil;
import com.ldz.util.spring.SpringContextUtil;
import com.ldz.znzp.service.ZnzpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

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
	
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;
	@Value("${spring.redis.otherDatabase}")
	private int otherDatabase;

	/**
	 * 本项目缓存Redis对象
	 * @return
	 */
	@Bean
	@Primary
	public RedisTemplateUtil redisTemplateDefault(){
		RedisTemplateUtil bean = new RedisTemplateUtil(redisConnectionFactory);
		return bean;
	}
	
	/**
	 * 从外部提取数据的Redis对象
	 * @return
	 */
	@Bean(name="redisOtherDB")
	public RedisTemplateUtil redisTemplateOtherDB(){
		JedisConnectionFactory jedisConn = (JedisConnectionFactory)redisConnectionFactory;
		JedisConnectionFactory nJedisConn = new JedisConnectionFactory(jedisConn.getPoolConfig());
		nJedisConn.setHostName(jedisConn.getHostName());
		nJedisConn.setPort(jedisConn.getPort());
		nJedisConn.setPassword(jedisConn.getPassword());
		nJedisConn.setUsePool(true);
		nJedisConn.setShardInfo(jedisConn.getShardInfo());
		nJedisConn.setDatabase(otherDatabase);
		
		RedisTemplateUtil bean = new RedisTemplateUtil(nJedisConn);
		return bean;
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
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);

		//订阅了一个叫chat 的通道

		List<Topic> topics = new ArrayList<>();
		topics.add(new PatternTopic("gps"));
		topics.add(new PatternTopic("spk"));
		container.addMessageListener(new MessageReceiver(redisTemplateDefault()), topics);
		container.addMessageListener(new OffLineMessageReceiver(redisTemplateDefault(),SpringContextUtil.getBean(ZnzpService.class)),new PatternTopic("offline"));
		//这个container 可以添加多个 messageListener
		return container;
	}
}
