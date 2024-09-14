package com.to_do_api.todo_today_api.config;

import org.springframework.context.annotation.Bean;

import com.to_do_api.todo_today_api.services.CheckUserExist;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    public CheckUserExist checkUserExist() {
        return new CheckUserExist();
    }
}
