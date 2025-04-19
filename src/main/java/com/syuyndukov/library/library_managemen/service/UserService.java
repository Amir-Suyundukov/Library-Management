package com.syuyndukov.library.library_managemen.service;

import com.syuyndukov.library.library_managemen.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    List<User> findAllUsers();

    void deleteUser(Long id);

    User assignRoleToUser(Long userId, String roleName);

    User removeRoleFromUser(Long userId, String roleName);


}
