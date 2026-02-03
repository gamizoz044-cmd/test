package com.rpmarketplace.controller;

import com.rpmarketplace.model.Product;
import com.rpmarketplace.model.User;
import com.rpmarketplace.service.ProductService;
import com.rpmarketplace.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/public")
    public List<Product> listPublic() {
        return productService.listPublicProducts();
    }

    @GetMapping("/public/{id}")
    public Product getPublic(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping
    public Product createProduct(@Valid @RequestBody Product product, Principal principal) {
        User seller = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return productService.createProduct(product, seller);
    }

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/seller")
    public List<Product> listSellerProducts(Principal principal) {
        User seller = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return productService.listSellerProducts(seller);
    }

    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @Valid @RequestBody Product product, Principal principal) {
        User seller = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return productService.updateProduct(id, product, seller);
    }
}
