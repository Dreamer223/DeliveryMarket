package ru.dreamer.deliveryservice.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductPayload(
        @NotNull(message = "{Не должно быть пустым полем названия}")
        @Size(min = 3, message = "{Не может быть меньше трех символов}")
        String name,
        @NotNull(message = "{Не должно быть пустым полем категории}")
        @Size(min = 2, message = "{не может быть меньше двух символов}")
        String category,
        String description,
        @NotNull(message = "{Не должно быть пустым полем цены}")
        Double price
) {
}
