package com.retailease.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductResponse {
    private String id;
    private String productCode;
    private String productName;
    private Long price;
    private Integer stock;
}
