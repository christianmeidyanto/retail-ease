package com.retailease.model.request;

import lombok.*;

import javax.validation.constraints.Min;
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
    @Min(value = 0 ,message = "price must be greater than equal 0")
    private Long price;
    @NotNull(message = "stock is required")
    @Min(value = 0 ,message = "stock must be greater than equal 0")
    private Integer stock;
}
