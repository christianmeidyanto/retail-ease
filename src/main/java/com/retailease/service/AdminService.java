package com.retailease.service;

import com.retailease.entity.Admin;
import com.retailease.model.response.AdminResponse;

public interface AdminService {
    AdminResponse getAdminByUsername(String username);
    Admin getById(String id);
}
