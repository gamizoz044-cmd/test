package com.rpmarketplace.service;

import com.rpmarketplace.model.Product;
import com.rpmarketplace.model.User;
import com.rpmarketplace.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listPublicProducts() {
        return productRepository.findByActiveTrue();
    }

    public List<Product> listAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
    }

    public Product createProduct(Product product, User seller) {
        product.setSeller(seller);
        return productRepository.save(product);
    }

    public List<Product> listSellerProducts(User seller) {
        return productRepository.findBySeller(seller);
    }

    public Product updateProduct(Long id, Product update, User seller) {
        Product product = getProduct(id);
        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new IllegalArgumentException("Acesso negado");
        }
        product.setName(update.getName());
        product.setDescription(update.getDescription());
        product.setPrice(update.getPrice());
        product.setImages(update.getImages());
        product.setActive(update.isActive());
        return productRepository.save(product);
    }
}
