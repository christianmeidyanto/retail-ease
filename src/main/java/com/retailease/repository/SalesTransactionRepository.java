package com.retailease.repository;

import com.retailease.entity.SalesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesTransactionRepository extends JpaRepository<SalesTransaction,String>, JpaSpecificationExecutor<SalesTransaction> {
}
