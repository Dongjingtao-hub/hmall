package com.hmall.api.config;
import feign.Logger;
import org.springframework.context.annotation.Bean;


/**
 * Author: ${董靖涛}
 * Date: 2026/6/29
 */
public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
