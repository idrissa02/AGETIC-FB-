package com.AGETIC.Civil_service_competition.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // allow static assets
                .requestMatchers("/", "/index", "/mes-inscriptions", "/css/**", "/js/**", "/img/**", "/webjars/**,").permitAll()
                // allow everything else for now
                .anyRequest().permitAll()
            );
        return http.build();
        

     
    }


}
