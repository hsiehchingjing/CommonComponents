package hayaa.redis.client;

import static org.junit.Assert.*;

import org.junit.Test;

import hayaa.redis.client.config.RedisConfig;
import hayaa.redis.client.config.RedisGroup;

public class redistest {
	@Test
	public void test() {
			RedisGroup group=new RedisGroup();
			group.configs=new RedisConfig[1];
			group.configs[0]=new RedisConfig();
			 //System.out.println(JsonConvert.SerializeObject(group) );
	}
	@Test
	public void testCreateService() {
		IRedisClient service=RedisService.getService("config1");
		assertNotNull(service);
	}
	@Test
	public void testSet() {
		IRedisClient service=RedisService.getService("config1");
		service.setObject("key1", "value1");
		
	}
	@Test
	public void testGet() {
		IRedisClient service=RedisService.getService("config1");
		service.getObject("key1",String.class);
		
	}
	@Test
	public void testFlow() {
		IRedisClient service=RedisService.getService("config1");
		service.setObject("key1", "value1");
		service.getObject("key1",String.class);
		service.setObject("key2", "value1");
		
	}
}
