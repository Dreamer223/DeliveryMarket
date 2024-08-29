package ru.dreamer.deliveryOrderClient.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryOrderClient.entity.ShoppingCart;

import java.util.List;
import java.util.UUID;
@Repository
public interface ShoppingCartRepository extends ReactiveCrudRepository<ShoppingCart, UUID> {

    Mono<Void> deleteByUserIdAndProductId(String userId, List<Long> productId);

    Flux<ShoppingCart> findAllByUserId(String userId);

    Mono<ShoppingCart> findByUserIdAndProductId(String userId, List<Long> productId);

    @Query("{ 'userId': ?0 }")
    Mono<Void> deleteAllByUserId(String userId);

}
