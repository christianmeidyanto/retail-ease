package com.retailease.service;

import com.retailease.model.request.SalesTransactionRequest;
import com.retailease.model.response.SalesTransactionResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SalesTransactionService {
    SalesTransactionResponse createNewTransaction(SalesTransactionRequest request);
    SalesTransactionResponse getTransactionById(String id);
    Page<SalesTransactionResponse> getAllTransaction(Integer page, Integer size);
}
