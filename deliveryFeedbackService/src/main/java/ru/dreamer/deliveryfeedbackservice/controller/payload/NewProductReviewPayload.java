package ru.dreamer.deliveryfeedbackservice.controller.payload;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductReviewPayload(
        @NotNull(message = "{feedback.product.review.Product_Id.not.null}")
        Long productId,
        @NotNull(message = "{feedback.product.review.rating.not.null}")
        @Min(value = 1, message = "{feedback.product.review.rating.min.value}")
        @Max(value = 5, message = "{feedback.product.review.rating.max.value}")
        Integer rating,
        @Size(max = 1000, message = "{feedback.product.review.text.max.length}")
        String text) {
}
