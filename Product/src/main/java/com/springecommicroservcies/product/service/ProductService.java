package com.springecommicroservcies.product.service;


import com.springecommicroservcies.product.dto.ProductRequest;
import com.springecommicroservcies.product.dto.ProductResponse;
import com.springecommicroservcies.product.model.Product;
import com.springecommicroservcies.product.repository.ProductRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse addProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product,productRequest);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }
    private ProductResponse mapToProductResponse(Product savedProduct) {
        ProductResponse response = new ProductResponse();
        response.setId(savedProduct.getId());
        response.setName(savedProduct.getName());
        response.setActive(savedProduct.getActive());
        response.setCategory(savedProduct.getCategory());
        response.setDescription(savedProduct.getDescription());
        response.setPrice(savedProduct.getPrice());
        response.setImageUrl(savedProduct.getImageUrl());
        response.setStockQuantity(savedProduct.getStockQuantity());
        return response;
    }

    private void updateProductFromRequest(Product product,ProductRequest productRequest){
        product.setName(productRequest.getName());
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStockQuantity(productRequest.getStockQuantity());
    }

    public List<ProductResponse> getProducts() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(this::mapToProductResponse)
                .toList();
    }


    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    updateProductFromRequest(existingProduct,productRequest);
                    Product savedProduct = productRepository.save(existingProduct);
                    return mapToProductResponse(savedProduct);
                });
    }

    public boolean deleteProduct(Long id) {
            return productRepository.findById(id)
                    .map(existingProduct -> {
                        existingProduct.setActive(false);
                        productRepository.save(existingProduct);
                        return true;
                    }).orElse(false);
    }

    public List<ProductResponse> searchProduct(String keyword) {
        return productRepository.searchProducts(keyword)
                .stream()
                .map(this::mapToProductResponse)
                .toList();
    }
}
