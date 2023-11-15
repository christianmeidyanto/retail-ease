package com.retailease.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retailease.entity.Admin;
import com.retailease.entity.Role;
import com.retailease.entity.UserCredential;
import com.retailease.entity.constant.ERole;
import com.retailease.model.response.AdminResponse;
import com.retailease.model.response.CommonResponse;
import com.retailease.service.AdminService;
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
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

    @Order(1)
    @Test
    void isShouldGetAdminAndReturnAdminResponseOfAdminAndStatusCode() throws Exception {
        List<Role> roles= new ArrayList<>();
        roles.add(new Role("role1", ERole.ROLE_ADMIN));

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
        AdminResponse adminResponse = AdminResponse.builder()
                .id("1")
                .name("Admin")
                .build();

        when(adminService.getAdminByUsername(anyString())).thenReturn(adminResponse);

        mockMvc.perform(
                get("/api/v1/admin/admin123")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<AdminResponse> response =objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(response.getData().getId(),"1");
            assertEquals(response.getData().getName(),"Admin");
        });

        verify(adminService,times(1)).getAdminByUsername(anyString());
    }
}