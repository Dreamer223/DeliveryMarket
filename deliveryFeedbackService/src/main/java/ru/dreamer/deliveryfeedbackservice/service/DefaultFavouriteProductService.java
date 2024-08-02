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
    public Mono<FavouriteProduct> addFavouriteProduct(Long productId) {
        return this.favouriteProductRepository.save(new FavouriteProduct(UUID.randomUUID(), productId));
    }

    @Override
    public Mono<Void> removeFavouriteProduct(Long productId) {
        return this.favouriteProductRepository.deleteByProductId(productId);
    }

    @Override
    public Flux<FavouriteProduct> findFavouriteProducts() {
        return this.favouriteProductRepository.findAll();
    }

    @Override
    public Mono<FavouriteProduct> findFavouriteProductByProductId(Long productId) {
        return this.favouriteProductRepository.findByProductId(productId);
    }

}
