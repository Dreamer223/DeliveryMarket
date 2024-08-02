package ru.dreamer.deliveryfeedbackservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryfeedbackservice.controller.payload.NewFavouriteProductPayload;
import ru.dreamer.deliveryfeedbackservice.entity.FavouriteProduct;
import ru.dreamer.deliveryfeedbackservice.service.FavouriteProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("feedback-api/favouriteProducts")
public class FavouriteProductRestController {

    public final FavouriteProductService favouriteProductService;

    @GetMapping
    public Flux<FavouriteProduct> findAllFavouriteProducts() {
        return this.favouriteProductService.findFavouriteProducts();
    }

    @GetMapping("byProductId/{productId:\\d+}")
    public Mono<FavouriteProduct> findFavouriteProductsByProductId(@PathVariable("productId") Long productId) {
        return this.favouriteProductService.findFavouriteProductByProductId(productId);
    }
    @PostMapping
    public Mono<ResponseEntity<FavouriteProduct>> addFavouriteProduct(
            @Valid @RequestBody Mono<NewFavouriteProductPayload> payloadMono,
            UriComponentsBuilder uriComponentsBuilder) {
        return payloadMono
                .flatMap(payload ->this.favouriteProductService.addFavouriteProduct(payload.productId()))
                .map(favouriteProduct -> ResponseEntity
                        .created(uriComponentsBuilder.replacePath("feedback-api/favouriteProducts/{id}")
                                .build(favouriteProduct.getId()))
                .body(favouriteProduct));
    }

    @DeleteMapping("byProductId/{productId:\\d+}")
    public Mono<ResponseEntity<Void>> removeFavouriteProduct(@PathVariable("productId") Long productId) {
        return this.favouriteProductService.removeFavouriteProduct(productId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

}
