package ru.dreamer.deliveryOrderClient.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryOrderClient.entity.ShoppingCart;
import ru.dreamer.deliveryOrderClient.repository.ShoppingCartRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultShoppingCartService implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public Flux<ShoppingCart> findAllByUserId(String userId) {
        return shoppingCartRepository.findAllByUserId(userId);
    }

    @Override
    public Mono<ShoppingCart> findByUserIdAndProductId(String userId, List<Long> productId) {
        return shoppingCartRepository.findByUserIdAndProductId(userId,productId);
    }

    @Override
    public Mono<ShoppingCart> save(List<Long> productId, String userId, Double price) {
        return shoppingCartRepository.save(new ShoppingCart(UUID.randomUUID(),
                userId, productId, price));
    }

    @Override
    public Mono<Void> deleteByUserIdAndProductId(String userId, List<Long> productId) {
        return shoppingCartRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public Mono<Void> deleteAllByUserId(String userId) {
        return shoppingCartRepository.deleteAllByUserId(userId);
    }
}
