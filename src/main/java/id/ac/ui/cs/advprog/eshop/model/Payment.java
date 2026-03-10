package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;

@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;
    Order order;

    public Payment(String id, String method, Order order, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.order = order;
        this.paymentData = paymentData;
        this.status = determineStatus(method, paymentData);
    }

    public Payment(String id, String method, Order order, Map<String, String> paymentData, String status) {
        this.id = id;
        this.method = method;
        this.order = order;
        this.paymentData = paymentData;
        this.setStatus(status);
    }

    public void setStatus(String status) {
        if (PaymentStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private String determineStatus(String method, Map<String, String> paymentData) {
        if (PaymentMethod.VOUCHER_CODE.getValue().equals(method)) {
            return validateVoucherCode(paymentData);
        } else if (PaymentMethod.BANK_TRANSFER.getValue().equals(method)) {
            return validateBankTransfer(paymentData);
        }
        return PaymentStatus.REJECTED.getValue();
    }

    private String validateVoucherCode(Map<String, String> data) {
        String code = data.get("voucherCode");
        if (code == null) {
            return PaymentStatus.REJECTED.getValue();
        }
        if (code.length() != 16) {
            return PaymentStatus.REJECTED.getValue();
        }
        if (!code.startsWith("ESHOP")) {
            return PaymentStatus.REJECTED.getValue();
        }
        long digitCount = code.chars().filter(Character::isDigit).count();
        if (digitCount != 8) {
            return PaymentStatus.REJECTED.getValue();
        }
        return PaymentStatus.SUCCESS.getValue();
    }

    private String validateBankTransfer(Map<String, String> data) {
        String bankName = data.get("bankName");
        String referenceCode = data.get("referenceCode");
        if (bankName == null || bankName.isEmpty()) {
            return PaymentStatus.REJECTED.getValue();
        }
        if (referenceCode == null || referenceCode.isEmpty()) {
            return PaymentStatus.REJECTED.getValue();
        }
        return PaymentStatus.SUCCESS.getValue();
    }
}
