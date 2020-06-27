package com.mikufans.jwt.modle.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.tomcat.jni.User;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Table(name = "user")
@Data
public class UserDto extends User
{
    /**
     * 登录时间
     */
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTime;

}
