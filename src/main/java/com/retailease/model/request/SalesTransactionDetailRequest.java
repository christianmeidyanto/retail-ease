package com.retailease.model.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SalesTransactionDetailRequest {
    @NotNull
    private String productPriceId;
    @NotNull(message = "quantity is required")
    @Min(value = 0 ,message = "quantity must be greater than equal 0")
    private Integer quantity;
}
