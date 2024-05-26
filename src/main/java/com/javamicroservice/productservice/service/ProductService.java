package com.javamicroservice.productservice.service;


import com.javamicroservice.productservice.dto.ProductRequest;
import com.javamicroservice.productservice.dto.ProductResponse;
import com.javamicroservice.productservice.model.Product;
import com.javamicroservice.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    @Autowired
    private final ProductRepository productRepository; // constructor based DI

    // need to create constructor, so instead of doing it manually, we use
    // lombok annotation for RequiredArgsConstructor #line 12

    public void createProduct(ProductRequest productRequest){

        // ProductRequest needs to be mapped with Product model
        // we are creating instance of product
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        // now we have to save this to DB, so need to access productRepository
        // so for this, need to inject the dependency of repository into this ProductService class.
        // for that, we are doing constructor based injection #line 14

        // saving the product in DB
        productRepository.save(product);

        // need to log using slf4j
        log.info("Product {} is saved", product.getId());

    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();

        //now we have to map the Product class into ProductResponse class
        return products.stream().map(product -> mapToProductResponse(product)).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }


}
