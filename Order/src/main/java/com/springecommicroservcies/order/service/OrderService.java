package com.springecommicroservcies.order.service;


import com.springecommicroservcies.order.dto.OrderItemDTO;
import com.springecommicroservcies.order.dto.OrderResponse;
import com.springecommicroservcies.order.model.CartItem;
import com.springecommicroservcies.order.model.Order;
import com.springecommicroservcies.order.model.OrderItem;
import com.springecommicroservcies.order.model.OrderStatus;
import com.springecommicroservcies.order.repository.OrderRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class OrderService {

    private final CartService cartService;

    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId){

//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if (userOptional.isEmpty())
//            return Optional.empty();

        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty())
            return Optional.empty();

//        User user = userOptional.get();

        BigDecimal totalPrice = cartItems.stream()
                .map(cartItem
                        -> cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(
                        null,
                        cartItem.getProductId(),
                        cartItem.getQuantity(),
                        cartItem.getPrice(),
                        order
                )).toList();

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))).toList(),
                order.getCreatedAt()

        );
    }

}
