package ru.dreamer.deliveryservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.dreamer.deliveryservice.entity.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Iterable<Product> findAllByNameLikeIgnoreCase(String name);
}
