package ru.dreamer.deliveryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dreamer.deliveryservice.entity.Product;
import ru.dreamer.deliveryservice.repository.ProductRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultProductService implements ProductService {
    private final ProductRepository productRepository;

    public Iterable<Product> findAll(String filter) {
        if (filter != null && !filter.isEmpty()) {
            return productRepository.findAllByNameLikeIgnoreCase("%"+filter+"%");
        }else {
            return productRepository.findAll();
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional
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
    @Transactional
    public Product save(String name, String category, String description, Double price) {
        return productRepository.save(new Product(null, name, category, description, price));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
