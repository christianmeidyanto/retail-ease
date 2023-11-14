package com.retailease.service.impl;

import com.retailease.entity.Admin;
import com.retailease.model.response.AdminResponse;
import com.retailease.repository.AdminRepository;
import com.retailease.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    @Override
    public AdminResponse getAdminByUsername(String username) {
        Admin admin = adminRepository.findAdminByUserCredential_Username(username).get();
        return AdminResponse.builder()
                .id(admin.getId())
                .name(admin.getName())
                .build();
    }

    @Override
    public Admin getById(String id){
        return adminRepository.findById(id).get();
    }
}
