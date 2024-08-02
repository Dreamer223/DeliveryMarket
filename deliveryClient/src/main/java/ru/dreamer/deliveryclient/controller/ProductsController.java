package ru.dreamer.deliveryclient.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import ru.dreamer.deliveryclient.client.FavouritesProductClient;
import ru.dreamer.deliveryclient.client.ProductsClient;
import ru.dreamer.deliveryclient.entity.FavouriteProduct;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductsController {
    private final ProductsClient productsClient;
    private final FavouritesProductClient favouritesProductClient;

    @GetMapping("list")
    public Mono<String> getProductsList(Model model,
                                                @RequestParam (name = "filter", required = false) String filter) {
        model.addAttribute("filter", filter);
        return this.productsClient.findAllProducts(filter)
                .collectList()
                .doOnNext(products -> model.addAttribute("products", products))
                .thenReturn("customers/products/list");

    }

    @GetMapping("favourite")
    public Mono<String> getFavouriteProductsList(Model model,
                                                 @RequestParam (name = "filter", required = false) String filter) {

        model.addAttribute("filter", filter);
        return this.favouritesProductClient.findFavouriteProducts()
                .map(FavouriteProduct::productId)
                .collectList()
                .flatMap(favouriteProducts -> this.productsClient.findAllProducts(filter)
                        .filter(product -> favouriteProducts.contains(product.id()))
                        .collectList()
                        .doOnNext(products -> model.addAttribute("products", products)))
                .thenReturn("customers/products/favourites");
    }
}
