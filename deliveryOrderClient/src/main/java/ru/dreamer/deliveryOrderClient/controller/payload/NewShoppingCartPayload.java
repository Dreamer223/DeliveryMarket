package ru.dreamer.deliveryOrderClient.controller.payload;

import jakarta.validation.constraints.NotNull;

public record NewShoppingCartPayload(
    @NotNull(message = "{shoppingCart.Product_Id.not.null}")
    Long productId,
    @NotNull(message = "{shoppingCart.count.not.null}")
    int count
) {
}
