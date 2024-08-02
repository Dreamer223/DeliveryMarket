package ru.dreamer.deliveryfeedbackservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryfeedbackservice.controller.payload.NewProductReviewPayload;
import ru.dreamer.deliveryfeedbackservice.entity.ProductReview;
import ru.dreamer.deliveryfeedbackservice.service.ProductReviewsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("feedback-api/productReviews")
public class ProductReviewsRestController {
    private final ProductReviewsService productReviewsService;

    @GetMapping("byProductId/{productId:\\d+}")
    public Flux<ProductReview> findProductReviews(@PathVariable("productId") Long productId) {
        return productReviewsService.findAllProductReviewsByProductId(productId);
    }

    @PostMapping
    public Mono<ResponseEntity<ProductReview>> createProductReview(
            @Valid @RequestBody Mono<NewProductReviewPayload> payloadMono,
            UriComponentsBuilder uriComponentsBuilder) {
        return payloadMono
                .flatMap(payload -> this.productReviewsService.createProductReview(
                        payload.productId(), payload.rating(), payload.text()))
                .map(productReview->ResponseEntity
                        .created(uriComponentsBuilder.replacePath("/feedback-api/productReviews/{id}")
                                .build(productReview.getId()))
                        .body(productReview));
    }


}
