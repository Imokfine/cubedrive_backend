package com.imokfine.cubedrive;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.imokfine.cubedrive.mapper")
@SpringBootApplication
public class CubedriveApplication {

    public static void main(String[] args) {
        SpringApplication.run(CubedriveApplication.class, args);
    }

}
