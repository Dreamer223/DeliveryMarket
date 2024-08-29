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
import ru.dreamer.deliveryclient.client.payload.NewShoppingCartPayload;
import ru.dreamer.deliveryclient.entity.ShoppingCart;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class WebClientShoppingCartClient implements ShoppingCartClient {
    private final WebClient webClient;

    @Override
    public Flux<ShoppingCart> findAllShoppingCart() {
        return webClient.get()
                .uri("shoppingCart/v1")
                .retrieve()
                .bodyToFlux(ShoppingCart.class);
    }

    @Override
    public Mono<ShoppingCart> findByProductId(Long productId) {
        return this.webClient
                .get()
                .uri("shoppingCart/v1/byUserId/{productId}", productId)
                .retrieve()
                .bodyToMono(ShoppingCart.class)
                .onErrorComplete(WebClientResponseException.NotFound.class);
    }

    @Override
    public Mono<ShoppingCart> addProductToShoppingCart(Long productId, Double price) {
        return this.webClient
                .post()
                .uri("shoppingCart/v1")
                .bodyValue(new NewShoppingCartPayload(productId, price))
                .retrieve()
                .bodyToMono(ShoppingCart.class)
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
    public Mono<Void> removeProductFromShoppingCart(Long productId) {
        return this.webClient
                .delete()
                .uri("shoppingCart/v1/byUserId/{productId}", productId)
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    @Override
    public Mono<Void> removeAllProducts() {
        return this.webClient
                .delete()
                .uri("shoppingCart/v1/byUserId")
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
