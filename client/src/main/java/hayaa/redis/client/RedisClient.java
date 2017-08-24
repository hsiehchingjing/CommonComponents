package hayaa.redis.client;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import hayaa.redis.client.config.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @see 内部有连接优化，不建议创建多个对象
 * @author hsieh
 *
 */
public class RedisClient implements IRedisClient {
	private String g_configName = "default";
	private RedisConfig g_Config = null;
	private JedisPool g_jedisPool = null;

	public RedisClient(String configName) throws Exception {
		this.g_configName = configName;
		g_Config = ConfigManager.getConfig(this.g_configName);
		if (g_Config == null)
			throw new Exception("redis config is null");
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(100); // maximum active connections
			config.setMaxIdle(100);  // maximum idle connections
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);
			g_jedisPool = new JedisPool(config, g_Config.ServiceHost,
					g_Config.ServicePort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public  void setObject(String key, Object value) {
		if (g_jedisPool != null) {
			Jedis redisResource = null;
			try {
				redisResource = g_jedisPool.getResource();
				redisResource.set(key, JsonConvert.SerializeObject(value));
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
			if (redisResource != null)
				redisResource.close();
		}

	}

	public  <T> T getObject(String key, Class<T> valueType) {
		if (g_jedisPool != null) {
			Jedis redisResource = null;
			try {
				redisResource = g_jedisPool.getResource();
				String value = redisResource.get(key);
				
				return JsonConvert.DeserializeObject(value, valueType);
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (redisResource != null)
				redisResource.close();
		}
		return null;
	}

}
