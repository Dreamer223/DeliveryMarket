package ru.dreamer.deliveryfeedbackservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryfeedbackservice.entity.FavouriteProduct;
import ru.dreamer.deliveryfeedbackservice.repository.FavouriteProductRepository;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class DefaultFavouriteProductService implements FavouriteProductService {

    private final FavouriteProductRepository favouriteProductRepository;
    @Override
    public Mono<FavouriteProduct> addFavouriteProduct(Long productId,String userId) {
        return this.favouriteProductRepository.save(new FavouriteProduct(UUID.randomUUID(), productId, userId));
    }

    @Override
    public Mono<Void> removeFavouriteProduct(Long productId,String userId) {
        return this.favouriteProductRepository.deleteByProductIdAndUserId(productId, userId);
    }

    @Override
    public Flux<FavouriteProduct> findFavouriteProducts(String userId) {
        return this.favouriteProductRepository.findAllByUserId(userId);
    }

    @Override
    public Mono<FavouriteProduct> findFavouriteProductByProductId(Long productId,String userId) {
        return this.favouriteProductRepository.findByProductIdAndUserId(productId, userId);
    }

}
