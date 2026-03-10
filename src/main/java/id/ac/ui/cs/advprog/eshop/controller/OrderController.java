package id.ac.ui.cs.advprog.eshop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ProductService productService;

    @GetMapping("/create")
    public String createOrderPage(Model model) {
        return "";
    }

    @PostMapping("/create")
    public String createOrderPost(@RequestParam String author,
                                  @RequestParam List<String> productIds,
                                  Model model) {
        return "";
    }

    @GetMapping("/history")
    public String orderHistoryPage() {
        return "";
    }

    @PostMapping("/history")
    public String orderHistoryPost(@RequestParam String author, Model model) {
        return "";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable String orderId, Model model) {
        return "";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrderPost(@PathVariable String orderId,
                               @RequestParam String method,
                               @RequestParam Map<String, String> paymentData,
                               Model model) {
        return "";
    }
}
