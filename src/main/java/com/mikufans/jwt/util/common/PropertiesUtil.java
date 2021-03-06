package com.mikufans.jwt.util.common;

import com.mikufans.jwt.exception.CustomException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

@Slf4j
public class PropertiesUtil
{

    /**
     * PROP
     */
    private static final Properties PROP = new Properties();


    private PropertiesUtil() {}

    /**
     * 读取配置文件
     */
    public static void readProperties(String fileName)
    {
        InputStream in = null;
        try
        {
            in = PropertiesUtil.class.getResourceAsStream("/" + fileName);
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            PROP.load(bf);
        } catch (IOException e)
        {
            log.error("PropertiesUtil工具类读取配置文件出现IOException异常:{}", e.getMessage());
            throw new CustomException("PropertiesUtil工具类读取配置文件出现IOException异常:" + e.getMessage());
        } finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            } catch (IOException e)
            {
                log.error("PropertiesUtil工具类读取配置文件出现IOException异常:{}", e.getMessage());
                throw new CustomException("PropertiesUtil工具类读取配置文件出现IOException异常:" + e.getMessage());
            }
        }
    }

    /**
     * 根据key读取对应的value
     */
    public static String getProperty(String key)
    {
        return PROP.getProperty(key);
    }
}
