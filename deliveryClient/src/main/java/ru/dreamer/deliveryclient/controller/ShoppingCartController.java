package ru.dreamer.deliveryclient.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.reactive.result.view.CsrfRequestDataValueProcessor;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryclient.client.ProductsClient;
import ru.dreamer.deliveryclient.client.ShoppingCartClient;
import ru.dreamer.deliveryclient.entity.ShoppingCart;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("cart")
public class ShoppingCartController {
    private final ShoppingCartClient shoppingCartClient;
    private final ProductsClient productsClient;

    @GetMapping
    public Mono<String> getCartPage(Model model,
                                    @RequestParam(name = "filter", required = false) String filter) {
        model.addAttribute("filter", filter);
        return this.shoppingCartClient.findAllShoppingCart()
                .map(ShoppingCart::productId)
                .collectList()
                .flatMap(productsInCart -> this.productsClient.findAllProducts(filter)
                        .filter(product -> productsInCart.contains(product.id()))
                        .collectList()
                        .doOnNext(products -> model.addAttribute("products", products))
                .thenReturn("customers/shopping-cart/list"));

    }
    @PostMapping("clear")
    public Mono<String> removeFromCart() {
        return shoppingCartClient.removeAllProducts().thenReturn("redirect:/cart");
    }
    @ModelAttribute
    public Mono<CsrfToken> loadCsrfToken(ServerWebExchange exchange) {
        return Objects.requireNonNull(exchange.<Mono<CsrfToken>>getAttribute(CsrfToken.class.getName()))
                .doOnSuccess(token -> exchange.getAttributes()
                        .put(CsrfRequestDataValueProcessor.DEFAULT_CSRF_ATTR_NAME, token));
    }
}
