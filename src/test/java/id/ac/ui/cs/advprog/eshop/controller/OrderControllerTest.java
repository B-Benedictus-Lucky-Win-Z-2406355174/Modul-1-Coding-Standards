package id.ac.ui.cs.advprog.eshop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @InjectMocks
    private OrderController orderController;

    private Order order;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("prod-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("order-123", products, 1708560000L, "Safira Sudrajat");
    }

    @Test
    void testCreateOrderPage() {
        when(productService.findAll()).thenReturn(products);
        String viewName = orderController.createOrderPage(model);
        assertEquals("CreateOrder", viewName);
        verify(model, times(1)).addAttribute("products", products);
    }

    @Test
    void testCreateOrderPost() {
        when(productService.findById("prod-1")).thenReturn(products.get(0));
        doReturn(order).when(orderService).createOrder(any(Order.class));

        String viewName = orderController.createOrderPost("Safira Sudrajat",
                List.of("prod-1"), model);
        assertEquals("redirect:/order/history", viewName);
        verify(orderService, times(1)).createOrder(any(Order.class));
    }

    @Test
    void testCreateOrderPostEmptyProducts() {
        String viewName = orderController.createOrderPost("Safira Sudrajat", null, model);
        assertEquals("redirect:/order/create", viewName);
    }

    @Test
    void testOrderHistoryPage() {
        String viewName = orderController.orderHistoryPage();
        assertEquals("OrderHistory", viewName);
    }

    @Test
    void testOrderHistoryPost() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderService.findAllByAuthor("Safira Sudrajat")).thenReturn(orders);

        String viewName = orderController.orderHistoryPost("Safira Sudrajat", model);
        assertEquals("OrderHistoryResult", viewName);
        verify(model, times(1)).addAttribute("orders", orders);
    }

    @Test
    void testPayOrderPage() {
        when(orderService.findById("order-123")).thenReturn(order);

        String viewName = orderController.payOrderPage("order-123", model);
        assertEquals("PayOrder", viewName);
        verify(model, times(1)).addAttribute("order", order);
    }

    @Test
    void testPayOrderPost() {
        when(orderService.findById("order-123")).thenReturn(order);
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(),
                order, paymentData);
        doReturn(payment).when(paymentService).addPayment(
                any(Order.class), anyString(), anyMap());

        String viewName = orderController.payOrderPost("order-123",
                PaymentMethod.VOUCHER_CODE.getValue(), paymentData, model);
        assertEquals("PaymentResult", viewName);
        verify(model, times(1)).addAttribute(eq("payment"), any(Payment.class));
    }
}
