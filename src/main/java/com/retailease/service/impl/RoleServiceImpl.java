package com.retailease.service.impl;

import com.retailease.entity.Role;
import com.retailease.entity.constant.ERole;
import com.retailease.repository.RoleRepository;
import com.retailease.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getOrsave(ERole erole) {
        return roleRepository.findByRole(erole).orElseGet(
                () -> roleRepository.save(Role.builder()
                        .role(erole)
                        .build())
        );
    }
}
