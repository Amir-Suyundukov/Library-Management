package com.syuyndukov.library.library_managemen.impl;

import com.syuyndukov.library.library_managemen.domain.Role;
import com.syuyndukov.library.library_managemen.domain.User;
import com.syuyndukov.library.library_managemen.dto.UserCreateDto;
import com.syuyndukov.library.library_managemen.dto.UserResponseDto;
import com.syuyndukov.library.library_managemen.dto.UserUpdateDto;
import com.syuyndukov.library.library_managemen.mapper.UserMapper;
import com.syuyndukov.library.library_managemen.repository.RoleRepository;
import com.syuyndukov.library.library_managemen.repository.UserRepository;
import com.syuyndukov.library.library_managemen.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }


    @Override
    @Transactional // Этот метод изменяет данные, поэтому должна быть транзакция на запись
    public UserResponseDto createUser(UserCreateDto userDto) {
        //TODO: реализовать проверку на уникальность username и email перед созданием,
        //TODO: чтобы предотвратить ошибки базы данных и предоставить более понятное сообщение

        User user = userMapper.toEntity(userDto);

        String rawPassword = userDto.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        user.setPasswordHash(encPassword);

        user.setEnabled(true);
        // TODO: Возможно, эту логику стоит вынести или сделать более гибкой
        //  (например, принимать список ролей в DTO для админа)

        // TODO: Обработать случай, если роль READER не найдена
        //  (например, создать ее при старте приложения или кинуть исключение)

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }


    @Override
    public Optional<UserResponseDto> findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(userMapper::toDto);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional;
    }

    @Override
    public List<UserResponseDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        Optional<User> userOptional = userRepository.findById(id);

        // TODO: Использовать свое кастомное исключение UserNotFoundException
        User userToUpdate = userOptional.orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        // TODO: Добавить проверки прав доступа! Например, только ADMIN может менять enabled,
        // TODO: или пользователь может менять только СВОЙ профиль.
        // TODO: Эту логику можно добавить здесь или на уровне сервисного слоя выше,
        // TODO: или на уровне контроллера с помощью аннотаций Spring Security (@PreAuthorize).

        userMapper.updateEntityFromDto(userUpdateDto, userToUpdate);
        return userMapper.toDto(userToUpdate);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UserResponseDto assignRoleToUser(Long userId, String roleName) {
        // TODO: Использовать свое кастомное исключение UserNotFoundException
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // TODO: Использовать свое кастомное исключение RoleNotFoundException
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName));

        user.addRole(role);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto removeRoleFromUser(Long userId, String roleName) {
        // TODO: Использовать свое кастомное исключение UserNotFoundException
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // TODO: Использовать свое кастомное исключение RoleNotFoundException
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName));

        user.removeRole(role);

        return userMapper.toDto(user);
    }

    //    @Override
//    @Transactional(readOnly = true)
    public Optional<User> findEntityByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // TODO: Добавить метод для изменения пароля!
}
