package ru.dreamer.deliveryclient.controller.payload;

public record NewProductReviewPayload(
        Integer rating,
        String review) {
}
