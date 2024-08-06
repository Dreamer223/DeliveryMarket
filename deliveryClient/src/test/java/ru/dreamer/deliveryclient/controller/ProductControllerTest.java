package ru.dreamer.deliveryclient.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.dreamer.deliveryclient.client.FavouritesProductClient;
import ru.dreamer.deliveryclient.client.ProductReviewsClient;
import ru.dreamer.deliveryclient.client.ProductsClient;
import ru.dreamer.deliveryclient.entity.Product;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @Mock
    ProductsClient productsClient;
    @Mock
    FavouritesProductClient favouritesProductClient;
    @Mock
    ProductReviewsClient productReviewsClient;
    @InjectMocks
    ProductController controller;

    @Test
    void loadProduct_ProductExists_ReturnsNotEmptyMono() {
        //given
        var product = new Product(1L, "test", "test", "test", 0.0);
        doReturn(Mono.just(product)).when(productsClient).findProduct(1L);
        //when
        StepVerifier.create(controller.loadProduct(1L))
                //then
                .expectNext(new Product(1L, "test", "test", "test", 0.0))
                .expectComplete()
                .verify();

        verify(this.productsClient).findProduct(1L);
        verifyNoMoreInteractions(this.productsClient);
        verifyNoInteractions(this.favouritesProductClient, this.productReviewsClient);
    }
    @Test
    @DisplayName("Исключение NoSuchElementException - возвращает страницу ошибки 404")
    void handleNoSuchElementException_ReturnsErrors404() {
        //give
        var exception = new NoSuchElementException("Product not found");
        var model = new ConcurrentModel();
        //when
        var result = controller.handleNoSuchElementException(exception, model);
        //then
        assertEquals("errors/404", result);
        assertEquals("Product not found", model.getAttribute("error"));
    }

}