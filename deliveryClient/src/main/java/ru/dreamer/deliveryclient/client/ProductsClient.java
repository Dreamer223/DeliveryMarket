package ru.dreamer.deliveryclient.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryclient.entity.Product;

public interface ProductsClient {


    Flux<Product> findAllProducts(String filter);

    Mono<Product> findProduct(Long productId);
}
