package com.syuyndukov.library.library_managemen.config;

import com.syuyndukov.library.library_managemen.service.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    // 2. Наш UserDetailsService. Spring Security будет его использовать для загрузки UserDetails по логину.
    // Spring Boot часто найдет его сам, если он помечен @Service, но явное объявление тут полезно.
    // Мы внедряем сюда наш CustomUserDetailsService (который сам внедряет наш UserService)
    @Bean
    public UserDetailsService userDetailsService(CustomUserDetailsService customUserDetailsService){
        return customUserDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);// Указываем, как найти пользователя
        authenticationProvider.setPasswordEncoder(passwordEncoder);// Указываем как проверить роль

        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll() // Главная, логин, регистрация, статика (CSS/JS/картинки)
                                .requestMatchers("/books", "/books/{id}").permitAll()//каталог книг
                                .requestMatchers("/admin/**").hasRole("ADMIN") // URL, начинающиеся с /admin/ доступны только ADMIN
                                .requestMatchers("/users/**").hasRole("ADMIN") // CRUD пользователей только ADMIN
                                .requestMatchers("/roles/**").hasRole("ADMIN") // CRUD ролей только ADMIN
                                .requestMatchers("/permission/**").hasRole("ADMIN")

                                .requestMatchers("/books/new", "/books/{id}/edit", "/books/{id}/delete").hasAnyRole("ADMIN", "LIBRARIAN")
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
