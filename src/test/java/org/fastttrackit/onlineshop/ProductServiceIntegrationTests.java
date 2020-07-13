package org.fastttrackit.onlineshop;

import org.fastttrackit.onlineshop.domain.Product;
import org.fastttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fastttrackit.onlineshop.service.ProductService;
import org.fastttrackit.onlineshop.transfer.product.GetProductsRequest;
import org.fastttrackit.onlineshop.transfer.product.SaveProductRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
class ProductServiceIntegrationTests {

    //field injection
    @Autowired
    private ProductService productService;

    @Test
    void createProduct_whenValidRequest_thenReturnCreatedProduct() {
        createProduct();
    }

    @Test
    void createProduct_whenMissingMandatoryProperties_thenThrowException(){
        SaveProductRequest request = new SaveProductRequest();


        try {
            productService.createProduct(request);
        } catch (Exception e) {
            assertThat("Unexpected exception thrown.", e instanceof ConstraintViolationException);
        }

    }

    @Test
    void getProduct_whenExistingProduct_thenReturnProduct(){
        Product product = createProduct();

        Product response = productService.getProduct(product.getId());

        assertThat(response, notNullValue());
        assertThat(response.getId(), is(product.getId()));
        assertThat(response.getName(), is(product.getName()));
        assertThat(response.getPrice(), is(product.getPrice()));
        assertThat(response.getQuantity(), is(product.getQuantity()));
        assertThat(response.getDescription(), is(product.getDescription()));
        assertThat(response.getImageUrl(), is(product.getImageUrl()));

    }

    @Test
    void getProduct_whenNonExistingProduct_thenThrowResourceNotFoundException(){
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.getProduct(0));
    }

    @Test
    void getProducts_whenOneExistingProduct_thenReturnPageOfOneProduct(){
        Product product = createProduct();

        Page<Product> productsPage = productService.getproducts(new GetProductsRequest(), PageRequest.of(0, 1000));

        assertThat(productsPage, notNullValue());
        assertThat(productsPage.getTotalElements(), greaterThanOrEqualTo(1L));
        assertThat(productsPage.getContent(), contains(product));

    }

    @Test
    void updateProduct_whenValidRequest_thenReturnUpdatedProduct(){
        Product product = createProduct();

        SaveProductRequest request = new SaveProductRequest();
        request.setName(product.getName() + " updated");
        request.setPrice(product.getPrice() + 10);
        request.setQuantity(product.getQuantity() + 10);

        Product updatedProduct = productService.updateProduct(product.getId(), request);

        assertThat(updatedProduct, notNullValue());
        assertThat(updatedProduct.getId(), is(product.getId()));
        assertThat(updatedProduct.getName(), is(request.getName()));
        assertThat(updatedProduct.getPrice(), is(request.getPrice()));
        assertThat(updatedProduct.getQuantity(), is(request.getQuantity()));

    }

    @Test
    void deleteProduct_whenExistingRequest_thenProductDoesNotExistAnymore(){
        Product product = createProduct();

        productService.deleteProduct(product.getId());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.getProduct(product.getId()));
    }


    private Product createProduct() {
        SaveProductRequest request = new SaveProductRequest();
        request.setName("Phone");
        request.setPrice(500);
        request.setQuantity(1000);


        Product product = productService.createProduct(request);

        //assertions
        assertThat(product, notNullValue());
        assertThat(product.getId(), greaterThan(0L));
        assertThat(product.getName(), is(request.getName()));
        assertThat(product.getPrice(), is(request.getPrice()));
        assertThat(product.getQuantity(), is(request.getQuantity()));

        return product;
    }
}
