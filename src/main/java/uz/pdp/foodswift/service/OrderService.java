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

        // 1. Savatdagi barcha narsalarni olamiz
        List<CartItem> cartItems = cartItemRepository.findAllByUserId(user.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Savatingiz bo'sh!");
        }

        // 2. Jami summani hisoblaymiz
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getFood().getPrice() * item.getQuantity())
                .sum();

        // 3. Yangi Order ochamiz (OrderStatus.YANGI biriktirilmoqda)
        Order order = Order.builder()
                .user(user)
                .totalAmount(BigDecimal.valueOf(totalAmount))
                .status(OrderStatus.NEW) // "PENDING" o'rniga enumdagi YANGI qiymati qo'yildi
                .createdAt(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        // 4. Savatdagilarni OrderItem ga aylantiramiz
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .food(cartItem.getFood())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getFood().getPrice())
                    .build();
            order.getItems().add(orderItem);
        }

        // 5. Buyurtmani saqlab, savatni tozalaymiz
        orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);
    }

    @Transactional(readOnly = true)
    public List<Order> getUserOrders(String phoneNumber) {
        AuthUsers user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));

        return orderRepository.findAllByUserIdWithItemsOrderByCreatedAtDesc(user.getId());
    }

    // 6. Admin arizani qabul qilganda yoki rad etganda statusni o'zgartirish metodi
    @Transactional
    public void updateStatus(String orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Buyurtma topilmadi! ID: " + orderId));
        order.setStatus(orderStatus);
        orderRepository.save(order); // Status yangilanadi
    }

    // 7. Admin paneldagi "Arizalar" sahifasiga faqat YANGI tushgan buyurtmalarni chiqarish
    @Transactional(readOnly = true)
    public List<Order> getNewApplications() {
        return orderRepository.findAllByStatusOrderByCreatedAtDesc(OrderStatus.NEW);
    }
}