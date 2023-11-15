package com.retailease.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retailease.entity.Admin;
import com.retailease.entity.Role;
import com.retailease.entity.UserCredential;
import com.retailease.entity.constant.ERole;
import com.retailease.model.request.LoginRequest;
import com.retailease.model.response.AdminResponse;
import com.retailease.model.response.CommonResponse;
import com.retailease.model.response.LoginResponse;
import com.retailease.service.AdminService;
import com.retailease.service.AuthService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
/*Before run this controller test, enable code in security/SecurityConfiguration Line:50*/
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Order(1)
    @Test
    void itShouldLoginAndReturnLoginResponseOfAdminAndTokenAndStatusCode() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .username("admin123")
                .password("test")
                .build();

        List<String> roles = List.of("ROLE_ADMIN");
        LoginResponse loginResponse = LoginResponse.builder()
                .username("admin123")
                .roles(roles)
                .token("jwt_token_secret")
                .build();


        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().getUsername(), "admin123");
            assertEquals(response.getData().getRoles().get(0), "ROLE_ADMIN");
            assertEquals(response.getData().getToken(), "jwt_token_secret");
        });

        verify(authService,times(1)).login(any(LoginRequest.class));

    }
}