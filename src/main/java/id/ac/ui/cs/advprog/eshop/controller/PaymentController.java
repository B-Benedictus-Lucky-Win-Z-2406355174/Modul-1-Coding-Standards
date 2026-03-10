package id.ac.ui.cs.advprog.eshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import id.ac.ui.cs.advprog.eshop.service.PaymentService;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/admin/list")
    public String adminPaymentList(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "PaymentAdminList";
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String adminPaymentDetail(@PathVariable String paymentId, Model model) {
        model.addAttribute("payment", paymentService.getPayment(paymentId));
        return "PaymentAdminDetail";
    }
}
