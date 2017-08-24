package hayaa.redis.client;

public interface IRedisClient {
  public void setObject(String key,Object value);
  public <T> T getObject(String key, Class<T> valueType);
}
