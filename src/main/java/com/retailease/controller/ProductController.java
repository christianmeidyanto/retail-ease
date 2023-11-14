package com.retailease.controller;

import com.retailease.model.request.ProductRequest;
import com.retailease.model.request.UpdateProductRequest;
import com.retailease.model.response.CommonResponse;
import com.retailease.model.response.PageResponse;
import com.retailease.model.response.ProductResponse;
import com.retailease.security.JwtUtils;
import com.retailease.service.ProductService;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {
        try {
            ProductResponse productResponse = productService.createProduct(request);

            CommonResponse<Object> commonResponse = CommonResponse.builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully created product")
                    .data(productResponse)
                    .build();
            return ResponseEntity.status((HttpStatus.CREATED)).body(commonResponse);
        } catch (Exception exception) {
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(CommonResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Failed create product :{}" + exception.getMessage())
                    .data(null)
                    .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        try {
            ProductResponse product = productService.getById(id);
            CommonResponse<Object> commonResponse = CommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully get by id product")
                    .data(product)
                    .build();
            return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(CommonResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Get by id product" + e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @GetMapping
    public ResponseEntity<?> getListProduct(@RequestParam Integer page, @RequestParam Integer size) {

        Page<ProductResponse> allProducts = productService.getAllProducts(page, size);
        PageResponse pageResponse = PageResponse.builder()
                .currentPage(page)
                .totalPage(allProducts.getTotalPages())
                .size(size)
                .build();
        return ResponseEntity.status((HttpStatus.OK)).body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get all product")
                .data(allProducts.getContent())
                .paging(pageResponse)
                .build());
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody UpdateProductRequest product) {
        try {
        ProductResponse productResponse = productService.update(product);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<ProductResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully update product")
                        .data(productResponse)
                        .build());
        }catch (Exception exception) {
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(CommonResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Failed update product :{}" + exception.getMessage())
                    .data(null)
                    .build());
        }
    }

}
