package ru.dreamer.deliveryservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.dreamer.deliveryservice.controller.payload.NewProductPayload;
import ru.dreamer.deliveryservice.entity.Product;
import ru.dreamer.deliveryservice.service.ProductService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductsRestController {
    private final ProductService productService;

    @GetMapping
    public Iterable<Product> findAll(@RequestParam(name = "filter", required = false) String filter) {
        return this.productService.findAll(filter);
    }
    @PostMapping
    public ResponseEntity<?> save(@Valid@RequestBody NewProductPayload payload,
                                        BindingResult bindingResult,
                                        UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if(bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }else {
                throw new BindException(bindingResult);
            }
        }else {
            Product product = this.productService.save(payload.name(), payload.category(),
                    payload.description(), payload.price());
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .replacePath("/api/products/{id}")
                            .build(Map.of("id",product.getId())))
                    .body(product);
        }


    }
}
