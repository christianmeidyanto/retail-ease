package com.retailease.repository;

import com.retailease.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,String> {
    Optional<Admin> findAdminByUserCredential_Username(String username);
}
