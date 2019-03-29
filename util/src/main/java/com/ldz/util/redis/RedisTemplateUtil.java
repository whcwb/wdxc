package com.ldz.util.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis对象类
 * @author 李彬彬
 *
 */
@SuppressWarnings("unchecked")
public class RedisTemplateUtil extends RedisTemplate<Object, Object>{
	
	/**
	 * 将Key序列化都保持一致，防止Key模式不对，造成过期时间设置无效和key引用操作失败
	 */
	public RedisTemplateUtil() {
		StringRedisSerializer stringSerializer = new StringRedisSerializer();  
		//Jackson2JsonRedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
		JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();
		setKeySerializer(stringSerializer);
		setHashKeySerializer(stringSerializer);
		
		setValueSerializer(jdkSerializer);
		setHashValueSerializer(jdkSerializer);
	}
	
	public RedisTemplateUtil(RedisConnectionFactory connectionFactory) {
        this();
        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }
	
	/**
	 * 根据指定的值从redis中取出序列化对象，取出序列化方式为Json。自动将结果转换为指定的序列化对象
	 * @param key
	 * @param cls
	 * @return
	 */
	public <T>T getSerializerValue(String key, Class<T> cls){
		return (T)this.boundValueOps(key).get();
	}
	
	public <T>T getSerializerHashValue(String hash, String key, Class<T> cls){
		return (T)this.boundHashOps(hash).get(key);
	}
	public Object getSerializerHashValue(String hash, String key){
		return this.boundHashOps(hash).get(key);
	}
}
