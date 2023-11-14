package com.retailease.service.impl;

import com.retailease.entity.*;
import com.retailease.model.request.SalesTransactionRequest;
import com.retailease.model.response.*;
import com.retailease.repository.SalesTransactionDetailRepository;
import com.retailease.repository.SalesTransactionRepository;
import com.retailease.service.AdminService;
import com.retailease.service.CustomerService;
import com.retailease.service.ProductPriceService;
import com.retailease.service.SalesTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesTransactionServiceImpl implements SalesTransactionService {

    private final SalesTransactionRepository salesTransactionRepository;
    private final SalesTransactionDetailRepository salesTransactionDetailRepository;
    private final CustomerService customerService;
    private final ProductPriceService productPriceService;
    private final AdminService adminService;

    @Transactional
    @Override
    public SalesTransactionResponse createNewTransaction(SalesTransactionRequest request) {
        /*Validate Customer*/
        Customer customer = customerService.getCustomerById(request.getCustomerId());

        /*Validate Admin*/
        Admin admin = adminService.getById(request.getAdminId());


        /*Convert orderDetailRequest to orderDetail*/
        List<SalesTransactionDetail> transactionDetails = request.getTransactionDetail().stream().map(orderDetailRequest -> {

            /*Validate Product Price*/
            ProductPrice productPrice = productPriceService.getById(orderDetailRequest.getProductPriceId());

            return SalesTransactionDetail.builder()
                    .productPrice(productPrice)
                    .quantity(orderDetailRequest.getQuantity())
                    .build();
        }).collect(Collectors.toList());

        /*Create New Transaction*/
        SalesTransaction salesTransaction = SalesTransaction.builder()
                .customer(customer)
                .transDate(LocalDateTime.now())
                .admin(admin)
                .salesTransactionDetails(transactionDetails)
                .build();
        salesTransactionRepository.saveAndFlush(salesTransaction);

        List<SalesTransactionDetailResponse> salesTransactionDetailResponses = salesTransaction.getSalesTransactionDetails().stream().map(transactionDetail -> {

            /*Set order from order detail after creating new order*/
            transactionDetail.setSalesTransaction(salesTransaction);

            /*Change stock from the purchase quantity*/
            ProductPrice currentProductPrice = transactionDetail.getProductPrice();
            currentProductPrice.setStock(currentProductPrice.getStock()-transactionDetail.getQuantity());

            salesTransactionDetailRepository.save(transactionDetail);

            return SalesTransactionDetailResponse.builder()
                    .salesTransactionDetailId(transactionDetail.getId())
                    .quantity(transactionDetail.getQuantity())

                    /*Convert Product to Product Response (from ProductPrice)*/
                    .product(ProductResponse.builder()
                            .id(currentProductPrice.getProduct().getId())
                            .productCode(currentProductPrice.getProduct().getCode())
                            .productName(currentProductPrice.getProduct().getName())
                            .price(currentProductPrice.getPrice())
                            .stock(currentProductPrice.getStock())
                            .build())
                    .build();
        }).collect(Collectors.toList());
        CustomerResponse customerResponse = CustomerResponse.builder()
                .CustomerId(customer.getId())
                .CustomerName(customer.getName())
                .address(customer.getAddress())
                .phoneNumber(customer.getPhoneNumber())
                .city(customer.getCity())
                .build();

        AdminResponse adminResponse = AdminResponse.builder()
                .id(admin.getId())
                .name(admin.getName())
                .build();

        return toSalesTransactionResponse(salesTransaction, customerResponse, adminResponse, salesTransactionDetailResponses);
    }

    private static SalesTransactionResponse toSalesTransactionResponse(SalesTransaction salesTransaction, CustomerResponse customerResponse, AdminResponse adminResponse, List<SalesTransactionDetailResponse> salesTransactionDetailResponses) {
        return SalesTransactionResponse.builder()
                .salesTransactionId(salesTransaction.getId())
                .customer(customerResponse)
                .admin(adminResponse)
                .transDate(salesTransaction.getTransDate())
                .transactionDetail(salesTransactionDetailResponses)
                .build();
    }

    @Override
    public SalesTransactionResponse getTransactionById(String id) {
        SalesTransaction salesTransaction = salesTransactionRepository.findById(id).get();

        CustomerResponse customerResponse = CustomerResponse.builder()
                .CustomerId(salesTransaction.getCustomer().getId())
                .CustomerName(salesTransaction.getCustomer().getName())
                .address(salesTransaction.getCustomer().getAddress())
                .phoneNumber(salesTransaction.getCustomer().getPhoneNumber())
                .city(salesTransaction.getCustomer().getCity())
                .build();

        AdminResponse adminResponse = AdminResponse.builder()
                .id(salesTransaction.getAdmin().getId())
                .name(salesTransaction.getAdmin().getName())
                .build();

        List<SalesTransactionDetailResponse> salesTransactionDetailResponses = salesTransaction.getSalesTransactionDetails().stream().map(transactionDetail -> {
            return SalesTransactionDetailResponse.builder()
                    .salesTransactionDetailId(transactionDetail.getId())
                    .quantity(transactionDetail.getQuantity())

                    /*Convert Product to Product Response (from ProductPrice)*/
                    .product(ProductResponse.builder()
                            .id(transactionDetail.getProductPrice().getProduct().getId())
                            .productCode(transactionDetail.getProductPrice().getProduct().getCode())
                            .productName(transactionDetail.getProductPrice().getProduct().getName())
                            .price(transactionDetail.getProductPrice().getPrice())
                            .stock(transactionDetail.getProductPrice().getStock())
                            .build())
                    .build();
        }).collect(Collectors.toList());
        return toSalesTransactionResponse(salesTransaction,customerResponse,adminResponse,salesTransactionDetailResponses);
    }

    @Override
    public Page<SalesTransactionResponse> getAllTransaction(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<SalesTransaction> salesTransactions = salesTransactionRepository.findAll(pageable);
        List<SalesTransactionResponse> salesTransactionResponses = new ArrayList<>();
        for (SalesTransaction transaction:salesTransactions.getContent()){
            List<SalesTransactionDetailResponse> salesTransactionDetailResponses = transaction.getSalesTransactionDetails().stream().map(transactionDetail -> {
                return SalesTransactionDetailResponse.builder()
                        .salesTransactionDetailId(transactionDetail.getId())
                        .quantity(transactionDetail.getQuantity())

                        /*Convert Product to Product Response (from ProductPrice)*/
                        .product(ProductResponse.builder()
                                .id(transactionDetail.getProductPrice().getProduct().getId())
                                .productCode(transactionDetail.getProductPrice().getProduct().getCode())
                                .productName(transactionDetail.getProductPrice().getProduct().getName())
                                .price(transactionDetail.getProductPrice().getPrice())
                                .stock(transactionDetail.getProductPrice().getStock())
                                .build())
                        .build();
            }).collect(Collectors.toList());

            Customer customer = transaction.getCustomer();
            CustomerResponse customerResponse = CustomerResponse.builder()
                    .CustomerId(customer.getId())
                    .CustomerName(customer.getName())
                    .address(customer.getAddress())
                    .phoneNumber(customer.getPhoneNumber())
                    .city(customer.getCity())
                    .build();

            Admin admin = transaction.getAdmin();
            AdminResponse adminResponse = AdminResponse.builder()
                    .id(admin.getId())
                    .name(admin.getName())
                    .build();
            salesTransactionResponses.add(toSalesTransactionResponse(transaction,customerResponse,adminResponse,salesTransactionDetailResponses));
        }
        return new PageImpl<>(salesTransactionResponses,pageable,salesTransactions.getTotalElements());
    }
}
