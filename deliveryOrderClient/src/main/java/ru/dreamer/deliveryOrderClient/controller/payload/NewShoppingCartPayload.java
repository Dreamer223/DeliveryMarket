package ru.dreamer.deliveryOrderClient.controller.payload;

public record NewShoppingCartPayload(
    Long productId,
    Double price
) {
}
