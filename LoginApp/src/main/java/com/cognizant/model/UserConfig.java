package com.cognizant.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
    @Bean(name="User")
    public User user(){
        return new User();
    }
}
