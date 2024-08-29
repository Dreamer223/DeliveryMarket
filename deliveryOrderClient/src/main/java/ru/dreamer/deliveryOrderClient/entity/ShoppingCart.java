package ru.dreamer.deliveryOrderClient.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("shoppingCart")
public class ShoppingCart {

    @Id
    private UUID id;
    private String userId;
    private List<Long> productId;
    private Double price;
}
