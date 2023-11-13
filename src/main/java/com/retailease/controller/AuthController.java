package com.retailease.controller;


import com.retailease.model.request.LoginRequest;
import com.retailease.model.request.RegisterAdminRequest;
import com.retailease.model.response.CommonResponse;
import com.retailease.model.response.LoginResponse;
import com.retailease.model.response.RegisterAdminResponse;
import com.retailease.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

//    @PostMapping(
//            path = "/register",
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    public ResponseEntity<?> register(@RequestBody RegisterCustomerRequest request){
//        RegisterCustomerResponse newCustomer = authService.registerNewCustomer(request);
//
//        CommonResponse<Object> commonResponse = CommonResponse.builder()
//                .statusCode(HttpStatus.CREATED.value())
//                .message("Successfully registered")
//                .data(newCustomer)
//                .build();
//        return ResponseEntity.status((HttpStatus.CREATED)).body(commonResponse);
//    }

    @PostMapping(
            path = "/register-admin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterAdminRequest request) {
        RegisterAdminResponse register = authService.registerNewAdmin(request);

        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully registered")
                .data(register)
                .build();

        return ResponseEntity.status((HttpStatus.CREATED)).body(commonResponse);
    }

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try{
        LoginResponse login = authService.login(request);
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully Login")
                .data(login)
                .build();
        return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);

        }catch (BadCredentialsException e){
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Username Or Password is incorrect")
                .data(null)
                .build();
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(commonResponse);
        }
    }
}
