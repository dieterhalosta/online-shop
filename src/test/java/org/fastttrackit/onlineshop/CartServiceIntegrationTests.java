package org.fastttrackit.onlineshop;

import org.fastttrackit.onlineshop.domain.Product;
import org.fastttrackit.onlineshop.domain.User;
import org.fastttrackit.onlineshop.service.CartService;
import org.fastttrackit.onlineshop.steps.ProductTestSteps;
import org.fastttrackit.onlineshop.steps.UserTestSteps;
import org.fastttrackit.onlineshop.transfer.cart.AddProductsToCartRequest;
import org.fastttrackit.onlineshop.transfer.cart.CartResponse;
import org.fastttrackit.onlineshop.transfer.product.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.constraints.NotNull;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;


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

        ProductResponse product = productTestSteps.createProduct();

        AddProductsToCartRequest request = new AddProductsToCartRequest();

        request.setProductIds(Collections.singletonList(product.getId()));

        cartService.addProductsToCard(user.getId(), request);

        CartResponse cartResponse = cartService.getCart(user.getId());

        assertThat(cartResponse, notNullValue());
        assertThat(cartResponse.getId(), is(user.getId()));
        assertThat(cartResponse.getProducts(), notNullValue());
        assertThat(cartResponse.getProducts(), hasSize(1));
        assertThat(cartResponse.getProducts().get(0), notNullValue());
        assertThat(cartResponse.getProducts().get(0).getId(), is(product.getId()));
        assertThat(cartResponse.getProducts().get(0).getName(), is(product.getName()));
        assertThat(cartResponse.getProducts().get(0).getPrice(), is(product.getPrice()));
        assertThat(cartResponse.getProducts().get(0).getImageUrl(), is(product.getImageUrl()));


    }


}
