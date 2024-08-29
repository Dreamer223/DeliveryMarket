package ru.dreamer.deliveryclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;
import ru.dreamer.deliveryclient.client.WebClientFavouritesProductClient;
import ru.dreamer.deliveryclient.client.WebClientProductReviewsClient;
import ru.dreamer.deliveryclient.client.WebClientProductsClient;
import ru.dreamer.deliveryclient.client.WebClientShoppingCartClient;

@Configuration
public class ClientConfig {

    @Bean
    @Scope("prototype")
    public WebClient.Builder deliveryWebClientBuilder(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository
    ) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction filter =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository,
                        authorizedClientRepository);
        filter.setDefaultClientRegistrationId("keycloak");
        return WebClient.builder().
                filter(filter);
    }

    @Bean
    public WebClientProductsClient webClientProductsClient(
            @Value("${dreamer.delivery.service.uri:http://localhost:8081}") String catalogueBaseUri,
            WebClient.Builder deliveryWebClientBuilder
    ) {
        return new WebClientProductsClient(
                deliveryWebClientBuilder
                .baseUrl(catalogueBaseUri)
                .build());
    }
    @Bean
    public WebClientProductReviewsClient webClientProductReviewsClient(
            @Value("${feedback.service.uri:http://localhost:8084}") String feedbackBaseUri,
            WebClient.Builder deliveryWebClientBuilder,
            ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction
    ) {
        return new WebClientProductReviewsClient(
                deliveryWebClientBuilder
                        .baseUrl(feedbackBaseUri)
                        .filter(loadBalancerExchangeFilterFunction)
                        .build());
    }

    @Bean
    public WebClientFavouritesProductClient webClientFavouritesProductClient(
            @Value("${feedback.service.uri:http://localhost:8084}") String feedbackBaseUri,
            WebClient.Builder deliveryWebClientBuilder,
            ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction
    ) {
        return new WebClientFavouritesProductClient(
                deliveryWebClientBuilder
                        .baseUrl(feedbackBaseUri)
                        .filter(loadBalancerExchangeFilterFunction)
                        .build());
    }

    @Bean
    public WebClientShoppingCartClient webClientShoppingCartClient(
            @Value("${shoppingCart.service.uri:http://localhost:8085}") String shoppingCartBaseUri,
            WebClient.Builder deliveryWebClientBuilder
    ) {
        return new WebClientShoppingCartClient(
                deliveryWebClientBuilder
                        .baseUrl(shoppingCartBaseUri)
                        .build());
    }

}
