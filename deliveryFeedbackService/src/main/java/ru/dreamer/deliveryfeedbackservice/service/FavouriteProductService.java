package ru.dreamer.deliveryfeedbackservice.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryfeedbackservice.entity.FavouriteProduct;

public interface FavouriteProductService {

    Mono<FavouriteProduct> addFavouriteProduct(Long productId,String userId);

    Mono<Void> removeFavouriteProduct(Long productId,String userId);

    Flux<FavouriteProduct> findFavouriteProducts(String userId);
    Mono<FavouriteProduct> findFavouriteProductByProductId(Long productId, String userId);
}
