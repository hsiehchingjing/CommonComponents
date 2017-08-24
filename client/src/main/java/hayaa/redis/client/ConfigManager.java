package hayaa.redis.client;
/**
 * @see redis配置管理
 * @author hsieh
 *
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;


import hayaa.redis.client.config.RedisConfig;
import hayaa.redis.client.config.RedisGroup;

final class ConfigManager {
	private static HashMap<String, RedisConfig> g_configSet = new HashMap<String, RedisConfig>();
	static {
		loadConfig();
	}
	static RedisConfig getConfig(String name) {
		if(g_configSet.containsKey(name)) {
			return g_configSet.get(name);
		}
		return null;
	}
	private static void loadConfig() {
		// 读取配置文件
		Properties props = System.getProperties();
		String baseDirectory = props.getProperty("user.dir");
		try {
			RedisGroup group = JsonConvert.DeserializeObject(
					FileHelper.ReadAllText(baseDirectory + "/redis.config"),
					RedisGroup.class);
			if ((group != null) && (group.configs != null)) {
				for (int i = 0; i < group.configs.length; i++) {
					g_configSet.put(group.configs[i].ConfigName,
							group.configs[i]);
				}
			}
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
	public static void reloadConfig() {
		g_configSet.clear();
		loadConfig();
	}
}
