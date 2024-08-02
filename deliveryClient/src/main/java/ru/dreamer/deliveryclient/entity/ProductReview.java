package ru.dreamer.deliveryclient.entity;

import java.util.UUID;

public record ProductReview(UUID id,
                            Long productId,
                            Integer rating,
                            String text) {
}
