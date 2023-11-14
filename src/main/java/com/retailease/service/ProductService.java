package com.retailease.service;

import com.retailease.model.request.ProductRequest;
import com.retailease.model.request.UpdateProductRequest;
import com.retailease.model.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getById(String id);
    ProductResponse update(UpdateProductRequest product);
//    List<Product> getAll();
//    void deleteById(String id);
    Page<ProductResponse> getAllProducts(Integer page, Integer size);
}
