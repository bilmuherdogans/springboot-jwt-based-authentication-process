package com.timeissecuritytime.service;

import com.timeissecuritytime.entity.Product;
import com.timeissecuritytime.entity.Sale;
import com.timeissecuritytime.repository.ProductRepository;
import com.timeissecuritytime.repository.SaleRepository;
import com.timeissecuritytime.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;

    @PreAuthorize("hasRole('USER')")
    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(Constants.PRODUCT_NOT_FOUND + id));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('USER')")
    public List<Sale> findSalesByProductId(Long productId) {
        return saleRepository.findByProductId(productId);
    }
}
