package hayaa.redis.client;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import hayaa.redis.client.config.RedisClusterConfig;
import hayaa.redis.client.config.RedisConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
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
	private Set<HostAndPort> g_jedisClusterNodes = null;

	public RedisClient(String configName) throws Exception {
		this.g_configName = configName;
		g_Config = ConfigManager.getConfig(this.g_configName);
		if (g_Config == null)
			throw new Exception("redis config is null");
		if (g_Config.IsCluster) {
			InitCluster();
		} else {
			InitPool();
		}
	}

	/**
	 * @see 初始化集群
	 */
	private void InitCluster() {
		g_jedisClusterNodes = new HashSet<HostAndPort>();
		for (RedisClusterConfig config : g_Config.ClusterHosts) {
			g_jedisClusterNodes
					.add(new HostAndPort(config.ServerHost, config.ServerPort));
		}
	}

	/**
	 * @see 初始化连接池
	 */
	private void InitPool() {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(g_Config.MaxTotal); // maximum active connections
			config.setMaxIdle(g_Config.MaxIdle); // maximum idle connections
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);
			g_jedisPool = new JedisPool(config, g_Config.ServiceHost,
					g_Config.ServicePort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean setObject(String key, Object value) {
		if (g_Config.IsCluster) {
			return c_setObject(key, value);
		} else {
			return p_setObject(key, value);
		}
	}

	private boolean c_setObject(String key, Object value) {
		boolean r = true;
		JedisCluster jc =null;
		try {
			 jc = new JedisCluster(g_jedisClusterNodes);
			jc.set(key, JsonConvert.SerializeObject(value));
		} catch (Exception e) {
			r = false;
			e.printStackTrace();
		}
		if(jc!=null) {
			try {
				jc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return r;
	}

	private boolean p_setObject(String key, Object value) {
		boolean r = true;
		if (g_jedisPool != null) {
			Jedis redisResource = null;
			try {
				redisResource = g_jedisPool.getResource();
				redisResource.set(key, JsonConvert.SerializeObject(value));

			} catch (Exception e) {
				r = false;
				e.printStackTrace();
			}
			if (redisResource != null)
				redisResource.close();
		}
		return r;
	}

	@Override
	public boolean setObject(String key, Object value, int seconds) {
		if (g_Config.IsCluster) {
			return c_setObject(key, value, seconds);
		} else {
			return p_setObject(key, value, seconds);
		}
	}

	private boolean c_setObject(String key, Object value, int seconds) {
		boolean r = true;
		JedisCluster jc=null;
		try {
			 jc = new JedisCluster(g_jedisClusterNodes);
			if (jc.expire(key, seconds) == 1) {
				jc.set(key, JsonConvert.SerializeObject(value));
			}
		} catch (Exception e) {
			r = false;
			e.printStackTrace();
		}
		if(jc!=null) {
			try {
				jc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return r;
	}

	private boolean p_setObject(String key, Object value, int seconds) {
		boolean r = true;
		if (g_jedisPool != null) {
			Jedis redisResource = null;
			try {
				redisResource = g_jedisPool.getResource();
				if (redisResource.expire(key, seconds) == 1) {
					redisResource.set(key, JsonConvert.SerializeObject(value));
				}

			} catch (Exception e) {
				r = false;
				e.printStackTrace();
			}
			if (redisResource != null)
				redisResource.close();
		}
		return r;
	}

	@Override
	public boolean delObject(List<String> keys) {
		if (g_Config.IsCluster) {
			return c_delObject(keys);
		} else {
			return p_delObject(keys);
		}
	}

	private boolean c_delObject(List<String> keys) {
		boolean r = false;
		JedisCluster jc =null;
		try {
			 jc = new JedisCluster(g_jedisClusterNodes);
			r = (jc.del((String[]) keys.toArray(new String[keys.size()])) > 0);
		
		} catch (Exception e) {
			r = false;
			e.printStackTrace();
		}
		if(jc!=null) {
			try {
				jc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return r;
	}

	private boolean p_delObject(List<String> keys) {
		boolean r = false;
		if (g_jedisPool != null) {
			Jedis redisResource = null;
			try {
				redisResource = g_jedisPool.getResource();
				r = (redisResource.del(
						(String[]) keys.toArray(new String[keys.size()])) > 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (redisResource != null)
				redisResource.close();
		}
		return r;
	}

	@Override
	public <T> T getObject(String key, Class<T> valueType) {
		if (g_Config.IsCluster) {
			return c_getObject(key, valueType);
		} else {
			return p_getObject(key, valueType);
		}
	}

	private <T> T c_getObject(String key, Class<T> valueType) {
		JedisCluster jc =null;
		try {
			 jc = new JedisCluster(g_jedisClusterNodes);
			String value = jc.get(key);
			jc.close();
			return JsonConvert.DeserializeObject(value, valueType);
		} catch (Exception e) {

			e.printStackTrace();
		}
		if(jc!=null) {
			try {
				jc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private <T> T p_getObject(String key, Class<T> valueType) {
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
