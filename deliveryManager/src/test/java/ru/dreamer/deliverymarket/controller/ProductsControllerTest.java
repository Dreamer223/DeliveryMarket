package ru.dreamer.deliverymarket.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ConcurrentModel;
import ru.dreamer.deliverymarket.client.BadRequestException;
import ru.dreamer.deliverymarket.client.ProductsRestClient;
import ru.dreamer.deliverymarket.controller.payload.NewProductPayload;
import ru.dreamer.deliverymarket.entity.Product;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

    @Mock
    ProductsRestClient productsRestClient;
    @InjectMocks
    ProductsController controller;

    @Test
    void getProductsList_ReturnsProductsListPage() {
        // given
        var model = new ConcurrentModel();
        var filter = "товар";

        var products = IntStream.range(1, 4)
                .mapToObj(i -> new Product((long) i, "Товар №%d".formatted(i),
                        "Категория #%d".formatted(i), "Описание товара №%d".formatted(i), 0.0))
                .toList();

        doReturn(products).when(this.productsRestClient).findAllProducts(filter);

        // when
        var result = this.controller.getProductsList(model, filter);

        // then
        assertEquals("catalogue/products/list", result);
        assertEquals(filter, model.getAttribute("filter"));
        assertEquals(products, model.getAttribute("products"));
    }

    @Test
    void getNewProductPage_ReturnsNewProductPage () {
        // given

        // when
        var result = this.controller.getNewProductPage();

        // then
        assertEquals("catalogue/products/new_product", result);
    }

    @Test
    @DisplayName("createProduct - создаст новый товар и вернет на страницу товара")
    void createProduct_RequestIsValid_ReturnsRedirectionToProductPage() {
        //given
        var payload = new NewProductPayload("test", "test", "test", 0.0);
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();

        doReturn(new Product(1L, "test", "test", "test", 0.0))
                .when(productsRestClient)
                .createProduct("test", "test", "test", 0.0);
        //when
        var result = this.controller.createProduct(payload, model,response);
        //then
        assertEquals("redirect:/catalogue/products/1", result);
        verify(productsRestClient).createProduct("test", "test", "test", 0.0);
        verifyNoMoreInteractions(this.productsRestClient);
    }

    @Test
    @DisplayName("createProduct вернёт страницу с ошибками, если запрос невалиден")
    void createProduct_RequestIsInvalid_ReturnsProductFormWithErrors() {
        // given
        var payload = new NewProductPayload("  ", "  ", "  ", 0.0);
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();

        doThrow(new BadRequestException(List.of("Ошибка 1", "Ошибка 2")))
                .when(this.productsRestClient)
                .createProduct("  ", "  ", "  ", 0.0);

        // when
        var result = this.controller.createProduct(payload, model, response);

        // then
        assertEquals("catalogue/products/new_product", result);
        assertEquals(payload, model.getAttribute("payload"));
        assertEquals(List.of("Ошибка 1", "Ошибка 2"), model.getAttribute("errors"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        verify(this.productsRestClient).createProduct("  ", "  ", "  ", 0.0);
        verifyNoMoreInteractions(this.productsRestClient);
    }
}