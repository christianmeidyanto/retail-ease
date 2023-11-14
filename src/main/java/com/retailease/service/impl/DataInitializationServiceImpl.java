package com.retailease.service.impl;

import com.retailease.model.request.RegisterAdminRequest;
import com.retailease.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataInitializationServiceImpl implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final AuthServiceimpl authServiceimpl;

    @Override
    public void run(String... args) throws Exception {

        /*DataInitialization For Admin*/
        if (adminRepository.findAll().isEmpty()){
            RegisterAdminRequest request = RegisterAdminRequest.builder()
                    .name("admin")
                    .username("admin123")
                    .password("jOnrlxgQab")
                    .build();
            authServiceimpl.registerNewAdmin(request);
        }
    }
}
