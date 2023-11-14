package com.retailease.model.response;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomerResponse {
    private String CustomerId;
    private String CustomerName;
    private String phoneNumber;
    private String address;
    private String city;
}

