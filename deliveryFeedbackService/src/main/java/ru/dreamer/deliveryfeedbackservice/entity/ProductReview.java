package ru.dreamer.deliveryfeedbackservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("review")
public class ProductReview {
    @Id
    private UUID id;
    private Long productId;
    private Integer rating;
    private String review;
    private String userId;
}
