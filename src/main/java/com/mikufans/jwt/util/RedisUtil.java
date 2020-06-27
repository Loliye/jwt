package com.mikufans.jwt.util;

import com.mikufans.jwt.exception.CustomException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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
                return
            }

        }
    }
}
