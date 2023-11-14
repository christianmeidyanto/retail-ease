package com.retailease.service;

import com.retailease.entity.Customer;
import com.retailease.model.request.CustomerRequest;
import com.retailease.model.response.CustomerResponse;
import com.retailease.model.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse getById(String id);
    Customer getCustomerById(String id);
    Page<CustomerResponse> getAllCustomer(Integer page, Integer size);
    CustomerResponse updateCustomer(CustomerRequest request);
}
