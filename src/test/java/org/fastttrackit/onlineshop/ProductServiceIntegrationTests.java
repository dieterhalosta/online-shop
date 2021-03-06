package org.fastttrackit.onlineshop;

import org.fastttrackit.onlineshop.domain.Product;
import org.fastttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fastttrackit.onlineshop.service.ProductService;
import org.fastttrackit.onlineshop.steps.ProductTestSteps;
import org.fastttrackit.onlineshop.transfer.product.GetProductsRequest;
import org.fastttrackit.onlineshop.transfer.product.ProductResponse;
import org.fastttrackit.onlineshop.transfer.product.SaveProductRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductServiceIntegrationTests {

    //field injection
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductTestSteps productTestSteps;

    @Test
    void createProduct_whenValidRequest_thenReturnCreatedProduct() {
        productTestSteps.createProduct();
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
        ProductResponse product = productTestSteps.createProduct();

        ProductResponse response = productService.getProductResponse(product.getId());

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
                () -> productService.getProductResponse(0));
    }

    @Test
    void getProducts_whenOneExistingProduct_thenReturnPageOfOneProduct(){
        ProductResponse product = productTestSteps.createProduct();

        Page<ProductResponse> productsPage = productService.getProducts(new GetProductsRequest(), PageRequest.of(0, 1000));

        assertThat(productsPage, notNullValue());
        assertThat(productsPage.getTotalElements(), is(1L));
        assertThat(productsPage.getContent().get(0).getId(), is(product.getId()));
        assertThat(productsPage.getContent().get(0).getName(), is(product.getName()));
        assertThat(productsPage.getContent().get(0).getPrice(), is(product.getPrice()));
        assertThat(productsPage.getContent().get(0).getQuantity(), is(product.getQuantity()));
        assertThat(productsPage.getContent().get(0).getDescription(), is(product.getDescription()));
        assertThat(productsPage.getContent().get(0).getImageUrl(), is(product.getImageUrl()));

    }

    @Test
    void updateProduct_whenValidRequest_thenReturnUpdatedProduct(){
        ProductResponse product = productTestSteps.createProduct();

        SaveProductRequest request = new SaveProductRequest();
        request.setName(product.getName() + " updated");
        request.setPrice(product.getPrice() + 10);
        request.setQuantity(product.getQuantity() + 10);

        ProductResponse updatedProduct = productService.updateProduct(product.getId(), request);

        assertThat(updatedProduct, notNullValue());
        assertThat(updatedProduct.getId(), is(product.getId()));
        assertThat(updatedProduct.getName(), is(request.getName()));
        assertThat(updatedProduct.getPrice(), is(request.getPrice()));
        assertThat(updatedProduct.getQuantity(), is(request.getQuantity()));

    }

    @Test
    void deleteProduct_whenExistingRequest_thenProductDoesNotExistAnymore(){
        ProductResponse product = productTestSteps.createProduct();

        productService.deleteProduct(product.getId());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductResponse(product.getId()));
    }



}
