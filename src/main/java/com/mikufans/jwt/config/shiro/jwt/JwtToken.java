package com.mikufans.jwt.config.shiro.jwt;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

@Data
public class JwtToken implements AuthenticationToken
{
    private String token;

    @Override
    public Object getPrincipal()
    {
        return token;
    }

    @Override
    public Object getCredentials()
    {
        return token;
    }
}