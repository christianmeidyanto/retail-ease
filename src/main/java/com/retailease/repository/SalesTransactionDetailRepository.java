package com.retailease.repository;

import com.retailease.entity.SalesTransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesTransactionDetailRepository extends JpaRepository<SalesTransactionDetail,String> , JpaSpecificationExecutor<SalesTransactionDetailRepository> {
}
