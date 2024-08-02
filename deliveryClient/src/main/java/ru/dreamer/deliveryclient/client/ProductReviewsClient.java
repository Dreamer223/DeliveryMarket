package ru.dreamer.deliveryclient.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryclient.entity.ProductReview;

public interface ProductReviewsClient {

    Flux<ProductReview> findProductReviewsByProductId(Long productId);
    Mono<ProductReview> createProductReview(Long productId, Integer rating, String text);
}
