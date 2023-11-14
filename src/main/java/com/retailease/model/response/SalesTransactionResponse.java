package com.retailease.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SalesTransactionResponse {
    private String salesTransactionId;
    private LocalDateTime transDate;
    private CustomerResponse customer;
    private AdminResponse admin;
    private List<SalesTransactionDetailResponse> transactionDetail;
}
