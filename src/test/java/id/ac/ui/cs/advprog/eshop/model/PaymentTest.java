package id.ac.ui.cs.advprog.eshop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;

class PaymentTest {

    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("order-123", products, 1708560000L, "Safira Sudrajat");

        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testCreatePaymentDefaultStatus() {
        Payment payment = new Payment("pay-123", PaymentMethod.VOUCHER_CODE.getValue(),
                order, paymentData);
        assertEquals("pay-123", payment.getId());
        assertEquals(PaymentMethod.VOUCHER_CODE.getValue(), payment.getMethod());
        assertSame(order, payment.getOrder());
        assertSame(paymentData, payment.getPaymentData());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentWithStatus() {
        Payment payment = new Payment("pay-123", PaymentMethod.VOUCHER_CODE.getValue(),
                order, paymentData, PaymentStatus.SUCCESS.getValue());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentWithInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("pay-123", PaymentMethod.VOUCHER_CODE.getValue(),
                    order, paymentData, "MEOW");
        });
    }

    @Test
    void testSetStatusToSuccess() {
        Payment payment = new Payment("pay-123", PaymentMethod.VOUCHER_CODE.getValue(),
                order, paymentData);
        payment.setStatus(PaymentStatus.SUCCESS.getValue());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatus() {
        Payment payment = new Payment("pay-123", PaymentMethod.VOUCHER_CODE.getValue(),
                order, paymentData);
        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("MEOW"));
    }

    // Voucher Code sub-feature tests
    @Test
    void testVoucherCodeValid() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-123", PaymentMethod.VOUCHER_CODE.getValue(),
                order, paymentData);
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testVoucherCodeInvalidNotStartWithEshop() {
        paymentData.put("voucherCode", "XXXXX1234ABC5678");
        Payment payment = new Payment("pay-123", PaymentMethod.VOUCHER_CODE.getValue(),
                order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testVoucherCodeInvalidNotSixteenChars() {
        paymentData.put("voucherCode", "ESHOP1234ABC");
        Payment payment = new Payment("pay-123", PaymentMethod.VOUCHER_CODE.getValue(),
                order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testVoucherCodeInvalidNotEightNumerical() {
        paymentData.put("voucherCode", "ESHOPABCDEFGHIJK");
        Payment payment = new Payment("pay-123", PaymentMethod.VOUCHER_CODE.getValue(),
                order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    // Bank Transfer sub-feature tests
    @Test
    void testBankTransferValid() {
        Map<String, String> bankData = new HashMap<>();
        bankData.put("bankName", "BCA");
        bankData.put("referenceCode", "REF123456");
        Payment payment = new Payment("pay-456", PaymentMethod.BANK_TRANSFER.getValue(),
                order, bankData);
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testBankTransferEmptyBankName() {
        Map<String, String> bankData = new HashMap<>();
        bankData.put("bankName", "");
        bankData.put("referenceCode", "REF123456");
        Payment payment = new Payment("pay-456", PaymentMethod.BANK_TRANSFER.getValue(),
                order, bankData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testBankTransferNullBankName() {
        Map<String, String> bankData = new HashMap<>();
        bankData.put("bankName", null);
        bankData.put("referenceCode", "REF123456");
        Payment payment = new Payment("pay-456", PaymentMethod.BANK_TRANSFER.getValue(),
                order, bankData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testBankTransferEmptyReferenceCode() {
        Map<String, String> bankData = new HashMap<>();
        bankData.put("bankName", "BCA");
        bankData.put("referenceCode", "");
        Payment payment = new Payment("pay-456", PaymentMethod.BANK_TRANSFER.getValue(),
                order, bankData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testBankTransferNullReferenceCode() {
        Map<String, String> bankData = new HashMap<>();
        bankData.put("bankName", "BCA");
        bankData.put("referenceCode", null);
        Payment payment = new Payment("pay-456", PaymentMethod.BANK_TRANSFER.getValue(),
                order, bankData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }
}
