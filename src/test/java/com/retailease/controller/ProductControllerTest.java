package com.retailease.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retailease.model.request.CustomerRequest;
import com.retailease.model.request.ProductRequest;
import com.retailease.model.request.UpdateProductRequest;
import com.retailease.model.response.CommonResponse;
import com.retailease.model.response.CustomerResponse;
import com.retailease.model.response.ProductResponse;
import com.retailease.service.CustomerService;
import com.retailease.service.ProductService;
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
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Order(1)
    @Test
    void ItShouldCreateProductAndReturnProductResponseOfProductAndStatusCode() throws Exception {
        ProductRequest productRequest = ProductRequest.builder()
                .productId("123")
                .productCode("TB0001")
                .productName("TBA 30 gram Red")
                .price(14000L)
                .stock(10)
                .build();
        ProductResponse productResponse = ProductResponse.builder()
                .id("123")
                .productCode("TB0001")
                .productName("TBA 30 gram Red")
                .price(14000L)
                .stock(10)
                .build();
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);

        mockMvc.perform(
                post("/api/v1/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(productRequest))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            CommonResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().getProductCode(), "TB0001");
            assertEquals(response.getData().getProductName(), "TBA 30 gram Red");
        });
    }

    @Order(2)
    @Test
    void ItShouldGetProductByIdAndReturnProductResponseOfProductAndStatusCode() throws Exception {
        ProductResponse productResponse = ProductResponse.builder()
                .id("123")
                .productCode("TB0001")
                .productName("TBA 30 gram Red")
                .price(14000L)
                .stock(10)
                .build();
        when(productService.getById(anyString())).thenReturn(productResponse);

        mockMvc.perform(
                get("/api/v1/product/123")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().getProductCode(), "TB0001");
            assertEquals(response.getData().getProductName(), "TBA 30 gram Red");
        });
    }

    @Order(3)
    @Test
    void ItShouldGetListProductAndReturnProductResponseOfProductAndStatusCode() throws Exception {
        List<ProductResponse> productList = new ArrayList<>();
        productList.add(ProductResponse.builder()
                .id("123")
                .productCode("TB0001")
                .productName("TBA 30 gram Red")
                .price(14000L)
                .stock(10)
                .build());
        productList.add(ProductResponse.builder()
                .id("123")
                .productCode("TB0002")
                .productName("TBA 35 gram Red")
                .price(15000L)
                .stock(10)
                .build());
        Page<ProductResponse> productResponses = new PageImpl<>(productList);

        when(productService.getAllProducts(0,2)).thenReturn(productResponses);

        mockMvc.perform(
                get("/api/v1/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(2))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<List<ProductResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().size(), 2);
            assertEquals(response.getData().get(0).getProductName(), "TBA 30 gram Red");
            assertEquals(response.getData().get(1).getProductName(), "TBA 35 gram Red");
        });

        verify(productService, times(1)).getAllProducts(any(),any());
    }

    @Order(4)
    @Test
    void ItShouldUpdateProductAndReturnProductResponseOfProductAndStatusCode() throws Exception {
        UpdateProductRequest updateProductRequest = UpdateProductRequest.builder()
                .productId("123")
                .productCode("TB0001")
                .productName("TBA 35 gram Red")
                .price(15000L)
                .build();
        ProductResponse productResponse = ProductResponse.builder()
                .id("123")
                .productCode("TB0001")
                .productName("TBA 35 gram Red")
                .price(15000L)
                .stock(10)
                .build();

        when(productService.update(any(UpdateProductRequest.class))).thenReturn(productResponse);

        mockMvc.perform(
                put("/api/v1/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updateProductRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(response.getData().getProductName(), "TBA 35 gram Red");
            assertEquals(response.getData().getPrice(), 15000L);
        });

        verify(productService, times(1)).update(any(UpdateProductRequest.class));
    }
}