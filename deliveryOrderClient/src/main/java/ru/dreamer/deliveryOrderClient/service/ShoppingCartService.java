package ru.dreamer.deliveryOrderClient.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryOrderClient.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

     Flux<ShoppingCart> findAllByUserId(String userId);
     Mono<ShoppingCart> findByUserIdAndProductId(String userId, List<Long> productId);
     Mono<ShoppingCart> save(List<Long> productId, String userId, Double price);
     Mono<Void> deleteByUserIdAndProductId(String userId, List<Long> productId);
     Mono<Void> deleteAllByUserId(String userId);
}
