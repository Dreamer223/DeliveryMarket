package ru.dreamer.deliveryclient.client.payload;

public record NewReviewProductPayload(Long productId,
                                      Integer rating,
                                      String text) {
}
