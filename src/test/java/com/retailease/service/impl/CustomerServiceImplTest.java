package com.retailease.service.impl;

import com.retailease.entity.Customer;
import com.retailease.model.request.CustomerRequest;
import com.retailease.model.response.CustomerResponse;
import com.retailease.repository.CustomerRepository;
import com.retailease.service.CustomerService;
import com.retailease.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@SpringBootTest
class CustomerServiceImplTest {
    private final ValidationService validationService = mock(ValidationService.class);
    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final Pageable pageable = mock(Pageable.class);
    private final CustomerService customerService = new CustomerServiceImpl(customerRepository,validationService);

    @Test
    void createCustomer() {
        CustomerRequest customerRequest = CustomerRequest.builder()
                .CustomerName("Customer 1")
                .address("Jl. Lombok No. XX")
                .phoneNumber("6280192828292")
                .city("Kota Bandung")
                .build();

        Customer customer = Customer.builder()
                .id("123")
                .name("Customer 1")
                .address("Jl. Lombok No. XX")
                .phoneNumber("6280192828292")
                .city("Kota Bandung")
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse customerResponse = customerService.createCustomer(customerRequest);
        verify(customerRepository, times(1)).save(any(Customer.class));
        assertEquals(customerResponse.getCustomerName(),"Customer 1");

    }

    @Test
    void getById() {
        Customer customer = Customer.builder()
                .id("123")
                .name("Customer 1")
                .address("Jl. Lombok No. XX")
                .phoneNumber("6280192828292")
                .city("Kota Bandung")
                .build();

        when(customerRepository.findById(anyString())).thenReturn(Optional.ofNullable(customer));

        CustomerResponse customerResponse = customerService.getById("123");
        verify(customerRepository,times(1)).findById(anyString());
        assertEquals(customerResponse.getCustomerId(),"123");
    }


    @Test
    void getAllCustomer() {
        Pageable pageRequest = PageRequest.of(0, 1);
        
        List<Customer> customerList = new ArrayList<>();
        customerList.add(new Customer("1","Customer1","6280192828292","Jl. Lombok No. XX","Kota Bandung"));
        customerList.add(new Customer("2","Customer2","6280192218292","Jl. Lombok No. XX","Kota Bandung"));
        customerList.add(new Customer("3","Customer3","6280192212134","Jl. Lombok No. XX","Kota Bandung"));

        Page<Customer> customers = new PageImpl<>(customerList);

        when(customerRepository.findAll(pageRequest)).thenReturn(customers);

        Page<CustomerResponse> customer = customerService.getAllCustomer(0,1);

        verify(customerRepository,times(1)).findAll(pageRequest);

        assertEquals(customers.getTotalElements(), customer.getTotalElements());
    }

    @Test
    void updateCustomer() {
        Customer customerOld = new Customer("1", "Customer1", "6280192828292", "Jl. Lombok No. XX", "Kota Bandung");
        Customer customerNew = new Customer("1", "Customer2", "6280112122321", "Jl. Lombok No. XXX", "Kota Bandung");

        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customerOld));
        when(customerRepository.save(customerNew)).thenReturn(customerNew);

        CustomerRequest customerRequest = new CustomerRequest("1", "Customer2", "6280112122321", "Jl. Lombok No. XXX", "Kota Bandung");
        CustomerResponse customerResponse = customerService.updateCustomer(customerRequest);

        verify(customerRepository,times(1)).findById(customerOld.getId());
        verify(customerRepository,times(1)).save(customerNew);

        assertEquals(customerNew.getName(),customerResponse.getCustomerName());

    }
}