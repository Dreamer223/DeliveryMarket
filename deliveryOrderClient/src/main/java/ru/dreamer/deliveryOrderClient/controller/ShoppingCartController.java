package ru.dreamer.deliveryOrderClient.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryOrderClient.controller.payload.NewShoppingCartPayload;
import ru.dreamer.deliveryOrderClient.entity.ShoppingCart;
import ru.dreamer.deliveryOrderClient.service.ShoppingCartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("shoppingCart/v1")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public Flux<ShoppingCart> findAllByUserId(Mono<JwtAuthenticationToken> tokenMono) {
        return tokenMono.flatMapMany(token -> shoppingCartService.findAllByUserId(token.getToken().getSubject()));
    }

    @GetMapping("byUserId/{productId:\\d+}")
    public Mono<ShoppingCart> findByUserId(Mono<JwtAuthenticationToken> tokenMono,
                                           @PathVariable("productId") Long productId) {
        return tokenMono.flatMap(token -> shoppingCartService.findByUserIdAndProductId(
                token.getToken().getSubject(), productId));
    }
    @PostMapping
    public Mono<ResponseEntity<ShoppingCart>> addProductToCart(Mono<JwtAuthenticationToken> tokenMono,
                                                               @Valid @RequestBody Mono<NewShoppingCartPayload> payloadMono,
                                                               UriComponentsBuilder uriBuilder) {
             return Mono.zip(tokenMono, payloadMono)
                     .flatMap(tuple -> this.shoppingCartService.save(tuple.getT2().productId(),
                             tuple.getT1().getToken().getSubject(),
                             tuple.getT2().count()))
                     .map(shoppingCart -> ResponseEntity
                             .created(uriBuilder.replacePath("shoppingCart/v1/{id}")
                                     .build(shoppingCart.getId()))
                             .body(shoppingCart));
    }
    @DeleteMapping("byUserId/{productId:\\d+}")
    public Mono<ResponseEntity<Void>> removeProductFromCart(Mono<JwtAuthenticationToken> tokenMono,
                                                             @PathVariable("productId") Long productId) {
        return tokenMono.flatMap(token -> this.shoppingCartService.deleteByUserIdAndProductId(
                token.getToken().getSubject(), productId))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
    @DeleteMapping("byUserId")
    public Mono<ResponseEntity<Void>> removeAllProducts(Mono<JwtAuthenticationToken> tokenMono) {
        return tokenMono.flatMap(token -> this.shoppingCartService.deleteAllByUserId(token.getToken().getSubject()))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
