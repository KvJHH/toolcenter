package top.superookie.toolcenter;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class RedisOperate {

    private static volatile JedisPool jedisPool;

    private static final String HOST = System.getProperty("redis.host", ConfigProperty.get("redis.host"));
    private static final int PORT = Integer.parseInt(System.getProperty("redis.port", ConfigProperty.get("redis.port")));
    private static final String AUTH = System.getProperty("redis.auth", ConfigProperty.get("redis.auth"));
    private static final int TIMEOUT = 10000;

    private static JedisPool getJedisPool() {
        if (jedisPool == null) {
            synchronized (RedisOperate.class) {
                if (jedisPool == null) {
                    System.out.println("实例化jedisPool");
                    jedisPool = new JedisPool(new JedisPoolConfig(), HOST, PORT, TIMEOUT, AUTH);
                }
            }
        }
        return jedisPool;
    }

    public static Jedis getJedis(int dbIndex) {
        Jedis jedis = getJedisPool().getResource();
        jedis.select(dbIndex);
        return jedis;
    }


}
