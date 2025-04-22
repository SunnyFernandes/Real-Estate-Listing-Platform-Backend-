package com.relp.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception{
        http.csrf().disable().cors().disable();
        http.authorizeHttpRequests().anyRequest().permitAll();
        return http.build();
    }
}
