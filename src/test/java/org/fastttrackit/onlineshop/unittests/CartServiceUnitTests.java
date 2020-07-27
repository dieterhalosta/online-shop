package org.fastttrackit.onlineshop.unittests;

import org.fastttrackit.onlineshop.domain.Cart;
import org.fastttrackit.onlineshop.domain.Product;
import org.fastttrackit.onlineshop.domain.User;
import org.fastttrackit.onlineshop.domain.UserRole;
import org.fastttrackit.onlineshop.persistance.CartRepository;
import org.fastttrackit.onlineshop.service.CartService;
import org.fastttrackit.onlineshop.service.ProductService;
import org.fastttrackit.onlineshop.service.UserService;
import org.fastttrackit.onlineshop.transfer.cart.AddProductsToCartRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceUnitTests {

    private CartService cartService;

    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserService userService;
    @Mock
    private ProductService productService;

    @BeforeEach
    public void setup(){
        cartService = new CartService(cartRepository, userService, productService);
    }

    //Clean up
//    @AfterEach
//    public void teardown(){
//
//    }

    @Test
    public void addProductsToCart_whenNewUser_thenNoErrorIsThrown(){
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());

        User user = new User();
        user.setId(1L);
        user.setRole(UserRole.CUSTOMER.name());
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");

        when(userService.getUser(anyLong())).thenReturn(user);

        Product product = new Product();
        product.setId(1L);
        product.setDescription("TestProd");
        product.setName("TestProduct");
        product.setPrice(233);
        product.setQuantity(3);

        when(productService.getProduct(anyLong())).thenReturn(product);

        when(cartRepository.save(any(Cart.class))).thenReturn(null);

        AddProductsToCartRequest request = new AddProductsToCartRequest();
        request.setProductIds(Collections.singletonList(product.getId()));

        cartService.addProductsToCard(user.getId(), request);

        verify(cartRepository).findById(anyLong());
        verify(userService).getUser(anyLong());
        verify(productService).getProduct(anyLong());
        verify(cartRepository).save(any(Cart.class));
    }

}
