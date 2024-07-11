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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductsRestController {
    private final ProductService productService;

    @GetMapping
    public Iterable<Product> findAll() {
        return this.productService.findAll();
    }
    @PostMapping
    public ResponseEntity<Product> save(@Valid @RequestBody NewProductPayload payload,
                                        BindingResult bindingResult,
                                        UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if(bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }else {
                throw new BindException(bindingResult);
            }
        }else {
            Product product = this.productService.save(payload.name(), payload.category(), payload.description(),
                    payload.price());
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .path("/api/products/{id}")
                            .buildAndExpand(product.getId())
                            .toUri())
                    .body(product);
        }


    }
}
