package com.retailease.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retailease.entity.Customer;
import com.retailease.model.request.CustomerRequest;
import com.retailease.model.response.CommonResponse;
import com.retailease.model.response.CustomerResponse;
import com.retailease.service.CustomerService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
/*Before run this controller test, enable code in security/SecurityConfiguration Line:50*/
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Order(1)
    @Test
    void ItShouldCreateCustomerAndReturnCustomerResponseOfProductAndStatusCode() throws Exception {
        CustomerRequest customerRequest = CustomerRequest.builder()
                .CustomerId("123")
                .CustomerName("Sandi")
                .address("Jl. Lombok No.XX")
                .phoneNumber("628283828291")
                .city("Kota Bandung")
                .build();
        CustomerResponse customerResponse = CustomerResponse.builder()
                .CustomerId("123")
                .CustomerName("Sandi")
                .address("Jl. Lombok No.XX")
                .phoneNumber("628283828291")
                .city("Kota Bandung")
                .build();
        when(customerService.createCustomer(any(CustomerRequest.class))).thenReturn(customerResponse);

        mockMvc.perform(
                post("/api/v1/customer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(customerRequest))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            CommonResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().getCustomerId(), "123");
            assertEquals(response.getData().getCustomerName(), "Sandi");
        });

        verify(customerService, times(1)).createCustomer(any(CustomerRequest.class));
    }

    @Order(2)
    @Test
    void ItShouldGetCustomerByIdAndReturnCustomerResponseOfProductAndStatusCode() throws Exception {
        CustomerResponse customerResponse = CustomerResponse.builder()
                .CustomerId("123")
                .CustomerName("Sandi")
                .address("Jl. Lombok No.XX")
                .phoneNumber("628283828291")
                .city("Kota Bandung")
                .build();

        when(customerService.getById(anyString())).thenReturn(customerResponse);

        mockMvc.perform(
                get("/api/v1/customer/123")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().getCustomerId(), "123");
            assertEquals(response.getData().getCustomerName(), "Sandi");
        });
    }

    @Order(3)
    @Test
    void ItShouldGetListCustomerAndReturnCustomerResponseOfProductAndStatusCode() throws Exception {
        List<CustomerResponse> customerList = new ArrayList<>();
        customerList.add(CustomerResponse.builder()
                .CustomerId("123")
                .CustomerName("Sandi")
                .address("Jl. Lombok No.XX")
                .phoneNumber("628283828291")
                .city("Kota Bandung")
                .build());
      customerList.add(CustomerResponse.builder()
                .CustomerId("124")
                .CustomerName("Sandy")
                .address("Jl. Lombok No.XX")
                .phoneNumber("628212345691")
                .city("Kota Bandung")
                .build());
        Page<CustomerResponse> customerResponses = new PageImpl<>(customerList);

      when(customerService.getAllCustomer(0,2)).thenReturn(customerResponses);

        mockMvc.perform(
                get("/api/v1/customer")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(2))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<List<CustomerResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().size(), 2);
            assertEquals(response.getData().get(0).getCustomerName(), "Sandi");
            assertEquals(response.getData().get(1).getCustomerName(), "Sandy");
        });

        verify(customerService, times(1)).getAllCustomer(any(),any());
    }

    @Order(4)
    @Test
    void ItShouldUpdateCustomerAndReturnCustomerResponseOfProductAndStatusCode() throws Exception {
        CustomerRequest customerRequest = CustomerRequest.builder()
                .CustomerId("123")
                .CustomerName("Sandi")
                .address("Jl. Lombok No.XX")
                .phoneNumber("628283828291")
                .city("Kota Bandung")
                .build();
        CustomerResponse customerResponse = CustomerResponse.builder()
                .CustomerId("123")
                .CustomerName("Sandi")
                .address("Jl. Lombok No.XX")
                .phoneNumber("628283828291")
                .city("Kota Bandung")
                .build();
        when(customerService.updateCustomer(any(CustomerRequest.class))).thenReturn(customerResponse);

        mockMvc.perform(
                put("/api/v1/customer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(customerRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().getCustomerId(), "123");
            assertEquals(response.getData().getCustomerName(), "Sandi");
        });

        verify(customerService, times(1)).updateCustomer(any(CustomerRequest.class));
    }
}