package com.retailease.service.impl;

import com.retailease.entity.Product;
import com.retailease.entity.ProductPrice;
import com.retailease.model.request.ProductRequest;
import com.retailease.model.request.UpdateProductRequest;
import com.retailease.model.response.ProductPriceResponse;
import com.retailease.model.response.ProductResponse;
import com.retailease.repository.ProductRepository;
import com.retailease.service.ProductPriceService;
import com.retailease.service.ProductService;
import com.retailease.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductPriceService productPriceService;
    private final ValidationService validationService;

    @Override
    public ProductResponse getById(String id) {
        Product product = productRepository.findById(id).get();
        ProductPrice productPrice = productPriceService.getById(id);
        return ProductResponse.builder()
                .id(product.getId())
                .productCode(product.getCode())
                .productName(product.getName())
                .price(productPrice.getPrice())
                .stock(productPrice.getStock())
                .build();
    }

    @Transactional
    @Override
    public ProductResponse update(UpdateProductRequest product) {
        validationService.validate(product);
        try {
        Optional<Product> product1 = productRepository.findById(product.getProductId());
        ProductPrice productPrice1 = productPriceService.getById(product.getProductId());
        if(product1.isPresent()){
            product1.get().setCode(product.getProductCode());
            product1.get().setName(product.getProductName());
            productRepository.saveAndFlush(product1.get());
            if(!productPrice1.getPrice().equals(product.getPrice())){
                ProductPrice productPrice = ProductPrice.builder()
                        .price(product.getPrice())
                        .stock(productPrice1.getStock())
                        .product(product1.get())
                        .isActive(true)
                        .build();
                productPrice1.setIsActive(false);
                productPriceService.create(productPrice);
            }
            productPrice1 = productPriceService.getById(product.getProductId());
            return ProductResponse.builder()
                    .id(product1.get().getId())
                    .productCode(product1.get().getCode())
                    .productName(product1.get().getName())
                    .price(productPrice1.getPrice())
                    .stock(productPrice1.getStock())
                    .build();
        }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update customer");
        }
        return null;
    }



    @Override
    public Page<ProductResponse> getAllProducts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product:products.getContent()){
            Optional<ProductPrice> productPrice = product.getProductPriceList()
                    .stream()
                    .filter(ProductPrice::getIsActive)
                    .findFirst();

            if(productPrice.isEmpty()) continue;

            productResponses.add(toProductResponse(product,productPrice.get()));
        }
        return new PageImpl<>(productResponses,pageable,products.getTotalElements());
    }

    @Transactional
    @Override
    public ProductResponse createProduct(ProductRequest request) {
        validationService.validate(request);
        try {
        /*Set Product*/
        Product product = Product.builder()
                .code(request.getProductCode())
                .name(request.getProductName())
                .build();
        productRepository.saveAndFlush(product);

        /*Set ProductPrice*/
        ProductPrice productPrice = ProductPrice.builder()
                .price(request.getPrice())
                .stock(request.getStock())
                .product(product)
                .isActive(true)
                .build();
        productPriceService.create(productPrice);

        return toProductResponse(product, productPrice);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create customer");
        }
    }

    private static ProductResponse toProductResponse(Product product, ProductPrice productPrice) {
        return ProductResponse.builder()
                .id(product.getId())
                .productCode(product.getCode())
                .productName(product.getName())
                .price(productPrice.getPrice())
                .stock(productPrice.getStock())
                .build();
    }
}
