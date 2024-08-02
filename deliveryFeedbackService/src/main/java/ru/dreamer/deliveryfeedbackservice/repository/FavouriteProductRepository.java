package ru.dreamer.deliveryfeedbackservice.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryfeedbackservice.entity.FavouriteProduct;

public interface FavouriteProductRepository {
    Mono<FavouriteProduct> save(FavouriteProduct favouriteProduct);

    Mono<Void> deleteByProductId(Long productId);

    Mono<FavouriteProduct> findByProductId(Long productId);

    Flux<FavouriteProduct> findAll();
}
