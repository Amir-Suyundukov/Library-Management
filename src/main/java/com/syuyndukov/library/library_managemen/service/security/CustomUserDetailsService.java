package com.syuyndukov.library.library_managemen.service.security;

import com.syuyndukov.library.library_managemen.domain.User;
import com.syuyndukov.library.library_managemen.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Этот класс сообщает Spring Security, как загружать информацию о пользователе из нашей БД
@Service // Делаем этот класс Spring бином, чтобы его можно было внедрить в SecurityConfig
// @Transactional(readOnly = true) // Можно добавить, чтобы обеспечить транзакцию при загрузке пользователя, особенно если в User.getAuthorities() LAZY-коллекции
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService){
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с логином '" + username + "' не найден"));
        return user;
    }
}
