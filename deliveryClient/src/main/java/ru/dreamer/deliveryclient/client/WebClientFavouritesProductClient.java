package ru.dreamer.deliveryclient.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryclient.client.exception.ClientBadRequestException;
import ru.dreamer.deliveryclient.client.payload.NewFavouriteProductPayload;
import ru.dreamer.deliveryclient.entity.FavouriteProduct;

import java.util.List;

@RequiredArgsConstructor
public class WebClientFavouritesProductClient implements FavouritesProductClient {
    private final WebClient webClient;

    @Override
    public Flux<FavouriteProduct> findFavouriteProducts() {
        return this.webClient
                .get()
                .uri("feedback-api/favouriteProducts")
                .retrieve()
                .bodyToFlux(FavouriteProduct.class);
    }

    @Override
    public Mono<FavouriteProduct> findFavouriteProductByProductId(Long productId) {
        return this.webClient
                .get()
                .uri("feedback-api/favouriteProducts/byProductId/{productId}", productId)
                .retrieve()
                .bodyToMono(FavouriteProduct.class)
                .onErrorComplete(WebClientResponseException.NotFound.class);
    }

    @Override
    public Mono<FavouriteProduct> addFavouriteProduct(Long productId) {
        return this.webClient
                .post()
                .uri("feedback-api/favouriteProducts")
                .bodyValue(new NewFavouriteProductPayload(productId))
                .retrieve()
                .bodyToMono(FavouriteProduct.class)
                .onErrorMap(WebClientResponseException.BadRequest.class,
                        exception -> new ClientBadRequestException(exception,
                                ((List<String>) exception.getResponseBodyAs(ProblemDetail.class)
                                        .getProperties().get("errors"))));
    }

    @Override
    public Mono<Void> removeFavouriteProduct(Long id) {
        return this.webClient
                .delete()
                .uri("feedback-api/favouriteProducts/byProductId/{productId}", id)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
