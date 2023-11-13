package com.retailease.service;

import com.retailease.model.request.LoginRequest;
import com.retailease.model.request.RegisterAdminRequest;
import com.retailease.model.response.LoginResponse;
import com.retailease.model.response.RegisterAdminResponse;

public interface AuthService {
//    RegisterCustomerResponse registerNewCustomer (RegisterCustomerRequest request);
    RegisterAdminResponse registerNewAdmin (RegisterAdminRequest request);
    LoginResponse login(LoginRequest loginRequest);
}
