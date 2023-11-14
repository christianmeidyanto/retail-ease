package com.retailease.model.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SalesTransactionRequest {
    private String customerId;
    private String adminId;
    private List<SalesTransactionDetailRequest> transactionDetail;
}
