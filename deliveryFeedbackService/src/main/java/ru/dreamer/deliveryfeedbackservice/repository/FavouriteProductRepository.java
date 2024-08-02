package ru.dreamer.deliveryfeedbackservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryfeedbackservice.entity.FavouriteProduct;

public interface FavouriteProductRepository extends ReactiveCrudRepository<FavouriteProduct, Integer> {

    Mono<Void> deleteByProductIdAndUserId(Long productId,String userId);

    Flux<FavouriteProduct> findAllByUserId(String userId);

    Mono<FavouriteProduct> findByProductIdAndUserId(Long productId, String userId);
}
