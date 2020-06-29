package com.mikufans.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.mikufans.jwt.mapper")
public class JwtApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(JwtApplication.class, args);
    }

}
