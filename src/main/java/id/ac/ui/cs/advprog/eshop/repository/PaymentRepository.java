package id.ac.ui.cs.advprog.eshop.repository;

import java.util.ArrayList;
import java.util.List;

import id.ac.ui.cs.advprog.eshop.model.Payment;

public class PaymentRepository {
    private List<Payment> paymentData = new ArrayList<>();

    public Payment save(Payment payment) {
        return payment;
    }

    public Payment findById(String id) {
        return null;
    }

    public List<Payment> findAll() {
        return new ArrayList<>();
    }
}
