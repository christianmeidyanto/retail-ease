package com.retailease.model.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductRequest {
    private String productId;
    @NotBlank(message = "product code is required")
    private String productCode;
    @NotBlank(message = "product name is required")
    private String productName;
    @NotNull(message = "price is required")
    private Long price;
    @NotNull(message = "stock is required")
    private Integer stock;
}
