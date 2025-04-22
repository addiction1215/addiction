package com.addiction.global.config

import org.springframework.context.annotation.Bean
import feign.Logger;

class FeignConfig {

    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL;
    }
}
