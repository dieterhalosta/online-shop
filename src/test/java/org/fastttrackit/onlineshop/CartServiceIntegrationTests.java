package org.fastttrackit.onlineshop;

import org.fastttrackit.onlineshop.domain.User;
import org.fastttrackit.onlineshop.service.CartService;
import org.fastttrackit.onlineshop.steps.UserTestSteps;
import org.fastttrackit.onlineshop.transfer.cart.AddProductsToCartRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CartServiceIntegrationTests {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserTestSteps userTestSteps;

    @Test
    public void testAddProductsToCart_whenNewUser_thenCreateCartForUser(){

        User user = userTestSteps.createUser();


        AddProductsToCartRequest request = new AddProductsToCartRequest();
        // add product ids


        cartService.addProductsToCard(user.getId(), request);
    }


}
