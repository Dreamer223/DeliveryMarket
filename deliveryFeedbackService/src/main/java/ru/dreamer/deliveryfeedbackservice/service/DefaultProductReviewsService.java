package ru.dreamer.deliveryfeedbackservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryfeedbackservice.entity.ProductReview;
import ru.dreamer.deliveryfeedbackservice.repository.ProductReviewRepository;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class DefaultProductReviewsService implements ProductReviewsService {

    private final ProductReviewRepository productReviewRepository;

    @Override
    public Flux<ProductReview> findAllProductReviewsByProductId(Long productId) {
        return productReviewRepository.findAllByProductId(productId);
    }

    @Override
    public Mono<ProductReview> createProductReview(Long productId, Integer rating, String text,String userId) {
        return productReviewRepository.save(new ProductReview(UUID.randomUUID(), productId, rating, text, userId));
    }
}
