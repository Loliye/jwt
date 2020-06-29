package com.mikufans.jwt.config.shiro.jwt;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.mikufans.jwt.exception.CustomException;
import com.mikufans.jwt.model.common.Constant;
import com.mikufans.jwt.model.common.ResponseBean;
import com.mikufans.jwt.util.JwtUtil;
import com.mikufans.jwt.util.RedisUtil;
import com.mikufans.jwt.util.common.JsonConvertUtil;
import com.mikufans.jwt.util.common.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter
{
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
    {
        if (this.isLoginAttempt(request, response))
        {
            try
            {
                this.executeLogin(request, response);
            } catch (Exception e)
            {
                String msg = e.getMessage();
                // 获取应用异常(该Cause是导致抛出此throwable(异常)的throwable(异常))
                Throwable throwable = e.getCause();
                if (throwable instanceof SignatureVerificationException)
                {
                    // 该异常为JWT的AccessToken认证失败(Token或者密钥不正确)
                    msg = "Token或者密钥不正确(" + throwable.getMessage() + ")";
                } else if (throwable instanceof TokenExpiredException)
                {
                    // 该异常为JWT的AccessToken已过期，判断RefreshToken未过期就进行AccessToken刷新
                    if (this.refreshToken(request, response))
                        return true;
                    else msg = "Token已过期(" + throwable.getMessage() + ")";
                } else
                {
                    // 应用异常不为空
                    if (throwable != null)
                    {
                        // 获取应用异常msg
                        msg = throwable.getMessage();
                    }
                }
                // Token认证失败直接返回Response信息
                this.response401(response, msg);
                return false;
            }
        } else
        {
            //没有携带token
            HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
            String httpMethod = httpServletRequest.getMethod();
            String uri = httpServletRequest.getRequestURI();
            log.info("当前请求 {} Authorization属性(Token)为空 请求类型 {}", uri, httpMethod);
            final Boolean mustLoginFlag = false;
            if (mustLoginFlag)
            {
                this.response401(response, "请先登陆");
                return false;
            }
        }
        return true;
    }

    private boolean refreshToken(ServletRequest request, ServletResponse response)
    {
        //拿到authorization的accessToken(shiro中getAuthzHeader)
        String token = this.getAuthzHeader(request);
        //获取当前token中的信息
        String account = JwtUtil.getClaim(token, Constant.ACCOUNT);

        if (RedisUtil.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account))
        {
            //redis中还存在refreshToken，获取时间戳 进行匹配更新
            String currentTime = String.valueOf(System.currentTimeMillis());
            PropertiesUtil.readProperties("config.properties");
            String refreshTimeExpireTime = PropertiesUtil.getProperty("refreshTokenExpireTime");
            //进行更新时间戳 并设置过期时间
            RedisUtil.setObject(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account, currentTime, Integer.parseInt(refreshTimeExpireTime));
            //刷新token
            token = JwtUtil.sign(account, currentTime);
            //将双星的accessToken再次登陆
            JwtToken jwtToken = new JwtToken(token);
            this.getSubject(request, response).login(jwtToken);

            // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
            HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
            httpServletResponse.setHeader("Authorization", token);
            httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
            return true;
        }
        return false;
    }

    private void response401(ServletResponse response, String msg)
    {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        try
        {
            PrintWriter printWriter = httpServletResponse.getWriter();
            String data = JsonConvertUtil.objectToJson(new ResponseBean(HttpStatus.UNAUTHORIZED.value(),
                    "无权访问(Unauthorized):" + msg, null));
            printWriter.append(data);
        } catch (IOException e)
        {
            log.error("直接返回Response信息出现IOException异常:{}", e.getMessage());
            throw new CustomException("直接返回Response信息出现IOException异常:" + e.getMessage());
        }
    }

    /**
     * 上面使用了redis token进行登陆  重写父类方法，防止再次登陆
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception
    {
        this.sendChallenge(request, response);
        return false;
    }

    /**
     * 检测header中是否包含authorization字段，有就进行token登陆认证
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response)
    {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        return token != null;
    }

    /**
     * 进行token登陆 认证授权
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception
    {
        JwtToken token = new JwtToken(this.getAuthzHeader(request));
        //进行登陆  没有异常及正常登陆 异常登陆失败
        this.getSubject(request, response).login(token);
        return true;
    }

    /**
     * 跨域进行处理
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception
    {
        // 跨域已经在OriginFilter处全局配置

//        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
//        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
//        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
//        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
//        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
//        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
//        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name()))
//        {
//            httpServletResponse.setStatus(HttpStatus.OK.value());
//            return false;
//        }

        return super.preHandle(request, response);
    }


}
