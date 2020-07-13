package org.fastttrackit.onlineshop.transfer.cart;

import javax.validation.constraints.NotNull;
import java.util.List;

public class AddProductsToCartRequest {

    @NotNull
    private List<Long> productIds;

}
