package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService service;

    @Mock
    private Model model;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateProductPage() {
        String viewName = productController.createProductPage(model);
        assertEquals("createProduct", viewName);
        verify(model, times(1)).addAttribute(eq("product"), any(Product.class));
    }

    @Test
    void testCreateProductPost() {
        Product product = new Product();
        product.setProductName("Test Product");
        product.setProductQuantity(10);

        String viewName = productController.createProductPost(product, model);
        assertEquals("redirect:list", viewName);
        verify(service, times(1)).create(product);
    }

    @Test
    void testProductListPage() {
        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setProductName("Test Product");
        productList.add(product);

        when(service.findAll()).thenReturn(productList);

        String viewName = productController.productListPage(model);
        assertEquals("productList", viewName);
        verify(model, times(1)).addAttribute("products", productList);
    }

    @Test
    void testEditProductPage() {
        Product product = new Product();
        product.setProductId("test-id");
        product.setProductName("Test Product");

        when(service.findById("test-id")).thenReturn(product);

        String viewName = productController.editProductPage("test-id", model);
        assertEquals("editProduct", viewName);
        verify(model, times(1)).addAttribute("product", product);
    }

    @Test
    void testEditProductPost() {
        Product product = new Product();
        product.setProductId("test-id");
        product.setProductName("Updated Product");

        String viewName = productController.editProductPost(product, model);
        assertEquals("redirect:list", viewName);
        verify(service, times(1)).edit(product);
    }

    @Test
    void testDeleteProduct() {
        String viewName = productController.deleteProduct("test-id");
        assertEquals("redirect:/product/list", viewName);
        verify(service, times(1)).delete("test-id");
    }
}
