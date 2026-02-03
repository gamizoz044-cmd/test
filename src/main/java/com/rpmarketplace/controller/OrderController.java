package com.rpmarketplace.controller;

import com.rpmarketplace.model.Order;
import com.rpmarketplace.model.User;
import com.rpmarketplace.service.OrderService;
import com.rpmarketplace.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    public Order createOrder(@Valid @RequestBody OrderRequest request, Principal principal) {
        User customer = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return orderService.createOrder(customer, request.items());
    }

    @GetMapping
    public List<Order> listCustomerOrders(Principal principal) {
        User customer = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return orderService.listCustomerOrders(customer);
    }

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/seller")
    public List<Order> listSellerOrders(Principal principal) {
        User seller = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return orderService.listSellerOrders(seller);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public List<Order> listAllOrders() {
        return orderService.listAllOrders();
    }

    public record OrderRequest(List<OrderService.OrderItemRequest> items) {
    }
}
