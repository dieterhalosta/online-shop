package org.fastttrackit.onlineshop;

import org.fastttrackit.onlineshop.domain.Product;
import org.fastttrackit.onlineshop.domain.User;
import org.fastttrackit.onlineshop.service.CartService;
import org.fastttrackit.onlineshop.steps.ProductTestSteps;
import org.fastttrackit.onlineshop.steps.UserTestSteps;
import org.fastttrackit.onlineshop.transfer.cart.AddProductsToCartRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

@SpringBootTest
public class CartServiceIntegrationTests {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserTestSteps userTestSteps;

    @Autowired
    private ProductTestSteps productTestSteps;

    @Test
    public void testAddProductsToCart_whenNewUser_thenCreateCartForUser(){

        User user = userTestSteps.createUser();

        Product product = productTestSteps.createProduct();

        AddProductsToCartRequest request = new AddProductsToCartRequest();

        request.setProductIds(Collections.singletonList(product.getId()));

        cartService.addProductsToCard(user.getId(), request);
    }


}
