package com.springecommicroservcies.product.controller;


import com.springecommicroservcies.product.dto.ProductRequest;
import com.springecommicroservcies.product.dto.ProductResponse;
import com.springecommicroservcies.product.service.ProductService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
@Data
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestBody
                                                      ProductRequest productRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProduct(productRequest));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(){
        return ResponseEntity.ok(productService.getProducts());
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @RequestBody ProductRequest productRequest) {

        return productService.updateProduct(id,productRequest)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        boolean deleted = productService.deleteProduct(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }

    @GetMapping("search")
    public ResponseEntity<List<ProductResponse>> search(@RequestParam String keyword){
        return ResponseEntity.ok(productService.searchProduct(keyword));
    }
}
