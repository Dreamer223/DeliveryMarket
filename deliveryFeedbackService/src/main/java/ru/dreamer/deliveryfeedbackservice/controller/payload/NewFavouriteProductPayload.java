package ru.dreamer.deliveryfeedbackservice.controller.payload;

import jakarta.validation.constraints.NotNull;

public record NewFavouriteProductPayload(
        @NotNull(message = "{feedback.product.favourite.Product_Id.not.null}")
        Long productId) {
}
