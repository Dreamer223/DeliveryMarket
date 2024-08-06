package ru.dreamer.deliveryfeedbackservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    public Flux<FavouriteProduct> findAllFavouriteProducts(Mono<JwtAuthenticationToken> authenticationTokenMono) {
        return authenticationTokenMono.flatMapMany(authenticationToken ->
                this.favouriteProductService.findFavouriteProducts(authenticationToken.getToken().getSubject()));
    }

    @GetMapping("byProductId/{productId:\\d+}")
    public Mono<FavouriteProduct> findFavouriteProductsByProductId(
            Mono<JwtAuthenticationToken> authenticationTokenMono,
            @PathVariable("productId") Long productId) {
        return authenticationTokenMono.flatMap(authenticationToken ->
                this.favouriteProductService.findFavouriteProductByProductId(productId,
                        authenticationToken.getToken().getSubject()));
    }
    @PostMapping
    public Mono<ResponseEntity<FavouriteProduct>> addFavouriteProduct(
            Mono<JwtAuthenticationToken> authenticationTokenMono,
            @Valid @RequestBody Mono<NewFavouriteProductPayload> payloadMono,
            UriComponentsBuilder uriComponentsBuilder) {
        return Mono.zip(authenticationTokenMono,payloadMono)
                .flatMap(tuple ->this.favouriteProductService.addFavouriteProduct(tuple.getT2().productId(),
                        tuple.getT1().getToken().getSubject()))
                .map(favouriteProduct -> ResponseEntity
                        .created(uriComponentsBuilder.replacePath("feedback-api/favouriteProducts/{id}")
                                .build(favouriteProduct.getId()))
                .body(favouriteProduct));
    }

    @DeleteMapping("byProductId/{productId:\\d+}")
    public Mono<ResponseEntity<Void>> removeFavouriteProduct(
            Mono<JwtAuthenticationToken> authenticationTokenMono,
            @PathVariable("productId") Long productId) {
        return authenticationTokenMono
                .flatMap(authenticationToken ->this.favouriteProductService.removeFavouriteProduct(productId,
                        authenticationToken.getToken().getSubject()))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

}
