package ru.dreamer.deliveryclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.dreamer.deliveryclient.client.WebClientFavouritesProductClient;
import ru.dreamer.deliveryclient.client.WebClientProductReviewsClient;
import ru.dreamer.deliveryclient.client.WebClientProductsClient;

@Configuration
public class ClientConfig {


    @Bean
    public WebClientProductsClient webClientProductsClient(
            @Value("${dreamer.delivery.service.uri:http://localhost:8081}") String catalogueBaseUri
    ) {
        return new WebClientProductsClient(
                WebClient.builder()
                .baseUrl(catalogueBaseUri)
                .build());
    }
    @Bean
    public WebClientProductReviewsClient webClientProductReviewsClient(
            @Value("${dreamer.delivery.service.feedback.uri:http://localhost:8084}") String feedbackBaseUri
    ) {
        return new WebClientProductReviewsClient(
                WebClient.builder()
                        .baseUrl(feedbackBaseUri)
                        .build());
    }

    @Bean
    public WebClientFavouritesProductClient webClientFavouritesProductClient(
            @Value("${dreamer.delivery.service.feedback.uri:http://localhost:8084}") String feedbackBaseUri
    ) {
        return new WebClientFavouritesProductClient(
                WebClient.builder()
                        .baseUrl(feedbackBaseUri)
                        .build());
    }
}
