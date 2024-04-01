package com.dosu04.memoWebApp.services;

import com.dosu04.memoWebApp.models.Role;
import com.dosu04.memoWebApp.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
}
