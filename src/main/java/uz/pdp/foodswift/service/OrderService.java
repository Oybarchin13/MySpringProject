package uz.pdp.foodswift.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.foodswift.model.entity.*;
import uz.pdp.foodswift.model.entity.enums.OrderStatus;
import uz.pdp.foodswift.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final AuthUserRepository userRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository, AuthUserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createOrderFromCart(String phoneNumber) {
        AuthUsers user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));

        List<CartItem> cartItems = cartItemRepository.findAllByUserId(user.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Savatingiz bo'sh!");
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getFood().getPrice() * item.getQuantity())
                .sum();

        Order order = Order.builder()
                .user(user)
                .totalAmount(BigDecimal.valueOf(totalAmount))
                .status(OrderStatus.NEW) // "PENDING" o'rniga enumdagi YANGI qiymati qo'yildi
                .createdAt(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .food(cartItem.getFood())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getFood().getPrice())
                    .build();
            order.getItems().add(orderItem);
        }

        orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);
    }

    @Transactional(readOnly = true)
    public List<Order> getUserOrders(String phoneNumber) {
        AuthUsers user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));

        return orderRepository.findAllByUserIdWithItemsOrderByCreatedAtDesc(user.getId());
    }

    @Transactional
    public void updateStatus(String orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Buyurtma topilmadi! ID: " + orderId));
        order.setStatus(orderStatus);
        orderRepository.save(order); // Status yangilanadi
    }

    @Transactional(readOnly = true)
    public List<Order> getNewApplications() {
        return orderRepository.findAllByStatusOrderByCreatedAtDesc(OrderStatus.NEW);
    }
}