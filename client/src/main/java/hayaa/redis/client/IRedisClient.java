package hayaa.redis.client;

import java.util.List;

public interface IRedisClient {
   boolean setObject(String key,Object value);
   boolean setObject(String key,Object value,int seconds);
   <T> T getObject(String key, Class<T> valueType);
   boolean delObject(List<String> keys);
}
