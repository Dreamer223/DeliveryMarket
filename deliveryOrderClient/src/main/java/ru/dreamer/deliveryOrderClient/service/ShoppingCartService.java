package ru.dreamer.deliveryOrderClient.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryOrderClient.entity.ShoppingCart;

public interface ShoppingCartService {

     Flux<ShoppingCart> findAllByUserId(String userId);
     Mono<ShoppingCart> findByUserIdAndProductId(String userId, Long productId);
     Mono<ShoppingCart> save(Long productId, String userId);
     Mono<Void> deleteByUserIdAndProductId(String userId, Long productId);
     Mono<Void> deleteAllByUserId(String userId);
}
