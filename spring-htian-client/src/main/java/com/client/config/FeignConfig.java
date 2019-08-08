package com.client.config;

import feign.Logger;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * feign接口配置类
 * @author lizehao
 * @date 2019/04/15
 */
@Configuration
public class FeignConfig {

    /**
     * 配置日志输出级别
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    public static int connectTimeOutMillis = 15000;//超时时间
    public static int readTimeOutMillis = 15000;

    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeOutMillis, readTimeOutMillis);
    }
}
