package com.retailease.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retailease.entity.Admin;
import com.retailease.entity.Customer;
import com.retailease.entity.Product;
import com.retailease.entity.ProductPrice;
import com.retailease.model.request.SalesTransactionDetailRequest;
import com.retailease.model.request.SalesTransactionRequest;
import com.retailease.model.response.*;
import com.retailease.service.ProductService;
import com.retailease.service.SalesTransactionService;
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

import java.time.LocalDateTime;
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
class SalesTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SalesTransactionService salesTransactionService;

    @Order(1)
    @Test
    void ItShouldCreateSalesTransactionAndReturnSalesTransactionResponseOfSalesTransactionAndStatusCode() throws Exception {
        CustomerResponse customer = CustomerResponse.builder()
                .CustomerId("132")
                .CustomerName("Customer1")
                .address("Jl. Lombok No.XX")
                .phoneNumber("6282299202029202")
                .city("Kota Bandung")
                .build();
        AdminResponse admin = AdminResponse.builder()
                .id("123")
                .name("admin")
                .build();

        ProductResponse product = ProductResponse.builder()
                .id("1")
                .productCode("P0001")
                .productName("Product1")
                .price(13000L)
                .stock(10)
                .build();

        List<SalesTransactionDetailRequest> salesTransactionDetailRequestList = new ArrayList<>();
        salesTransactionDetailRequestList.add(new SalesTransactionDetailRequest("1", 1));


        SalesTransactionRequest salesTransactionRequest = SalesTransactionRequest.builder()
                .customerId("132")
                .adminId("123")
                .transactionDetail(salesTransactionDetailRequestList)
                .build();

        List<SalesTransactionDetailResponse> salesTransactionDetailResponseList = new ArrayList<>();
        salesTransactionDetailResponseList.add(new SalesTransactionDetailResponse("123",product,1));

        SalesTransactionResponse salesTransactionResponse = SalesTransactionResponse.builder()
                .salesTransactionId("123")
                .transDate(LocalDateTime.now())
                .admin(admin)
                .customer(customer)
                .transactionDetail(salesTransactionDetailResponseList)
                .build();

        when(salesTransactionService.createNewTransaction(any(SalesTransactionRequest.class))).thenReturn(salesTransactionResponse);

        mockMvc.perform(
                post("/api/v1/sales-transaction")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(salesTransactionRequest))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            CommonResponse<SalesTransactionResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().getAdmin().getId(), admin.getId());
            assertEquals(response.getData().getCustomer().getCustomerId(), customer.getCustomerId());
            assertEquals(response.getData().getTransactionDetail().get(0).getQuantity(),1);
        });
        //verify
        verify(salesTransactionService,times(1)).createNewTransaction(any(SalesTransactionRequest.class));
    }

    @Order(2)
    @Test
    void ItShouldGetListSalesTransactionByIdAndReturnSalesTransactionResponseOfSalesTransactionAndStatusCode() throws Exception {
        CustomerResponse customer = CustomerResponse.builder()
                .CustomerId("132")
                .CustomerName("Customer1")
                .address("Jl. Lombok No.XX")
                .phoneNumber("6282299202029202")
                .city("Kota Bandung")
                .build();
        AdminResponse admin = AdminResponse.builder()
                .id("123")
                .name("admin")
                .build();

        ProductResponse product = ProductResponse.builder()
                .id("1")
                .productCode("P0001")
                .productName("Product1")
                .price(13000L)
                .stock(10)
                .build();


        List<SalesTransactionDetailResponse> salesTransactionDetailResponseList1 = new ArrayList<>();
        salesTransactionDetailResponseList1.add(new SalesTransactionDetailResponse("123",product,1));

        List<SalesTransactionDetailResponse> salesTransactionDetailResponseList2 = new ArrayList<>();
        salesTransactionDetailResponseList2.add(new SalesTransactionDetailResponse("124",product,2));

        List<SalesTransactionResponse> transactionResponseList = new ArrayList<>();
        transactionResponseList.add(SalesTransactionResponse.builder()
                .salesTransactionId("123")
                .transDate(LocalDateTime.now())
                .admin(admin)
                .customer(customer)
                .transactionDetail(salesTransactionDetailResponseList1)
                .build());
        transactionResponseList.add(SalesTransactionResponse.builder()
                .salesTransactionId("124")
                .transDate(LocalDateTime.now())
                .admin(admin)
                .customer(customer)
                .transactionDetail(salesTransactionDetailResponseList2)
                .build());
        Page<SalesTransactionResponse> salesTransactionResponses = new PageImpl<>(transactionResponseList);


        when(salesTransactionService.getAllTransaction(0,2)).thenReturn(salesTransactionResponses);

        mockMvc.perform(
                get("/api/v1/sales-transaction")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(2))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<List<SalesTransactionResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().size(), 2);
            assertEquals(response.getData().get(0).getTransactionDetail().get(0).getQuantity(), 1);
            assertEquals(response.getData().get(1).getTransactionDetail().get(0).getQuantity(), 2);
        });

        verify(salesTransactionService, times(1)).getAllTransaction(any(),any());
    }

    @Order(3)
    @Test
    void ItShouldGetSalesTransactionByIdAndReturnSalesTransactionResponseOfSalesTransactionAndStatusCode() throws Exception {
        CustomerResponse customer = CustomerResponse.builder()
                .CustomerId("132")
                .CustomerName("Customer1")
                .address("Jl. Lombok No.XX")
                .phoneNumber("6282299202029202")
                .city("Kota Bandung")
                .build();
        AdminResponse admin = AdminResponse.builder()
                .id("123")
                .name("admin")
                .build();

        ProductResponse product = ProductResponse.builder()
                .id("1")
                .productCode("P0001")
                .productName("Product1")
                .price(13000L)
                .stock(10)
                .build();

        List<SalesTransactionDetailRequest> salesTransactionDetailRequestList = new ArrayList<>();
        salesTransactionDetailRequestList.add(new SalesTransactionDetailRequest("1", 1));

        List<SalesTransactionDetailResponse> salesTransactionDetailResponseList = new ArrayList<>();
        salesTransactionDetailResponseList.add(new SalesTransactionDetailResponse("123",product,1));

        SalesTransactionResponse salesTransactionResponse = SalesTransactionResponse.builder()
                .salesTransactionId("123")
                .transDate(LocalDateTime.now())
                .admin(admin)
                .customer(customer)
                .transactionDetail(salesTransactionDetailResponseList)
                .build();

        when(salesTransactionService.getTransactionById(anyString())).thenReturn(salesTransactionResponse);

        mockMvc.perform(
                get("/api/v1/sales-transaction/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<SalesTransactionResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().getAdmin().getId(), admin.getId());
            assertEquals(response.getData().getCustomer().getCustomerId(), customer.getCustomerId());
            assertEquals(response.getData().getTransactionDetail().get(0).getQuantity(),1);
        });
        //verify
        verify(salesTransactionService,times(1)).getTransactionById(anyString());
    }
}