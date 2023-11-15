package com.retailease.service.impl;

import com.retailease.entity.Admin;
import com.retailease.entity.Role;
import com.retailease.entity.UserCredential;
import com.retailease.entity.UserDetailImpl;
import com.retailease.entity.constant.ERole;
import com.retailease.model.request.LoginRequest;
import com.retailease.model.request.RegisterAdminRequest;
import com.retailease.model.response.LoginResponse;
import com.retailease.model.response.RegisterAdminResponse;
import com.retailease.repository.AdminRepository;
import com.retailease.repository.UserCredentialRepository;
import com.retailease.security.BcryptUtils;
import com.retailease.security.JwtUtils;
import com.retailease.service.AuthService;
import com.retailease.service.RoleService;
import com.retailease.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@SpringBootTest
class AuthServiceimplTest {

    private final ValidationService validationService = mock(ValidationService.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final Authentication authentication = mock(Authentication.class);
    private final UserDetailImpl userDetail=mock(UserDetailImpl.class);
    private final JwtUtils jwtUtils = mock(JwtUtils.class);
    private final AdminRepository adminRepository = mock(AdminRepository.class);
    private final BcryptUtils bcryptUtils = mock(BcryptUtils.class);
    private final RoleService roleService = mock(RoleService.class);
    private final UserCredentialRepository userCredentialRepository = mock(UserCredentialRepository.class);
    private final AuthService authService = new AuthServiceimpl(validationService, authenticationManager, jwtUtils, adminRepository, bcryptUtils, roleService, userCredentialRepository);

    @Test
    void registerNewAdmin() {
        Role role = Role.builder()
                .id("role1")
                .role(ERole.ROLE_ADMIN)
                .build();
        UserCredential userCredential = UserCredential.builder()
                .id("123")
                .username("admin123")
                .password("test")
                .build();

        RegisterAdminRequest request = RegisterAdminRequest.builder()
                .name("Sandi")
                .username("admin123")
                .password("root")
                .build();

        Admin admin = Admin.builder()
                .id("1")
                .name("Sandi")
                .build();

        when(roleService.getOrsave(any(ERole.class))).thenReturn(role);
        when(bcryptUtils.hashPassword(anyString())).thenReturn("password");
        when(userCredentialRepository.saveAndFlush(any(UserCredential.class))).thenReturn(userCredential);

        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        RegisterAdminResponse adminResponse = authService.registerNewAdmin(request);
        assertNotNull(adminResponse);
        verify(adminRepository, times(1)).save(any(Admin.class));

    }
}