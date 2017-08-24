package hayaa.redis.client;
/**
 * @see 提供服务接口
 * @author hsieh
 *
 */

import java.util.HashMap;

public class RedisService {
	private static HashMap<String, IRedisClient> g_InterfaceSet = 
			new HashMap<String, IRedisClient>();
	public static IRedisClient getService(String configName) {
		if(g_InterfaceSet.containsKey(configName)) {
			return g_InterfaceSet.get(configName);
		}
		try {
			g_InterfaceSet.put(configName, new RedisClient(configName));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return g_InterfaceSet.get(configName);
	}
}
