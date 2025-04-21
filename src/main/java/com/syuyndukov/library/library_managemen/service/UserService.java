package com.syuyndukov.library.library_managemen.service;

import com.syuyndukov.library.library_managemen.domain.User;
import com.syuyndukov.library.library_managemen.dto.UserCreateDto;
import com.syuyndukov.library.library_managemen.dto.UserResponseDto;
import com.syuyndukov.library.library_managemen.dto.UserUpdateDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponseDto createUser(UserCreateDto userDto);

    Optional<UserResponseDto> findById(Long id);

    Optional<UserResponseDto> findByUsername(String username);

    List<UserResponseDto> findAllUsers();

    UserResponseDto updateUser(Long id, UserUpdateDto userUpdateDto);

    void deleteUser(Long id);

    User assignRoleToUser(Long userId, String roleName);

    User removeRoleFromUser(Long userId, String roleName);


}
