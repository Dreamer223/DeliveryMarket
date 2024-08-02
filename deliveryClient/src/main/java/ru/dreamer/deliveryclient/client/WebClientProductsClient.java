package ru.dreamer.deliveryclient.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryclient.entity.Product;

@RequiredArgsConstructor
public class WebClientProductsClient implements ProductsClient {

    private final WebClient webClient;
    @Override
    public Flux<Product> findAllProducts(String filter) {
        return this.webClient.get()
                .uri("/api/products?filter={filter}", filter)
                .retrieve()
                .bodyToFlux(Product.class);
    }

    @Override
    public Mono<Product> findProduct(Long productId) {
        return this.webClient.get()
                .uri("/api/products/{id:\\d+}", productId)
                .retrieve()
                .bodyToMono(Product.class)
                .onErrorComplete(WebClientResponseException.class);
    }
}
