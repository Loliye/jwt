package com.mikufans.jwt.exception;


/**
 * 自定义的401无权限异常
 */
public class CustomUnauthorizedException extends RuntimeException
{

    public CustomUnauthorizedException(String msg)
    {
        super(msg);
    }

    public CustomUnauthorizedException()
    {
        super();
    }
}
