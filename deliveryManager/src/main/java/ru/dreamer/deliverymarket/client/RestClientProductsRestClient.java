package ru.dreamer.deliverymarket.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.dreamer.deliverymarket.controller.payload.NewProductPayload;
import ru.dreamer.deliverymarket.controller.payload.UpdateProductPayload;
import ru.dreamer.deliverymarket.entity.Product;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class RestClientProductsRestClient implements ProductsRestClient {

    private static final ParameterizedTypeReference<List<Product>> PRODUCTS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;
    @Override
    public List<Product> findAllProducts(String filter) {
        return this.restClient
                .get()
                .uri("/api/products?filter={filter}", filter)
                .retrieve()
                .body(PRODUCTS_TYPE_REFERENCE);
    }

    @Override
    public Product createProduct(String name, String category, String description, Double price) {
        try {
            return this.restClient
                    .post()
                    .uri("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new NewProductPayload(name, category, description, price))
                    .retrieve()
                    .body(Product.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public Optional<Product> findProduct(Long productId) {
        try {
            return Optional.ofNullable(this.restClient.get()
                    .uri("/api/products/{productId}", productId)
                    .retrieve()
                    .body(Product.class));
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public void updateProduct(Long productId, String name, String category, String description, Double price) {
        try {
            this.restClient
                    .post()
                    .uri("/api/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UpdateProductPayload(name, category, description, price))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }

    }

    @Override
    public void deleteProduct(Long productId) {
        try {
            this.restClient
                    .delete()
                    .uri("/api/products/{productId}", productId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new NoSuchElementException(exception);
        }
    }
}
