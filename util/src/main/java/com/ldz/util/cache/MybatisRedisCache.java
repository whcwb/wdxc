package com.ldz.util.cache;

import java.lang.reflect.Method;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.util.ReflectionUtils;

import com.ldz.util.redis.RedisTemplateUtil;
import com.ldz.util.spring.SpringContextUtil;

/**
 * Cache adapter for Redis.
 *
 */
public class MybatisRedisCache implements Cache {

	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	private String id;

	private RedisTemplateUtil redisDao;

	private static final String KEY_PERFIX = "whdxclglpt";
	
	public MybatisRedisCache(final String id) {
		if (id == null) {
			throw new IllegalArgumentException("Cache instances require an ID");
		}
		this.id = KEY_PERFIX + id.substring(id.lastIndexOf("."));
		//防止mybatis先于redis加载，造成redis对象为空，无法使用
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(redisDao == null){
					RedisConnectionFactory redisConn = SpringContextUtil.getBean(RedisConnectionFactory.class);
					if (redisConn != null){
						redisDao = new RedisTemplateUtil(redisConn);	
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public int getSize() {
		return redisDao.boundHashOps(id).size().intValue();
	}

	public Class<?> getMethodReturnType(String keyString) {
		String className = keyString.substring(0, keyString.lastIndexOf("."));
		className = className.substring(className.lastIndexOf(":") + 1);
		String methodName = keyString.substring(keyString.lastIndexOf(".") + 1);
		methodName = methodName.substring(0, methodName.indexOf(":"));
		try {
			Method method = ReflectionUtils.findMethod(Class.forName(className), methodName);
			return method.getReturnType();
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public void putObject(final Object key, final Object value) {
		redisDao.boundHashOps(id).put(key.toString(), value);
	}

	@Override
	public Object getObject(final Object key) {
		return redisDao.getSerializerHashValue(id, key.toString());
	}

	@Override
	public Object removeObject(final Object key) {
		return redisDao.boundHashOps(id).delete(key.toString());
	}

	@Override
	public void clear() {
		redisDao.delete(id);
	}

	@Override
	public ReadWriteLock getReadWriteLock() {
		return readWriteLock;
	}

	@Override
	public String toString() {
		return "Redis {" + id + "}";
	}
}
