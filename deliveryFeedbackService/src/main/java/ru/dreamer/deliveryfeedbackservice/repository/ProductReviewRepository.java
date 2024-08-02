package ru.dreamer.deliveryfeedbackservice.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryfeedbackservice.entity.ProductReview;

public interface ProductReviewRepository {

    Mono<ProductReview> save(ProductReview productReview);

    Flux<ProductReview> findAllByProductId(Long productId);
}
