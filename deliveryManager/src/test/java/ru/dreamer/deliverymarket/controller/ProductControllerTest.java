package ru.dreamer.deliverymarket.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ConcurrentModel;
import ru.dreamer.deliverymarket.client.BadRequestException;
import ru.dreamer.deliverymarket.client.ProductsRestClient;
import ru.dreamer.deliverymarket.controller.payload.UpdateProductPayload;
import ru.dreamer.deliverymarket.entity.Product;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    ProductsRestClient productsRestClient;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    ProductController controller;

    @Test
    void product_ProductExists_ReturnsProduct() {
        // given
        var product = new Product(1L, "Товар №1", "Категория", "Описание товара №1", 100.0);

        doReturn(Optional.of(product)).when(this.productsRestClient).findProduct(1L);

        // when
        var result = this.controller.product(1L);

        // then
        assertEquals(product, result);

        verify(this.productsRestClient).findProduct(1L);
        verifyNoMoreInteractions(this.productsRestClient);
    }

    @Test
    void product_ProductDoesNotExist_ThrowsNoSuchElementException() {
        // given

        // when
        var exception = assertThrows(NoSuchElementException.class, () -> this.controller.product(1L));

        // then
        assertEquals("catalogue.errors.product.not_found", exception.getMessage());

        verify(this.productsRestClient).findProduct(1L);
        verifyNoMoreInteractions(this.productsRestClient);
    }

    @Test
    void getProduct_ReturnsProductPage() {
        // given

        // when
        var result = this.controller.getProduct();

        // then
        assertEquals("catalogue/products/product", result);

        verifyNoInteractions(this.productsRestClient);
    }

    @Test
    void getProductEditPage_ReturnsProductEditPage() {
        // given

        // when
        var result = this.controller.getProductEditPage();

        // then
        assertEquals("catalogue/products/edit", result);

        verifyNoInteractions(this.productsRestClient);
    }

    @Test
    void updateProduct_RequestIsValid_RedirectsToProductPage() {
        // given
        var product = new Product(1L, "Товар №1", "Категория", "Описание товара №1", 100.0);
        var payload = new UpdateProductPayload("Новое название","Новое категория", "Новое описание", 200.0);
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();

        // when
        var result = this.controller.updateProduct(product, payload, model, response);

        // then
        assertEquals("redirect:/catalogue/products/1", result);

        verify(this.productsRestClient).updateProduct(1L, "Новое название","Новое категория",
                "Новое описание", 200.0);
        verifyNoMoreInteractions(this.productsRestClient);
    }

    @Test
    void updateProduct_RequestIsInvalid_ReturnsProductEditPage() {
        // given
        var product = new Product(1L, "Товар №1", "Категория", "Описание товара №1", 100.0);
        var payload = new UpdateProductPayload("   ", null, null, 0.0);
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();

        doThrow(new BadRequestException(List.of("Ошибка 1", "Ошибка 2")))
                .when(this.productsRestClient).updateProduct(1L, "   ", null,null, 0.0);

        // when
        var result = this.controller.updateProduct(product, payload, model, response);

        // then
        assertEquals("catalogue/products/edit", result);
        assertEquals(payload, model.getAttribute("payload"));
        assertEquals(List.of("Ошибка 1", "Ошибка 2"), model.getAttribute("errors"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        verify(this.productsRestClient).updateProduct(1L, "   ", null, null, 0.0);
        verifyNoMoreInteractions(this.productsRestClient);
    }

    @Test
    void deleteProduct_RedirectsToProductsListPage() {
        // given
        var product = new Product(1L, "Товар №1", "Категория", "Описание товара №1", 100.0);

        // when
        var result = this.controller.deleteProduct(product);

        // then
        assertEquals("redirect:/catalogue/products", result);

        verify(this.productsRestClient).deleteProduct(1L);
        verifyNoMoreInteractions(this.productsRestClient);
    }

    @Test
    void handleNoSuchElementException_Returns404ErrorPage() {
        // given
        var exception = new NoSuchElementException("error");
        var model = new ConcurrentModel();
        var response = new MockHttpServletResponse();
        var locale = Locale.of("ru");

        doReturn("Ошибка").when(this.messageSource)
                .getMessage("error", new Object[0], "error", Locale.of("ru"));

        // when
        var result = this.controller.handleNoSuchElementException(exception, model, response, locale);

        // then
        assertEquals("errors/404",  result);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

        verify(this.messageSource).getMessage("error", new Object[0], "error", Locale.of("ru"));
        verifyNoMoreInteractions(this.messageSource);
        verifyNoInteractions(this.productsRestClient);
    }

}
