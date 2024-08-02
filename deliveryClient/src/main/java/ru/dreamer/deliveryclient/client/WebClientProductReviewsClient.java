package ru.dreamer.deliveryclient.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryclient.client.exception.ClientBadRequestException;
import ru.dreamer.deliveryclient.client.payload.NewReviewProductPayload;
import ru.dreamer.deliveryclient.entity.ProductReview;

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
    public Mono<ProductReview> createProductReview(Long productId, Integer rating, String text) {
        return this.webClient
                .post()
                .uri("feedback-api/productReviews/")
                .bodyValue(new NewReviewProductPayload(productId,rating,text))
                .retrieve()
                .bodyToMono(ProductReview.class)
                .onErrorMap(WebClientResponseException.BadRequest.class,
                        exception-> new ClientBadRequestException(exception,
                                ((List<String>) exception.getResponseBodyAs(ProblemDetail.class).getProperties().get("errors"))));
    }
}
