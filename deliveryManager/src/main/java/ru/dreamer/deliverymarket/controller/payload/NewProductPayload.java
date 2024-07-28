package ru.dreamer.deliverymarket.controller.payload;

public record NewProductPayload(
        String name,
        String category,
        String description,
        Double price
) {
}
