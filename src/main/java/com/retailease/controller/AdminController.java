package com.retailease.controller;

import com.retailease.model.response.AdminResponse;
import com.retailease.model.response.CommonResponse;
import com.retailease.model.response.CustomerResponse;
import com.retailease.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getAdminByUsername(@PathVariable String username) {
        try {
            AdminResponse adminResponse = adminService.getAdminByUsername(username);
            CommonResponse<Object> commonResponse = CommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully get by username admin")
                    .data(adminResponse)
                    .build();
            return ResponseEntity.status((HttpStatus.OK)).body(commonResponse);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(CommonResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Get by username admin : " + e.getMessage())
                    .data(null)
                    .build());
        }
    }
}
