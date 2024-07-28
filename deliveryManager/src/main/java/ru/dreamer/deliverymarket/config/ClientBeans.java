package ru.dreamer.deliverymarket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;
import ru.dreamer.deliverymarket.client.RestClientProductsRestClient;
import ru.dreamer.deliverymarket.security.OAuth2Client;

@Configuration
public class ClientBeans {
    @Bean
    public RestClientProductsRestClient productsRestClient(
            @Value("${dreamer.delivery.service.uri:http://localhost:8081}") String catalogueBaseUri,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository,
            @Value("${dreamer.delivery.service.client-id:keycloak}") String clientId) {
        return new RestClientProductsRestClient(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .requestInterceptor(new OAuth2Client(
                        new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,
                                authorizedClientRepository),clientId
                ))
                .build());
    }
}
