package com.retailease.model.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomerRequest {
    private String CustomerId;
    private String CustomerName;
    private String phoneNumber;
    private String address;
    private String city;
}
