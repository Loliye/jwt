package com.mikufans.jwt.util;

import com.mikufans.jwt.exception.CustomException;
import com.mikufans.jwt.mapper.UserMapper;
import com.mikufans.jwt.model.common.Constant;
import com.mikufans.jwt.model.dto.UserDto;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShiroUtil
{
    @Autowired
    private UserMapper userMapper;


    /**
     * 获取登陆用户
     *
     * @return
     */
    public UserDto getUser()
    {
        String token = SecurityUtils.getSubject().getPrincipal().toString();
        String account = JwtUtil.getClaim(token, Constant.ACCOUNT);
        UserDto userDto = new UserDto();
        userDto.setAccount(account);
        userDto = userMapper.selectOne(userDto);
        if (userDto == null)
            throw new CustomException("该帐号不存在(The account does not exist.)");
        return userDto;
    }

    public Integer getUserId()
    {
        return getUser().getId();
    }

    public String getToken()
    {
        return SecurityUtils.getSubject().getPrincipal().toString();
    }

    /**
     * 获取登陆user的account
     *
     * @return
     */
    public String getAccount()
    {
        String token = SecurityUtils.getSubject().getPrincipal().toString();
        return JwtUtil.getClaim(token, Constant.ACCOUNT);
    }


}
