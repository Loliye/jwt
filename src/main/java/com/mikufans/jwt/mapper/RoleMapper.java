package com.mikufans.jwt.mapper;

import com.mikufans.jwt.modle.dto.RoleDto;
import com.mikufans.jwt.modle.dto.UserDto;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RoleMapper extends Mapper<RoleDto>
{
    List<RoleDto> findRoleByUser(UserDto userDto);
}
