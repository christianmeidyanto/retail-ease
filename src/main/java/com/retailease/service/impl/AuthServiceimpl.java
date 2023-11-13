package com.retailease.service.impl;

import com.retailease.entity.Admin;
import com.retailease.entity.Role;
import com.retailease.entity.UserCredential;
import com.retailease.entity.UserDetailImpl;
import com.retailease.entity.constant.ERole;
import com.retailease.model.request.LoginRequest;
import com.retailease.model.request.RegisterAdminRequest;
import com.retailease.model.response.CommonResponse;
import com.retailease.model.response.LoginResponse;
import com.retailease.model.response.RegisterAdminResponse;
import com.retailease.repository.AdminRepository;
import com.retailease.repository.UserCredentialRepository;
import com.retailease.security.BcryptUtils;
import com.retailease.security.JwtUtils;
import com.retailease.service.AuthService;
import com.retailease.service.RoleService;
import com.retailease.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceimpl implements AuthService {
    private final ValidationService validationService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AdminRepository adminRepository;
    private final BcryptUtils bcryptUtils;
    private final RoleService roleService;

    private final UserCredentialRepository userCredentialRepository;

//    @Transactional
//    @Override
//    public RegisterCustomerResponse registerNewCustomer(RegisterCustomerRequest request) {
//        validationService.validate(request);
//        try {
//            Role role = roleService.getOrsave(ERole.ROLE_CUSTOMER);
//            UserCredential userCredential = UserCredential.builder()
//                    .username(request.getUsername())
//                    .password(bcryptUtils.hashPassword(request.getPassword()))
//                    .roles(List.of(role))
//                    .build();
//            userCredentialRepository.saveAndFlush(userCredential);
//
//
//            Customer newCustomer = Customer.builder()
//                    .nama(request.getName())
//                    .email(request.getEmail())
//                    .nomerTelepon(request.getNomerTelepon())
//                    .userCredential(userCredential)
//                    .isActive(false)
//                    .isActivePeminjaman(false)
//                    .build();
//
//            customerRepository.save(newCustomer);
//
//
//            return RegisterCustomerResponse.builder()
//                    .id(newCustomer.getId())
//                    .username(userCredential.getUsername())
//                    .nama(newCustomer.getNama())
//                    .email(newCustomer.getEmail())
//                    .nomerTelepon(newCustomer.getNomerTelepon())
//                    .build();
//        } catch (DataIntegrityViolationException e){
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist");
//        }
//
//    }

    @Transactional
    @Override
    public RegisterAdminResponse registerNewAdmin(RegisterAdminRequest request) {
        validationService.validate(request);
        try {
            Role role = roleService.getOrsave(ERole.ROLE_ADMIN);
            UserCredential credential = UserCredential.builder()
                    .username(request.getUsername())
                    .password(bcryptUtils.hashPassword(request.getPassword()))
                    .roles(List.of(role))
                    .build();

            userCredentialRepository.saveAndFlush(credential);

            Admin admin = Admin.builder()
                    .name(request.getName())
                    .userCredential(credential)
                    .build();
            adminRepository.save(admin);

            return RegisterAdminResponse.builder()
                    .id(admin.getId())
                    .nama(admin.getName())
                    .username(credential.getUsername())
                    .build();
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "admin already exist");
        }
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailImpl userDetails = (UserDetailImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String token = jwtUtils.generateToken(userDetails.getUsername());
        return LoginResponse.builder()
                .username(userDetails.getUsername())
                .roles(roles)
                .token(token)
                .build();

    }


}
