package com.syuyndukov.library.library_managemen.impl;

import com.syuyndukov.library.library_managemen.domain.User;
import com.syuyndukov.library.library_managemen.dto.UserCreateDto;
import com.syuyndukov.library.library_managemen.dto.UserResponseDto;
import com.syuyndukov.library.library_managemen.dto.UserUpdateDto;
import com.syuyndukov.library.library_managemen.mapper.UserMapper;
import com.syuyndukov.library.library_managemen.repository.RoleRepository;
import com.syuyndukov.library.library_managemen.repository.UserRepository;
import com.syuyndukov.library.library_managemen.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        User user = userMapper.toEntity(userCreateDto);

        String rawPassword = userCreateDto.getPassword();
        String encoderPass = passwordEncoder.encode(rawPassword);
        user.setPasswordHash(encoderPass);

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public List<User> findAllUsers() {
        return List.of();
    }

    @Override
    public UserResponseDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        Optional<User> userOptional = userRepository.findById(id);// находим пользователя по id

        if (!userOptional.isPresent()){//если пользователь не найден выкидываем исключение
            throw new RuntimeException("User nit found with id: " + id);
        }

        User userToUpdate = userOptional.get();// получаем сущность User из Optional

        userMapper.updateEntityFromDto(userUpdateDto ,userToUpdate);
        return null;
    }

    @Override
    public void deleteUser(Long id) {
    }

    @Override
    public User assignRoleToUser(Long userId, String roleName) {
        return null;
    }

    @Override
    public User removeRoleFromUser(Long userId, String roleName) {
        return null;
    }

//    @Override
//    @Transactional(readOnly = true)
    public Optional<User> findEntityByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
