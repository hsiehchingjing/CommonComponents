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
	private JedisPool jedisPool = null;

	public RedisClient(String configName) throws Exception {
		this.g_configName = configName;
		g_Config = ConfigManager.getConfig(this.g_configName);
		 if(g_Config==null) throw new Exception("redis config is null");
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(g_Config.MaxTotal);
			config.setMaxIdle(g_Config.MaxIdle);
			config.setMaxWaitMillis(g_Config.MaxWait);
			config.setTestOnBorrow(true);
			jedisPool = new JedisPool(config, g_Config.ServiceHost, g_Config.ServicePort, 
					g_Config.TimeOut, g_Config.RedisPwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void setObject(String key,Object value) {
		if (jedisPool != null) {
			Jedis redisResource = jedisPool.getResource();
			redisResource.set(key, JsonConvert.SerializeObject(value));
			redisResource.close();
		}

	}

	public synchronized <T> T getObject(String key, Class<T> valueType){
		if (jedisPool != null) {
			Jedis redisResource = jedisPool.getResource();
			String value=redisResource.get(key);
			redisResource.close();
			try {
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
		}
		return null;
	}

}
