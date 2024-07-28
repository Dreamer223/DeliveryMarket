package ru.dreamer.deliverymarket.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import ru.dreamer.deliverymarket.client.ProductsRestClient;
import ru.dreamer.deliverymarket.controller.payload.NewProductPayload;
import ru.dreamer.deliverymarket.entity.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

    @Mock
    ProductsRestClient productsRestClient;
    @InjectMocks
    ProductsController productsController;

    @Test
    @DisplayName("createProduct - создаст новый товар и вернет на страницу товара")
    void createProduct_RequestIsValid_ReturnsRedirectionToProductPage() {
        //given
        var payload = new NewProductPayload("test", "test", "test", 0.0);
        var model = new ConcurrentModel();

        doReturn(new Product(1L, "test", "test", "test", 0.0))
                .when(productsRestClient)
                .createProduct("test", "test", "test", 0.0);
        //when
        var result = this.productsController.createProduct(payload, model);
        //then
        assertEquals("redirect:/catalogue/products/1", result);
        verify(productsRestClient).createProduct("test", "test", "test", 0.0);
        verifyNoMoreInteractions(this.productsRestClient);
    }
}