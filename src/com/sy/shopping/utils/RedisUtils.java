package com.sy.shopping.utils;

import com.sy.shopping.constant.AppConstant;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.security.Key;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Redis操作相关的工具类
 */
public class RedisUtils {
    private static JedisPool pool;

    private RedisUtils() {

    }

    static {
        Properties props = PropertiesUtils.loadProperties("redis.properties");
        pool = new JedisPool(props.getProperty("redis.host"),
                Integer.parseInt(props.getProperty("redis.port")));
    }

    /**
     * Redis中的set命令
     *
     * @param key   String类型对应Redis中的key
     * @param value String类型在Redis中对应的值
     * @return 是否写入成功
     */
    public static boolean set(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            return StringUtils.equals(jedis.set(key, value), AppConstant.REDIS_OK_RESP);
        }
    }

    /**
     * Redis的get命令
     *
     * @param key Redis中String类型对应的key
     * @return Redis中String类型对应的值
     */
    public static String get(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        }
    }

    /**
     * Redis中的set命令(用于写入序列化对象)
     *
     * @param key String类型对应Redis中的key
     * @param obj 要保存的可序列化对象
     * @return 是否写入成功
     */
    public static boolean set(String key, Serializable obj) {
        try (Jedis jedis = pool.getResource()) {
            return StringUtils.equals(jedis.set(key.getBytes(), SerializationUtils.serialize(obj)), AppConstant.REDIS_OK_RESP);
        }
    }

    /**
     * 从Redis中获取反序列化以后的对象
     *
     * @param key Redis中保存序列化对象的Key
     * @param <T> 泛型参数
     * @return 反序列化以后的对象
     */
    public static <T> T getObj(String key) {
        try (Jedis jedis = pool.getResource()) {
            byte[] bytes = jedis.get(key.getBytes());
            if (null == bytes) {
                return null;
            } else {
                return (T) SerializationUtils.deserialize(bytes);
            }
        }
    }

    /**
     * 删除Redis中指定Key的数据
     *
     * @param keys 要删除的Key(变长参数)
     * @return
     */
    public static boolean del(String... keys) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.del(keys) > 0;
        }
    }

    /**
     * 用于向hash类型中批量写入数据
     *
     * @param key   Redis中hash类型的key
     * @param value Hash类型中要写入的数据
     * @return 是否写入成功
     */
    public static boolean hmset(String key, Map<String, String> value) {
        try (Jedis jedis = pool.getResource()) {
            return StringUtils.equals(AppConstant.REDIS_OK_RESP, jedis.hmset(key, value));
        }
    }


    /**
     * hash类型的hincrby操作
     *
     * @param key   Redis中hash类型的key
     * @param field hash类型中的field
     * @param incr  hash类型中field的增量
     * @return 递增或递减后的数值
     */
    public static Long hincrby(String key, String field, Long incr) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hincrBy(key, field, incr);
        }
    }

    /**
     * hash类型的hgetall命令
     *
     * @param key Redis中hash类型的key
     * @return hash类型中所有的field和value构成的Map集合
     */
    public static Map<String, String> hgetall(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hgetAll(key);
        }
    }

    /**
     * sorted set对应的zadd操作
     *
     * @param key     sorted set在Redis中对应key
     * @param members 要写入的成员
     * @return 是否写入成功
     */
    public static boolean zadd(String key, Map<String, Double> members) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.zadd(key, members) > 0;
        }
    }

    /**
     * sorted set的zrevrange命令
     *
     * @param key   Redis中sorted set的key
     * @param start 起始索引
     * @param end   结束索引（包含）
     * @return 得到的start-end索引范围的成员
     */
    public static Set<String> zrevrange(String key, long start, long end) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.zrevrange(key, start, end);
        }
    }

    /**
     * List类型的lpush命令
     *
     * @param key    List类型的key
     * @param values 要向List中lpush的值
     * @return 是否操作成功
     */
    public static boolean lpush(String key, String... values) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.lpush(key, values) > 0;
        }
    }

    /**
     * List中删除指定value的count个元素
     *
     * @param key   List类型对应的key
     * @param count 要删除的数量
     * @param value 要删除的值
     * @return 是否删除成功
     */
    public static boolean lrem(String key, Long count, String value) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.lrem(key, count, value) > 0;
        }
    }

    /**
     * 获取List中指定索引范围的元素
     *
     * @param key   List对应的key
     * @param start 起始索引
     * @param end   结束索引
     * @return 指定索引范围的元素
     */
    public static List<String> lrange(String key, Long start, Long end) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.lrange(key, start, end);
        }
    }

    /**
     * 获取List的长度
     *
     * @param key List对应的key
     * @return List集合的长度
     */
    public static Long llen(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.llen(key);
        }
    }

    /**
     * 判断Redis中对应的key是否存在
     *
     * @param key 要判断存在性的key
     * @return 是否存在
     */
    public static boolean exists(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(key);
        }
    }

    /**
     * 设置或修改hash类型中的属性值
     *
     * @param key   hash类型的key
     * @param field 属性名
     * @param value 属性值
     */
    public static void hset(String key, String field, String value) {
        try (Jedis jedis = pool.getResource()) {
            jedis.hset(key, field, value);
        }

    }

}
