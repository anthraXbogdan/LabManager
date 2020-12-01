package com.vaadin.tutorial.crm.backend.services.roleService;

import com.vaadin.tutorial.crm.backend.entities.Role;
import com.vaadin.tutorial.crm.backend.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
