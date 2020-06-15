package org.fastttrackit.onlineshop.service;

import org.fastttrackit.onlineshop.domain.Product;
import org.fastttrackit.onlineshop.persistance.ProductRepository;
import org.fastttrackit.onlineshop.transfer.SaveProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Spring Bean (services, repositories, etc)
@Service
public class ProductService {


    // IoC (Inversion of Control)
    private final ProductRepository productRepository;

    // Dependency Injection
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(SaveProductRequest request){
        // todo: replace with logger
        System.out.println("Creating product: " + request);

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setImageUrl(request.getImageUrl());

        return productRepository.save(product);

    }
}