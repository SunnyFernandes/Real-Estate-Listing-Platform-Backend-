package com.relp.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {
    private JWTFilter jwtFilter;

    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception{
        http.csrf().disable().cors().disable();
//        http.authorizeHttpRequests().anyRequest().permitAll();

        http.addFilterBefore(jwtFilter, AuthorizationFilter.class);
        http.authorizeHttpRequests().requestMatchers(
                "/api/relp/auth/add-buyer",
                        "/api/relp/auth/add-admin",
                        "/api/relp/auth/add-seller",
                        "/api/relp/auth/login"
        ).permitAll()
                .requestMatchers(
                        "/api/relp/location/add-location",
                        "/api/relp/location/update-location",
                        "/api/relp/location/delete-location-by-id",
                        "/api/relp/user/get-all-users",
                        "/api/relp/user/delete-user-by-id"
                ).hasRole("ADMIN")
                .requestMatchers(
                        "/api/relp/property/add-property",
                        "/api/relp/property/update-property",
                        "/api/relp/property/delete-property-by-id"
//                        "/api/relp/upload-image/upload/file/{bucketName}/property/{propertyId}"
                ).hasRole("SELLER")
                .requestMatchers(
                        "/api/relp/location/get-location-by-id",
                        "/api/relp/location/get-all-location",
                        "/api/relp/property/get-property-by-id",
                        "/api/relp/property/get-all-property",
                        "/api/relp/property/search-filter-property",
                        "/api/relp/user/update-user",
                        "/api/relp/user/get-by-email",
                        "/api/relp/user/delete-user"
                ).hasAnyRole("BUYER","SELLER","ADMIN")
                .anyRequest().authenticated();
        return http.build();
    }

}
