package com.cognizant.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cognizant.filter.JwtLoginFilter;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean jwtAdminFilter() {

        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new JwtLoginFilter());
        filter.addUrlPatterns("/api/v1/*");

        return filter;
    }
}
