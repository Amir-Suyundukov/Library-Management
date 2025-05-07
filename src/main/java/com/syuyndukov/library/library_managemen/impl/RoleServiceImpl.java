package com.syuyndukov.library.library_managemen.impl;

import com.syuyndukov.library.library_managemen.domain.Role;
import com.syuyndukov.library.library_managemen.repository.RoleRepository;
import com.syuyndukov.library.library_managemen.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    @Transactional
    public Role createRole(String name) {
        Role role = new Role(name);

        return roleRepository.save(role);
    }
}
