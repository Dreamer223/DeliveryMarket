package ru.dreamer.deliveryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dreamer.deliveryservice.entity.Product;
import ru.dreamer.deliveryservice.repository.ProductRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {
    private final ProductRepository productRepository;

    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void update(Long id, String name, String category, String description, Double price) {
        this.productRepository.findById(id)
                .ifPresentOrElse(product -> {
                    product.setName(name);
                    product.setCategory(category);
                    product.setDescription(description);
                    product.setPrice(price);
                }, () -> {
                    throw new NoSuchElementException();
                });
    }

    @Override
    public Product save(String name, String category, String description, Double price) {
        return productRepository.save(new Product(null, name, category, description, price));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
