package com.syuyndukov.library.library_managemen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Метод, помеченный @Bean, говорит Спрингу создать объект BCryptPasswordEncoder
    // и управлять им как Spring Bean.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                //TODO: определить правила доступа для разных URL
                                // Пример: разрешить всем доступ к главной странице и странице входа
                                // .requestMatchers("/", "/login").permitAll()
                                // .requestMatchers("/admin/**").hasRole("ADMIN") // Только для пользователей с ролью ADMIN
                                // .requestMatchers("/books/**").hasAnyRole("ADMIN", "LIBRARIAN", "READER") // Для нескольких ролей
                                // .requestMatchers("/users/**").hasAuthority("USER_WRITE") // Только для тех, у кого есть право USER_WRITE
                                // А все остальные запросы требуют аутентификации
                                .anyRequest().authenticated())// ПОКА ЧТО: ВСЕ ЗАПРОСЫ ТРЕБУЮТ АУТЕНТИФИКАЦИИ
                //2. Настройка формы входа
                .formLogin(formLogin -> formLogin
                        // TODO: Настроить свою страницу входа и перенаправления
                        .permitAll())
                .logout(logout -> logout.permitAll()); // TODO: Настроить URL выхода и перенаправления
        // 4. Отключение CSRF защиты (ТОЛЬКО ДЛЯ ПРИМЕРОВ API БЕЗ UI, ДЛЯ UI ОБЫЧНО ВКЛЮЧЕНА!)
        // Для веб-приложения с Thymeleaf CSRF должен быть ВКЛЮЧЕН по умолчанию!
        // Если вдруг отключаешь (для тестов API без токенов), то так:
        // .csrf(csrf -> csrf.disable());

        // 5. Другие настройки (например, exception handling, remember-me и т.д.)
        // .exceptionHandling(...)

        // Строим и возвращаем фильтр-цепочку безопасности
        return http.build();
    }
    // --- TODO: Нужен Bean UserDetailsService для загрузки пользователя по логину ---
    // Spring Security нужно знать, как получить данные пользователя (Username, PasswordHash, Enabled, Authorities)
    // по его логину. Мы реализуем стандартный интерфейс UserDetailsService,
    // который вызовет наш UserService.findEntityByUsername().
    // Этот UserDetailsService Bean будет внедрен автоматически в AuthenticationProvider.
    // @Bean
    // public UserDetailsService userDetailsService() {
    //     // Нужно создать класс, который реализует UserDetailsService и использует ваш UserService
    //     return new YourCustomUserDetailsService(yourUserService);
    // }

    // TODO: Возможно, настроить AuthenticationProvider явно, хотя часто Spring Boot делает это сам,
    // TODO: если видит UserDetailsService и PasswordEncoder Bean'ы.
    // @Bean
    // public AuthenticationProvider authenticationProvider() {
    //     DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    //     authProvider.setUserDetailsService(userDetailsService()); // Использовать наш UserDetailsService
    //     authProvider.setPasswordEncoder(passwordEncoder()); // Использовать наш PasswordEncoder
    //     return authProvider;
    // }

    // Здесь же позже мы добавим основную конфигурацию Spring Security
    // например, SecurityFilterChain bean.
}
