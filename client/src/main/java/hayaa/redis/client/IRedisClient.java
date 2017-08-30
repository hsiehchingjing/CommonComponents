package hayaa.redis.client;

import java.util.List;

public interface IRedisClient {
  public boolean setObject(String key,Object value);
  public boolean setObject(String key,Object value,int seconds);
  public <T> T getObject(String key, Class<T> valueType);
  public boolean delObject(List<String> keys);
}
