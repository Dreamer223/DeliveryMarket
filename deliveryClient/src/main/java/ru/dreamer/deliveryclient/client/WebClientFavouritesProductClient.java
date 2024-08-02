package ru.dreamer.deliveryclient.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryclient.client.exception.ClientBadRequestException;
import ru.dreamer.deliveryclient.client.payload.NewFavouriteProductPayload;
import ru.dreamer.deliveryclient.entity.FavouriteProduct;

import java.util.Collections;
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
                .onErrorMap(WebClientResponseException.BadRequest.class, ex -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(ex.getResponseBodyAsString());
                        JsonNode errorsNode = root.path("errors");
                        List<String> errors = mapper.convertValue(errorsNode, new TypeReference<>() { });
                        return new ClientBadRequestException(ex, errors);
                    } catch (Exception e) {
                        return new ClientBadRequestException(ex, Collections.emptyList());
                    }
                });
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
