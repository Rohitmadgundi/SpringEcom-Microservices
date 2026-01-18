package com.springecommicroservcies.order.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private String productId;
    private Integer quantity;
}