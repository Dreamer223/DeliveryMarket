package ru.dreamer.deliveryclient.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryclient.entity.ShoppingCart;

public interface ShoppingCartClient {

    Flux<ShoppingCart> findAllShoppingCart();
    Mono<ShoppingCart> findByProductId(Long productId);
    Mono<ShoppingCart> addProductToShoppingCart(Long productId, int count);
    Mono<Void> removeProductFromShoppingCart(Long productId, String userId);
    Mono<Void> removeAllProducts();
}
