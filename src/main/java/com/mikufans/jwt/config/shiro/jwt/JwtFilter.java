package com.mikufans.jwt.config.shiro.jwt;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class JwtFilter extends BasicHttpAuthenticationFilter
{
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
    {
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception
    {
        return super.onAccessDenied(request, response);
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response)
    {
        return super.isLoginAttempt(request, response);
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception
    {
        return super.executeLogin(request, response);
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception
    {
        return super.preHandle(request, response);
    }


}
