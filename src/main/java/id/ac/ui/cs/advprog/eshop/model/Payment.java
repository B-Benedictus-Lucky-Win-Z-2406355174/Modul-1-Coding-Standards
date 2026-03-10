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

    private static final String REJECTED = PaymentStatus.REJECTED.getValue();
    private static final String SUCCESS = PaymentStatus.SUCCESS.getValue();

    private String determineStatus(String method, Map<String, String> paymentData) {
        if (PaymentMethod.VOUCHER_CODE.getValue().equals(method)) {
            return validateVoucherCode(paymentData);
        } else if (PaymentMethod.BANK_TRANSFER.getValue().equals(method)) {
            return validateBankTransfer(paymentData);
        }
        return REJECTED;
    }

    private String validateVoucherCode(Map<String, String> data) {
        String code = data.get("voucherCode");
        if (code == null || code.length() != 16 || !code.startsWith("ESHOP")) {
            return REJECTED;
        }
        long digitCount = code.chars().filter(Character::isDigit).count();
        return digitCount == 8 ? SUCCESS : REJECTED;
    }

    private String validateBankTransfer(Map<String, String> data) {
        String bankName = data.get("bankName");
        String referenceCode = data.get("referenceCode");
        if (bankName == null || bankName.isEmpty()
                || referenceCode == null || referenceCode.isEmpty()) {
            return REJECTED;
        }
        return SUCCESS;
    }
}
