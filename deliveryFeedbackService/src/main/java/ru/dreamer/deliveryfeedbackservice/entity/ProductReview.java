package ru.dreamer.deliveryfeedbackservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReview {
    private UUID id;
    private Long productId;
    private Integer rating;
    private String text;
}
