package org.fastttrackit.onlineshop.web;


import org.fastttrackit.onlineshop.domain.Product;
import org.fastttrackit.onlineshop.service.ProductService;
import org.fastttrackit.onlineshop.transfer.SaveProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody SaveProductRequest request){
        Product product = productService.createProduct(request);


        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable long id, @Valid @RequestBody SaveProductRequest request){
        Product product = productService.updateProduct(id, request);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }


}
