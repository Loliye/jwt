package com.mikufans.jwt.util;

import com.mikufans.jwt.exception.CustomException;
import com.mikufans.jwt.model.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@Component
public class RedisUtil
{
    private static JedisPool jedisPool;

    @Autowired
    public static void setJedisPool(JedisPool jedisPool)
    {
        RedisUtil.jedisPool = jedisPool;
    }

    public static synchronized Jedis getJedis()
    {
        try
        {
            if (jedisPool != null)
                return jedisPool.getResource();
            else return null;
        } catch (Exception e)
        {
            throw new CustomException("获取Jedis资源异常:" + e.getMessage());
        }
    }

    public static void closePool()
    {
        try
        {
            jedisPool.close();
        } catch (Exception e)
        {
            throw new CustomException("释放Jedis资源异常:" + e.getMessage());
        }
    }

    public static Object getObject(String key)
    {
        try
        {
            Jedis jedis = jedisPool.getResource();
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes != null && bytes.length != 0)
            {
                return SerializableUtil.unserializable(bytes);
            }

        } catch (Exception e)
        {
            throw new CustomException("获取Redis键值getObject方法异常:key=" + key + " cause=" + e.getMessage());
        }
        return null;
    }

    public static String setObject(String key, Object value, int expireTime)
    {
        String result;
        try
        {
            Jedis jedis = jedisPool.getResource();
            result = jedis.set(key.getBytes(), SerializableUtil.serializable(value));
            if (Constant.OK.equals(result))
                jedis.expire(key.getBytes(), expireTime);
            return result;
        } catch (Exception e)
        {
            throw new CustomException("设置Redis键值setObject方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
        }
    }

    public static String getJson(String key)
    {
        try
        {
            Jedis jedis = jedisPool.getResource();
            return jedis.get(key);
        } catch (Exception e)
        {
            throw new CustomException("获取Redis键值getJson方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

    public static String setJson(String key, String value)
    {
        try
        {
            Jedis jedis = jedisPool.getResource();
            return jedis.set(key, value);
        } catch (Exception e)
        {
            throw new CustomException("设置Redis键值setJson方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
        }
    }

    public static String setJson(String key, String value, int expireTime)
    {
        try
        {
            Jedis jedis = jedisPool.getResource();
            String result = jedis.set(key, value);
            if (Constant.OK.equals(result))
                jedis.expire(key, expireTime);
            return result;
        } catch (Exception e)
        {
            throw new CustomException("设置Redis键值setJson方法异常:key=" + key + " value=" + value + " cause=" + e.getMessage());
        }
    }

    public static Long delKey(String key)
    {
        try
        {
            Jedis jedis = jedisPool.getResource();
            return jedis.del(key.getBytes());
        } catch (Exception e)
        {
            throw new CustomException("删除Redis的键delKey方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

    public static Boolean exists(String key)
    {
        try
        {
            Jedis jedis = jedisPool.getResource();
            return jedis.exists(key.getBytes());
        } catch (Exception e)
        {
            throw new CustomException("查询Redis的键是否存在exists方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

    /**
     * 模糊查询key的集合
     *
     * @param key
     * @return
     */
    public static Set<String> keyS(String key)
    {
        try
        {
            Jedis jedis = jedisPool.getResource();
            return jedis.keys(key);
        } catch (Exception e)
        {
            throw new CustomException("模糊查询Redis的键集合keysS方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

    public static Set<byte[]> keyB(String key)
    {
        try
        {
            Jedis jedis = jedisPool.getResource();
            return jedis.keys(key.getBytes());
        } catch (Exception e)
        {
            throw new CustomException("模糊查询Redis的键集合keysB方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

    /**
     * 查询过期剩余时间
     * @param key
     * @return
     */
    public static Long ttl(String key)
    {
        Long result = -1L;
        try
        {
            Jedis jedis = jedisPool.getResource();
            result = jedis.ttl(key);
            return result;
        } catch (Exception e)
        {
            throw new CustomException("获取Redis键过期剩余时间ttl方法异常:key=" + key + " cause=" + e.getMessage());
        }
    }

}
