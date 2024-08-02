package ru.dreamer.deliveryfeedbackservice.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryfeedbackservice.entity.FavouriteProduct;

public interface FavouriteProductService {

    Mono<FavouriteProduct> addFavouriteProduct(Long productId);

    Mono<Void> removeFavouriteProduct(Long productId);

    Flux<FavouriteProduct> findFavouriteProducts();
    Mono<FavouriteProduct> findFavouriteProductByProductId(Long productId);
}
