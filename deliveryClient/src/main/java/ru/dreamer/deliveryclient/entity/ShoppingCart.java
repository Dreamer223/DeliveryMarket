package ru.dreamer.deliveryclient.entity;

import java.util.UUID;

public record ShoppingCart(
    UUID id,
    String userId,
    Long productId,
    int count
) {
}
