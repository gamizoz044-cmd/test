package com.rpmarketplace.service;

import com.rpmarketplace.model.Order;
import com.rpmarketplace.model.OrderItem;
import com.rpmarketplace.model.Product;
import com.rpmarketplace.model.User;
import com.rpmarketplace.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public Order createOrder(User customer, List<OrderItemRequest> items) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus("PENDING");
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : items) {
            Product product = productService.getProduct(itemRequest.productId());
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            item.setPrice(product.getPrice());
            order.getItems().add(item);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
        }
        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    public List<Order> listCustomerOrders(User customer) {
        return orderRepository.findByCustomer(customer);
    }

    public List<Order> listSellerOrders(User seller) {
        return orderRepository.findDistinctByItemsProductSeller(seller);
    }

    public List<Order> listAllOrders() {
        return orderRepository.findAll();
    }

    public record OrderItemRequest(Long productId, int quantity) {
    }
}
