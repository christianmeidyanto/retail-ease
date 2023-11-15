package com.retailease.service.impl;

import com.retailease.entity.Product;
import com.retailease.entity.ProductPrice;
import com.retailease.model.request.ProductRequest;
import com.retailease.model.request.UpdateProductRequest;
import com.retailease.model.response.ProductResponse;
import com.retailease.repository.ProductRepository;
import com.retailease.service.ProductPriceService;
import com.retailease.service.ProductService;
import com.retailease.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceImplTest {
    private final ProductRepository productRepository=mock(ProductRepository.class);
    private final ProductPriceService productPriceService=mock(ProductPriceService.class);
    private final ValidationService validationService=mock(ValidationService.class);
    private final ProductService productService = new ProductServiceImpl(productRepository,productPriceService,validationService);

    @Test
    void getById() {
        Product product = Product.builder()
                .id("1")
                .code("P0001")
                .name("Product1")
                .build();
        ProductPrice productPrice = ProductPrice.builder()
                .id("123")
                .price(13000L)
                .stock(10)
                .isActive(true)
                .build();

        when(productRepository.findById(anyString())).thenReturn(Optional.ofNullable(product));
        when(productPriceService.getById(anyString())).thenReturn(productPrice);

        ProductResponse productResponse = productService.getById("1");

        verify(productRepository,times(1)).findById(anyString());
        verify(productPriceService,times(1)).getById(anyString());

        assertEquals(productResponse.getProductName(),"Product1");

    }

    @Test
    void getAllProducts() {
    }

    @Test
    void createProduct() {
        ProductRequest productRequest = ProductRequest.builder()
                .productCode("P0001")
                .productName("Product1")
                .price(13000L)
                .stock(10)
                .build();

        Product product = Product.builder()
                .code(productRequest.getProductCode())
                .name(productRequest.getProductName())
                .build();

        ProductPrice productPrice = ProductPrice.builder()
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .build();

        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(product);
        when(productPriceService.create(any(ProductPrice.class))).thenReturn(productPrice);

        ProductResponse productResponse = productService.createProduct(productRequest);

        verify(validationService,times(1)).validate(productRequest);
        verify(productRepository,times(1)).saveAndFlush(any(Product.class));
        verify(productPriceService,times(1)).create(any(ProductPrice.class));

        assertEquals(product.getName(),productResponse.getProductName());
    }
}