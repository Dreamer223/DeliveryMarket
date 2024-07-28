package ru.dreamer.deliveryservice.service;

import ru.dreamer.deliveryservice.entity.Product;

import java.util.Optional;

public interface ProductService {

    Iterable<Product> findAll(String filter);
    Product save(String name, String category, String description, Double price);
    Optional<Product> findById(Long id);
    void deleteById(Long id);
    void update(Long id, String name, String category, String description, Double price);
}
