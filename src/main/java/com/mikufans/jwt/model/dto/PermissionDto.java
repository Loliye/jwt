package com.mikufans.jwt.model.dto;

import com.mikufans.jwt.model.entity.Permission;

import javax.persistence.Table;

@Table(name = "permission")
public class PermissionDto extends Permission
{
}
