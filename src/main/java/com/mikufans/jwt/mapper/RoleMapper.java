package com.mikufans.jwt.mapper;

import com.mikufans.jwt.model.dto.RoleDto;
import com.mikufans.jwt.model.dto.UserDto;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RoleMapper extends Mapper<RoleDto>
{
    List<RoleDto> findRoleByUser(UserDto userDto);
}
