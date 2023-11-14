package com.retailease.controller;


import com.retailease.entity.Customer;
import com.retailease.model.request.CustomerRequest;
import com.retailease.model.request.ProductRequest;
import com.retailease.model.request.UpdateProductRequest;
import com.retailease.model.response.CommonResponse;
import com.retailease.model.response.CustomerResponse;
import com.retailease.model.response.PageResponse;
import com.retailease.model.response.ProductResponse;
import com.retailease.security.JwtUtils;
import com.retailease.service.CustomerService;
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
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerRequest request) {
        try {
            CustomerResponse customerResponse = customerService.createCustomer(request);

            CommonResponse<Object> commonResponse = CommonResponse.builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully created customer")
                    .data(customerResponse)
                    .build();
            return ResponseEntity.status((HttpStatus.CREATED)).body(commonResponse);
        } catch (Exception exception) {
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(CommonResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to create customer : {}" + exception.getMessage())
                    .data(null)
                    .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable String id) {
        try {
            CustomerResponse customerResponse = customerService.getById(id);
            CommonResponse<Object> commonResponse = CommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully get by id customer")
                    .data(customerResponse)
                    .build();
            return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(CommonResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Get by id customer : {}" + e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @GetMapping
    public ResponseEntity<?> getListProduct(@RequestParam Integer page, @RequestParam Integer size) {

        Page<CustomerResponse> allCustomer = customerService.getAllCustomer(page, size);
        PageResponse pageResponse = PageResponse.builder()
                .currentPage(page)
                .totalPage(allCustomer.getTotalPages())
                .size(size)
                .build();
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get all customer")
                .data(allCustomer.getContent())
                .paging(pageResponse)
                .build();
        return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody CustomerRequest request) {
        try {
            CustomerResponse customerResponse = customerService.updateCustomer(request);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(CommonResponse.<CustomerResponse>builder()
                            .statusCode(HttpStatus.OK.value())
                            .message("Successfully update customer")
                            .data(customerResponse)
                            .build());
        }catch (Exception exception) {
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(CommonResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to update customer :{}" + exception.getMessage())
                    .data(null)
                    .build());
        }
    }

}

