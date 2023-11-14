package com.retailease.service;

import com.retailease.entity.ProductPrice;
import com.retailease.model.response.ProductPriceResponse;

public interface ProductPriceService {
    ProductPrice create(ProductPrice productPrice);

    ProductPrice getById(String id);
//
//    ProductPrice findProductPriceActive(String productId, Boolean active);
}
