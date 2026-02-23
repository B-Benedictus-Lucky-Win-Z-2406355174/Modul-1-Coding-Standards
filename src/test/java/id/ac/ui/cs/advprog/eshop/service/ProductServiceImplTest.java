package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        product.setProductName("Test Product");
        product.setProductQuantity(10);

        when(productRepository.create(product)).thenReturn(product);

        Product result = productService.create(product);
        assertEquals(product, result);
        verify(productRepository, times(1)).create(product);
    }

    @Test
    void testFindAll() {
        List<Product> productList = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductName("Product 1");
        productList.add(product1);

        Product product2 = new Product();
        product2.setProductName("Product 2");
        productList.add(product2);

        Iterator<Product> iterator = productList.iterator();
        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> result = productService.findAll();
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getProductName());
        assertEquals("Product 2", result.get(1).getProductName());
    }

    @Test
    void testFindById() {
        Product product = new Product();
        product.setProductId("test-id");
        product.setProductName("Test Product");

        when(productRepository.findById("test-id")).thenReturn(product);

        Product result = productService.findById("test-id");
        assertNotNull(result);
        assertEquals("test-id", result.getProductId());
    }

    @Test
    void testEditProduct() {
        Product product = new Product();
        product.setProductId("test-id");
        product.setProductName("Updated Product");

        when(productRepository.edit(product)).thenReturn(product);

        Product result = productService.edit(product);
        assertNotNull(result);
        assertEquals("Updated Product", result.getProductName());
        verify(productRepository, times(1)).edit(product);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).delete("test-id");

        productService.delete("test-id");
        verify(productRepository, times(1)).delete("test-id");
    }
}
