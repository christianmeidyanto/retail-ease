package com.retailease.service.impl;

import com.retailease.entity.ProductPrice;
import com.retailease.model.response.ProductPriceResponse;
import com.retailease.repository.ProductPriceRepository;
import com.retailease.service.ProductPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductPriceServiceImpl implements ProductPriceService {

    private final ProductPriceRepository productPriceRepository;

    @Transactional
    @Override
    public ProductPrice create(ProductPrice productPrice) {
        return productPriceRepository.save(productPrice);
    }

    @Override
    public ProductPrice getById(String id) {
        return productPriceRepository.findByProduct_IdAndIsActive(id, true).get();
    }
//
//    @Override
//    public ProductPrice findProductPriceActive(String productId, Boolean active) {
//        return productPriceRepository.findByProduct_IdAndIsActive(productId,active)
//                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product Not Found"));
//    }

}
