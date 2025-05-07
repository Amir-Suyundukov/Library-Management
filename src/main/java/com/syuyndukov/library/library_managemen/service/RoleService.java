package com.syuyndukov.library.library_managemen.service;

import com.syuyndukov.library.library_managemen.domain.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);

    Role createRole(String name);

    // TODO: Возможно, добавить другие методы позже, если понадобится CRUD для ролей через админку
    //  Role updateRole(Long id, RoleUpdateDto roleDetails);
    //  void deleteRole(Long id);
    //  List<Role> findAllRoles();
}
