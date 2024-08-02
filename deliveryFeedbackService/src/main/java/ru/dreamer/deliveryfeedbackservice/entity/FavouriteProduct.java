package ru.dreamer.deliveryfeedbackservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("favourite")
public class FavouriteProduct {

    @Id
    private UUID id;
    private Long productId;
    private String userId;

}
