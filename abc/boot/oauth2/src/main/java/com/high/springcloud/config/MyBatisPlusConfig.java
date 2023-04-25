package com.high.springcloud.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Configuration
@MapperScan("com.high.springcloud.mapper")
public class MyBatisPlusConfig {
}
