package ru.dreamer.deliveryfeedbackservice.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryfeedbackservice.entity.ProductReview;

public interface ProductReviewsService {

    Mono<ProductReview> createProductReview(Long productId, Integer rating, String text);

    Flux<ProductReview> findAllProductReviewsByProductId(Long productId);
}
