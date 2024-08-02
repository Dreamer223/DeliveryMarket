package ru.dreamer.deliveryclient.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryclient.entity.FavouriteProduct;

public interface FavouritesProductClient {


    Flux<FavouriteProduct> findFavouriteProducts();

    Mono<FavouriteProduct> findFavouriteProductByProductId(Long productId);

    Mono<FavouriteProduct> addFavouriteProduct(Long id);

    Mono<Void> removeFavouriteProduct(Long id);
}
