package com.mikufans.jwt.mapper;

import com.mikufans.jwt.model.dto.PermissionDto;
import com.mikufans.jwt.model.dto.RoleDto;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PermissionMapper extends Mapper<PermissionDto>
{
    List<PermissionDto> findPermissionByRole(RoleDto roleDto);
}
