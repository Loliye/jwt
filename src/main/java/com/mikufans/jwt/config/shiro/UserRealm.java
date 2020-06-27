package com.mikufans.jwt.config.shiro;

import com.mikufans.jwt.config.shiro.jwt.JwtToken;
import com.mikufans.jwt.mapper.PermissionMapper;
import com.mikufans.jwt.mapper.RoleMapper;
import com.mikufans.jwt.mapper.UserMapper;
import com.mikufans.jwt.modle.common.Constant;
import com.mikufans.jwt.modle.dto.PermissionDto;
import com.mikufans.jwt.modle.dto.RoleDto;
import com.mikufans.jwt.modle.dto.UserDto;
import com.mikufans.jwt.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class UserRealm extends AuthorizingRealm
{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;


    @Override
    public boolean supports(AuthenticationToken token)
    {
        return token instanceof JwtToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
    {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String account = JwtUtil.getClaim(principals.toString(), Constant.ACCOUNT);
        UserDto userDto = new UserDto();
        userDto.setAccount(account);
        List<RoleDto> roleDtos = roleMapper.findRoleByUser(userDto);
        for (RoleDto roleDto : roleDtos)
        {
            if (roleDto != null)
            {
                info.addRole(roleDto.getName());
                List<PermissionDto> permissionDtos = permissionMapper.findPermissionByRole(roleDto);
                for (PermissionDto permissionDto : permissionDtos)
                    if (permissionDto != null)
                        info.addStringPermission(permissionDto.getPerCode());
            }
        }
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
    {
        String secret = (String) token.getCredentials();
        String account = JwtUtil.getClaim(secret, Constant.ACCOUNT);
        if (StringUtils.isBlank(account))
            throw new AuthenticationException("Token中帐号为空(The account in Token is empty.)");

        UserDto userDto = new UserDto();
        userDto.setAccount(account);
        userDto = userMapper.selectOne(userDto);
        if (userDto == null)
            throw new AuthenticationException("该帐号不存在(The account does not exist.)");

        //认证
        if(JwtUtil.verify(secret)&&)



    }
}
