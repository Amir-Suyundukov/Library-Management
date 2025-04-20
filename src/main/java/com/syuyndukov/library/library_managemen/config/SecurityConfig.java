package com.syuyndukov.library.library_managemen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    // Метод, помеченный @Bean, говорит Спрингу создать объект BCryptPasswordEncoder
    // и управлять им как Spring Bean.
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Здесь же позже мы добавим основную конфигурацию Spring Security
    // например, SecurityFilterChain bean.
}
