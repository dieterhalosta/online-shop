package org.fastttrackit.onlineshop.service;

import org.fastttrackit.onlineshop.domain.Cart;
import org.fastttrackit.onlineshop.domain.Product;
import org.fastttrackit.onlineshop.domain.User;
import org.fastttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fastttrackit.onlineshop.persistance.CartRepository;
import org.fastttrackit.onlineshop.transfer.cart.AddProductsToCartRequest;
import org.fastttrackit.onlineshop.transfer.cart.CartResponse;
import org.fastttrackit.onlineshop.transfer.cart.ProductInCartResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;

    private final UserService userService;

    private final ProductService productService;

    @Autowired
    public CartService(CartRepository cartRepository, UserService userService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.productService = productService;
    }

    @Transactional
    public void addProductsToCard(long cartId, AddProductsToCartRequest request){
        LOGGER.info("Adding products to card {}: {}", cartId, request);

        Cart cart = cartRepository.findById(cartId).orElse(new Cart());

        if (cart.getUser() == null) {
            User user = userService.getUser(cartId);

            cart.setUser(user);
        }

        for (Long productId : request.getProductIds()){
            Product product = productService.getProduct(productId);

            cart.addProduct(product);
        }


            cartRepository.save(cart);
    }


    @Transactional
    public CartResponse getCart(long id){
        LOGGER.info("Retrieving cart {}", id);

        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart " + id + " does not exist"));

        CartResponse cartResponse = new CartResponse();
        cartResponse.setId(cart.getId());

        List<ProductInCartResponse> productDtos = new ArrayList<>();

        for(Product product : cart.getProducts()){
            ProductInCartResponse productResponse = new ProductInCartResponse();
            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setPrice(product.getPrice());
            productResponse.setImageUrl(product.getImageUrl());
            productDtos.add(productResponse);
        }

        cartResponse.setProducts(productDtos);

        return cartResponse;

    }
}
