package com.rpmarketplace.repository;

import com.rpmarketplace.model.Product;
import com.rpmarketplace.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();
    List<Product> findBySeller(User seller);
}
