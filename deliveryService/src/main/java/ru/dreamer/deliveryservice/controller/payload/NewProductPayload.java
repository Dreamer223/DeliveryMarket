package ru.dreamer.deliveryservice.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductPayload(
        @NotNull(message = "Name cannot be null")
        @Size(min = 3, message = "Name must be at least 3 characters long")
        String name,
        @NotNull(message = "Category cannot be null")
        @Size(min = 2, message = "Category must be at least 3 characters long")
        String category,
        String description,
        @NotNull(message = "Price cannot be null")
        Double price
) {
}
