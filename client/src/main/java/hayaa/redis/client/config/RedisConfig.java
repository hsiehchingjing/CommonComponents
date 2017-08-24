package hayaa.redis.client.config;

/**
 * @see redis配置
 * @author hsieh
 *
 */
public class RedisConfig {
	//配置名称
	public String ConfigName;
	//redis服务开放地址
	public String ServiceHost;
	//redis服务开发端口
	public int ServicePort;
	//redis验证用户
	public String ReidsUser;
	//redis验证密码
	public String RedisPwd;
	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	public  int MaxTotal;
	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	public  int MaxIdle ;
	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	public  long MaxWait;
	public  int TimeOut;
}
