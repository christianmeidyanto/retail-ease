package com.retailease.model.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SalesTransactionRequest {
    @NotNull
    private String customerId;
    @NotNull
    private String adminId;
    private List<SalesTransactionDetailRequest> transactionDetail;
}
