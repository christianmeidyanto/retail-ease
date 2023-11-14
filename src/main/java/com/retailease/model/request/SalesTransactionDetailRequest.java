package com.retailease.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SalesTransactionDetailRequest {
    private String productPriceId;
    private Integer quantity;
}
