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
import ru.dreamer.deliveryclient.client.payload.NewReviewProductPayload;
import ru.dreamer.deliveryclient.entity.ProductReview;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class WebClientProductReviewsClient implements ProductReviewsClient {
    private final WebClient webClient;
    @Override
    public Flux<ProductReview> findProductReviewsByProductId(Long productId) {
        return
                this.webClient
                        .get()
                        .uri("feedback-api/productReviews/byProductId/{productId}", productId)
                        .retrieve()
                        .bodyToFlux(ProductReview.class);
    }

    @Override
    public Mono<ProductReview> createProductReview(Long productId, Integer rating, String review) {
        return this.webClient
                .post()
                .uri("/feedback-api/productReviews")
                .bodyValue(new NewReviewProductPayload(productId, rating, review))
                .retrieve()
                .bodyToMono(ProductReview.class)
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
}
