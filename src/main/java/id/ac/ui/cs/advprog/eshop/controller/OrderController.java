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
        List<id.ac.ui.cs.advprog.eshop.model.Product> allProducts = productService.findAll();
        model.addAttribute("products", allProducts);
        return "CreateOrder";
    }

    @PostMapping("/create")
    public String createOrderPost(@RequestParam String author,
                                  @RequestParam List<String> productIds,
                                  Model model) {
        List<id.ac.ui.cs.advprog.eshop.model.Product> products = new java.util.ArrayList<>();
        for (String productId : productIds) {
            id.ac.ui.cs.advprog.eshop.model.Product product = productService.findById(productId);
            products.add(product);
        }

        id.ac.ui.cs.advprog.eshop.model.Order order = new id.ac.ui.cs.advprog.eshop.model.Order(
                null, products, System.currentTimeMillis(), author);
        orderService.createOrder(order);
        return "redirect:/order/history";
    }

    @GetMapping("/history")
    public String orderHistoryPage() {
        return "OrderHistory";
    }

    @PostMapping("/history")
    public String orderHistoryPost(@RequestParam String author, Model model) {
        List<id.ac.ui.cs.advprog.eshop.model.Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        return "OrderHistoryResult";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable String orderId, Model model) {
        id.ac.ui.cs.advprog.eshop.model.Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "PayOrder";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrderPost(@PathVariable String orderId,
                               @RequestParam String method,
                               @RequestParam Map<String, String> paymentData,
                               Model model) {
        id.ac.ui.cs.advprog.eshop.model.Order order = orderService.findById(orderId);
        id.ac.ui.cs.advprog.eshop.model.Payment payment = paymentService.addPayment(order, method, paymentData);
        model.addAttribute("payment", payment);
        return "PaymentResult";
    }
}
