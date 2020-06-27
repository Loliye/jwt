package com.mikufans.jwt.util;

import com.mikufans.jwt.exception.CustomException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@Slf4j
public class SerializableUtil
{
    private SerializableUtil() {}

    public static byte[] serializable(Object o)
    {
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try
        {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(objectOutputStream);
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e)
        {
            log.error("SerializableUtil工具类序列化出现IOException异常:{}", e.getMessage());
            throw new CustomException("SerializableUtil工具类序列化出现IOException异常:" + e.getMessage());
        } finally
        {
            {
                try
                {
                    if(objectOutputStream!=null)
                        objectOutputStream.close();
                    if (byteArrayOutputStream != null)
                        byteArrayOutputStream.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }



}
