package ru.dreamer.deliveryclient.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.reactive.result.view.CsrfRequestDataValueProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryclient.client.FavouritesProductClient;
import ru.dreamer.deliveryclient.client.ProductReviewsClient;
import ru.dreamer.deliveryclient.client.ProductsClient;
import ru.dreamer.deliveryclient.client.exception.ClientBadRequestException;
import ru.dreamer.deliveryclient.controller.payload.NewProductReviewPayload;
import ru.dreamer.deliveryclient.entity.Product;

import java.util.NoSuchElementException;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products/{productId:\\d+}")
@Slf4j
public class ProductController {

    private final ProductsClient productsClient;
    private final FavouritesProductClient favouritesProductClient;
    private final ProductReviewsClient productReviewsClient;

    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable("productId") Long productId) {
        return productsClient.findProduct(productId)
                .switchIfEmpty(Mono.error(() -> new NoSuchElementException("catalogue.errors.product.not_found")));
    }

    @GetMapping
    public Mono<String> getProductPage(@PathVariable("productId") Long productId, Model model) {
        model.addAttribute("inFavourite", false);
        return this.productReviewsClient.findProductReviewsByProductId(productId)
                .collectList()
                .doOnNext(productReviews -> model.addAttribute("reviews", productReviews))
                .then(this.favouritesProductClient.findFavouriteProductByProductId(productId)
                        .doOnNext(favouriteProduct -> model.addAttribute("inFavourite", true)))
                .thenReturn("customers/products/product");

    }

    @PostMapping("add-favourite")
    public Mono<String> addFavouriteProduct(@ModelAttribute("product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(id -> this.favouritesProductClient.addFavouriteProduct(id)
                        .thenReturn("redirect:/products/%d".formatted(id))
                        .onErrorResume(exception -> {
                            log.error(exception.getMessage(), exception);
                            return Mono.just("redirect:/products/%d".formatted(id));
                        }));
    }

    @PostMapping("remove-favourite")
    public Mono<String> removeFavouriteProduct(@ModelAttribute("product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(id -> this.favouritesProductClient.removeFavouriteProduct(id)
                        .thenReturn("redirect:/products/%d".formatted(id)));
    }

    @PostMapping("create-review")
    public Mono<String> createReview(@PathVariable("productId") Long productId,
                                     NewProductReviewPayload payload,
                                     Model model) {
        return this.productReviewsClient.createProductReview(productId, payload.rating(), payload.review())
                .thenReturn("redirect:/products/%d".formatted(productId))
                .onErrorResume(ClientBadRequestException.class, exception -> {
                    model.addAttribute("inFavourite", false);
                    model.addAttribute("payload", payload);
                    model.addAttribute("errors", exception.getErrors());
                    return this.favouritesProductClient.findFavouriteProductByProductId(productId)
                            .doOnNext(favouriteProduct -> model.addAttribute("inFavourite", true))
                            .thenReturn("customer/products/product");
                });
    }



    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "errors/404";
    }

    @ModelAttribute
    public Mono<CsrfToken> loadCsrfToken(ServerWebExchange exchange) {
        return Objects.requireNonNull(exchange.<Mono<CsrfToken>>getAttribute(CsrfToken.class.getName()))
                .doOnSuccess(token -> exchange.getAttributes()
                        .put(CsrfRequestDataValueProcessor.DEFAULT_CSRF_ATTR_NAME, token));
    }

}

