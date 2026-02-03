package com.rpmarketplace.controller;

import com.rpmarketplace.model.Product;
import com.rpmarketplace.model.Role;
import com.rpmarketplace.model.User;
import com.rpmarketplace.service.CommunityService;
import com.rpmarketplace.service.ProductService;
import com.rpmarketplace.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;
    private final ProductService productService;
    private final CommunityService communityService;

    public AdminController(UserService userService, ProductService productService, CommunityService communityService) {
        this.userService = userService;
        this.productService = productService;
        this.communityService = communityService;
    }

    @GetMapping("/users")
    public List<User> listUsers() {
        return userService.listUsers();
    }

    @PutMapping("/users/{id}/roles")
    public User updateRoles(@PathVariable Long id, @Valid @RequestBody RoleUpdateRequest request) {
        return userService.updateRoles(id, request.roles());
    }

    @GetMapping("/products")
    public List<Product> listProducts() {
        return productService.listAllProducts();
    }

    @PutMapping("/products/{id}/deactivate")
    public Product deactivateProduct(@PathVariable Long id) {
        Product product = productService.getProduct(id);
        product.setActive(false);
        return productService.updateProduct(id, product, product.getSeller());
    }

    @DeleteMapping("/community/posts/{postId}")
    public void deletePost(@PathVariable Long postId) {
        communityService.deletePost(postId);
    }

    @DeleteMapping("/community/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        communityService.deleteComment(commentId);
    }

    public record RoleUpdateRequest(List<Role> roles) {
    }
}
