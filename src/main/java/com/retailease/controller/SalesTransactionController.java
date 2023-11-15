package com.retailease.controller;

import com.retailease.entity.SalesTransaction;
import com.retailease.model.request.SalesTransactionRequest;
import com.retailease.model.response.CommonResponse;
import com.retailease.model.response.PageResponse;
import com.retailease.model.response.ProductResponse;
import com.retailease.model.response.SalesTransactionResponse;
import com.retailease.service.SalesTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/sales-transaction")
public class SalesTransactionController {
    private final SalesTransactionService salesTransactionService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SalesTransactionRequest request){
        try {
        SalesTransactionResponse transaction = salesTransactionService.createNewTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<SalesTransactionResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successful Create New Transaction")
                        .data(transaction)
                        .build());
        }catch (Exception exception){
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(CommonResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Failed create transaction : " + exception.getMessage())
                    .data(null)
                    .build());
        }

    }

    @GetMapping
    public ResponseEntity<?> getListSalesTransaction(@RequestParam Integer page, @RequestParam Integer size) {
        Page<SalesTransactionResponse> salesTransactionResponses = salesTransactionService.getAllTransaction(page, size);
        PageResponse pageResponse = PageResponse.builder()
                .currentPage(page)
                .totalPage(salesTransactionResponses.getTotalPages())
                .size(size)
                .build();
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get all sales transaction ")
                .data(salesTransactionResponses.getContent())
                .paging(pageResponse)
                .build();
        return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSalesTransactionById(@PathVariable String id) {
        try {
            SalesTransactionResponse transaction = salesTransactionService.getTransactionById(id);
            CommonResponse<Object> commonResponse = CommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully get by id sales transaction")
                    .data(transaction)
                    .build();
            return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);
        } catch (NoSuchElementException e) {
            CommonResponse<Object> commonResponse = CommonResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Get by id sales transaction : " + e.getMessage())
                    .data(null)
                    .build();
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(commonResponse);
        }
    }
}
