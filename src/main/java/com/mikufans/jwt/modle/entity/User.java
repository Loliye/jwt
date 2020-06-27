package com.mikufans.jwt.modle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable
{
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 帐号
     */
//    @NotNull(message = "帐号不能为空", groups = { UserLoginValidGroup.class, UserEditValidGroup.class })
    private String account;

    /**
     * 密码
     */
//    @NotNull(message = "密码不能为空", groups = { UserLoginValidGroup.class, UserEditValidGroup.class })
    private String password;

    /**
     * 昵称
     */
//    @NotNull(message = "用户名不能为空", groups = { UserEditValidGroup.class })
    private String username;

    /**
     * 注册时间
     */
    @Column(name = "reg_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date regTime;

}
