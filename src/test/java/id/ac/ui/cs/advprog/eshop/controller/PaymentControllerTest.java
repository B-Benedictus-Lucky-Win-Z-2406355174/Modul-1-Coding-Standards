package id.ac.ui.cs.advprog.eshop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private Model model;

    @InjectMocks
    private PaymentController paymentController;

    private Payment payment;
    private Order order;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("prod-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("order-123", products, 1708560000L, "Safira Sudrajat");

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        payment = new Payment("pay-001", PaymentMethod.VOUCHER_CODE.getValue(),
                order, paymentData);
    }

    @Test
    void testPaymentDetailForm() {
        String viewName = paymentController.paymentDetailForm();
        assertEquals("PaymentDetail", viewName);
    }

    @Test
    void testPaymentDetail() {
        when(paymentService.getPayment("pay-001")).thenReturn(payment);

        String viewName = paymentController.paymentDetail("pay-001", model);
        assertEquals("PaymentDetailResult", viewName);
        verify(model, times(1)).addAttribute("payment", payment);
    }

    @Test
    void testAdminPaymentListPage() {
        List<Payment> payments = new ArrayList<>();
        payments.add(payment);
        when(paymentService.getAllPayments()).thenReturn(payments);

        String viewName = paymentController.adminPaymentList(model);
        assertEquals("PaymentAdminList", viewName);
        verify(model, times(1)).addAttribute("payments", payments);
    }

    @Test
    void testAdminPaymentDetailPage() {
        when(paymentService.getPayment("pay-001")).thenReturn(payment);

        String viewName = paymentController.adminPaymentDetail("pay-001", model);
        assertEquals("PaymentAdminDetail", viewName);
        verify(model, times(1)).addAttribute("payment", payment);
    }

    @Test
    void testAdminSetStatus() {
        when(paymentService.getPayment("pay-001")).thenReturn(payment);

        String viewName = paymentController.adminSetStatus("pay-001",
                PaymentStatus.SUCCESS.getValue(), model);
        assertEquals("redirect:/payment/admin/list", viewName);
        verify(paymentService, times(1)).setStatus(payment,
                PaymentStatus.SUCCESS.getValue());
    }
}
