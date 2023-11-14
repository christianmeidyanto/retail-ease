package com.retailease.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SalesTransactionDetailResponse {
    private String salesTransactionDetailId;
    private ProductResponse product;
    private Integer quantity;
}
