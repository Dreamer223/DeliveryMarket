package ru.dreamer.deliverymarket.controller.payload;


public record UpdateProductPayload(
        String name,
        String category,
        String description,
        Double price
) {
}
