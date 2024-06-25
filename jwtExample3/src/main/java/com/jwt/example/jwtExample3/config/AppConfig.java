package com.jwt.example.jwtExample3.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static org.springframework.security.core.userdetails.User.builder;

@Configuration
public class AppConfig {
    @Bean
    public UserDetailsService userDetailsService(){

       UserDetails user =  User.builder().username("harsh").password(passwordEncoder().encode("abc")).roles("ADMIN").build();

        return new InMemoryUserDetailsManager(user);
    };

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
