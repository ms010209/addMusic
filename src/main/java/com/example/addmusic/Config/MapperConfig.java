package com.example.addmusic.Config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.example.resources.mybatis")
public class MapperConfig {
}
