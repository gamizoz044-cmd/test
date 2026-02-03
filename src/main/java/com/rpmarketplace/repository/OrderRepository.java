package com.rpmarketplace.repository;

import com.rpmarketplace.model.Order;
import com.rpmarketplace.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(User customer);
    List<Order> findDistinctByItemsProductSeller(User seller);
}
