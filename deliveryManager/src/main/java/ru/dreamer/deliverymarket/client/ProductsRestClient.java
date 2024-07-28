package ru.dreamer.deliverymarket.client;

import ru.dreamer.deliverymarket.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductsRestClient {

    List<Product> findAllProducts(String filter);

    Product createProduct(String name, String category, String description, Double price);

    Optional<Product> findProduct(Long productId);

    void updateProduct(Long productId, String name, String category, String description, Double price);

    void deleteProduct(Long productId);
}
