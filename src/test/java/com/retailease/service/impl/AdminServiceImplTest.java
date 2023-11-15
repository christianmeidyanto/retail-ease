package com.retailease.service.impl;

import com.retailease.entity.Admin;
import com.retailease.entity.Role;
import com.retailease.entity.UserCredential;
import com.retailease.entity.constant.ERole;
import com.retailease.model.response.AdminResponse;
import com.retailease.repository.AdminRepository;
import com.retailease.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdminServiceImplTest {
    private final AdminRepository adminRepository=mock(AdminRepository.class);
    private final AdminService adminService= new AdminServiceImpl(adminRepository);

    @Test
    void getAdminByUsername() {
        List<Role> roles= new ArrayList<>();
        roles.add(new Role("role1",ERole.ROLE_ADMIN));

        UserCredential userCredential = UserCredential.builder()
                .id("123")
                .username("admin123")
                .password("test")
                .roles(roles)
                .build();
        Admin admin = Admin.builder()
                .id("1")
                .name("Admin")
                .userCredential(userCredential)
                .build();

        when(adminRepository.findAdminByUserCredential_Username(anyString())).thenReturn(Optional.ofNullable(admin));

        AdminResponse findAdmin = adminService.getAdminByUsername("admin123");
        verify(adminRepository,times(1)).findAdminByUserCredential_Username(anyString());
        assertEquals(findAdmin.getId(),"1");
        assertEquals(findAdmin.getName(),"Admin");
    }
}