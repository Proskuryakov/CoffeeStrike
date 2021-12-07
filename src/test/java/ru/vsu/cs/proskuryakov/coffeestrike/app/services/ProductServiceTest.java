package ru.vsu.cs.proskuryakov.coffeestrike.app.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.ProductItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.repository.ProductRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private SequenceService sequenceService;
    @InjectMocks
    private ProductService productService;

    @BeforeEach
    private void initService() {
        productService = new ProductService(productRepository, sequenceService);
    }

    @Test
    @DisplayName("Create product test")
    void createProductTest() {
        when(sequenceService.getNextProductId()).thenReturn("1");
        when(productRepository.insert(any(ProductItem.class))).then(returnsFirstArg());
        var product = new ProductItem("1", "молоко");

        var res = productService.create(product);
        assertNotNull(res);
        assertEquals(product, res);
    }

    @Test
    @DisplayName("Get all product test")
    void getProductsTest() {
        var product1 = new ProductItem("1", "молоко");
        var product2 = new ProductItem("2", "эспрессо");
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        var res = productService.getAll();
        assertEquals(2, res.size());
        assertIterableEquals(List.of(product1, product2), res);
    }

    @Test
    @DisplayName("Search product by name test")
    void searchProductByNameTest() {
        var product1 = new ProductItem("1", "молоко");
        var product2 = new ProductItem("2", "эспрессо");
        when(productRepository.findAllByNameContains("о")).thenReturn(List.of(product1, product2));

        var res = productService.search("о");
        assertEquals(2, res.size());
        assertIterableEquals(List.of(product1, product2), res);
    }

}