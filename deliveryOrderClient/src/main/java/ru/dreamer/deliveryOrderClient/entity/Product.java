package ru.dreamer.deliveryOrderClient.entity;

public record Product(Long id,
                      String name,
                      String category,
                      String description,
                      Double price) {
}
