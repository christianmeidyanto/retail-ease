package com.retailease.service.impl;

import com.retailease.entity.Customer;
import com.retailease.entity.Product;
import com.retailease.entity.ProductPrice;
import com.retailease.model.request.CustomerRequest;
import com.retailease.model.response.CustomerResponse;
import com.retailease.model.response.ProductResponse;
import com.retailease.repository.CustomerRepository;
import com.retailease.service.CustomerService;
import com.retailease.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationService validationService;

    @Transactional
    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        validationService.validate(request);
        try {
        Customer customer = Customer.builder()
                .name(request.getCustomerName())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .city(request.getCity())
                .build();
            customerRepository.save(customer);
            return toCustomerResponse(customer);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create customer");
        }
    }

    @Override
    public CustomerResponse getById(String id) {
        return toCustomerResponse(getCustomerById(id));
    }

    @Override
    public Customer getCustomerById(String id) {
        return customerRepository.findById(id).get();
    }

    @Override
    public Page<CustomerResponse> getAllCustomer(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Customer> customers = customerRepository.findAll(pageable);
        List<CustomerResponse> customerResponses = new ArrayList<>();
        for (Customer customer:customers.getContent()){
            customerResponses.add(toCustomerResponse(customer));
        }
        return new PageImpl<>(customerResponses,pageable,customers.getTotalElements());
    }

    @Transactional
    @Override
    public CustomerResponse updateCustomer(CustomerRequest request) {
        validationService.validate(request);
        try {
            Customer customer = customerRepository.findById(request.getCustomerId()).get();
            customer.setName(request.getCustomerName());
            customer.setPhoneNumber(request.getPhoneNumber());
            customer.setAddress(request.getAddress());
            customer.setCity(request.getCity());
            customerRepository.save(customer);
            return toCustomerResponse(customer);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update customer");
        }
    }

    private static CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .CustomerId(customer.getId())
                .CustomerName(customer.getName())
                .address(customer.getAddress())
                .phoneNumber(customer.getPhoneNumber())
                .city(customer.getCity())
                .build();
    }
}
