package uz.pdp.foodswift.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.pdp.foodswift.model.entity.*;
import uz.pdp.foodswift.model.entity.enums.OrderStatus;
import uz.pdp.foodswift.repository.*;
import uz.pdp.foodswift.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final AuthUserRepository userRepository;
    private final OrderService orderService; // "Mening buyurtmalarim" sahifasi uchun

    // 1. Savatdagi yashil tugma bosilganda buyurtma yaratish (Checkout)
    @PostMapping("/cart/checkout")
    public String checkout(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "Toshkent shahri") String address,
            RedirectAttributes redirectAttributes
    ) {
        if (userDetails == null) {
            return "redirect:/auth/login";
        }

        // 1. Tizimga kirgan mijozni aniqlaymiz
        AuthUsers user = userRepository.findByPhoneNumber(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));

        // 2. Mijozning savatidagi mahsulotlarni o'qiymiz
        List<CartItem> cartItems = cartItemRepository.findAllByUserId(user.getId());

        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Savatingiz bo'sh!");
            return "redirect:/cart";
        }

        // 3. Yangi Ariza (Order) obyekti shakllantiramiz
        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(address);
        order.setContactPhone(user.getPhoneNumber()); // Telefon raqami bog'lanadi
        order.setStatus(OrderStatus.NEW);          // Tizimga YANGI ariza bo'lib tushadi
        order.setCreatedAt(LocalDateTime.now());

        // 4. Savat elementlarini buyurtma tarkibiga ko'chirib, summani hisoblaymiz
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cart : cartItems) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setFood(cart.getFood());
            item.setQuantity(cart.getQuantity());

            // AGAR taom narxi Double bo'lsa, uni BigDecimal.valueOf() orqali o'giramiz
            Double priceDouble = cart.getFood().getPrice();
            item.setPrice(priceDouble); // OrderItem ichidagi setPrice (Double yoki BigDecimal ga qarab)

            // Hisob-kitobni aniq qilish uchun BigDecimal ga o'girib ko'paytiramiz
            BigDecimal foodPriceAmount = BigDecimal.valueOf(priceDouble);
            BigDecimal itemTotal = foodPriceAmount.multiply(BigDecimal.valueOf(cart.getQuantity()));
            total = total.add(itemTotal);

            orderItems.add(item);
        }

        order.setItems(orderItems);
        order.setTotalAmount(total); // Order ichidagi totalAmount (BigDecimal)

        // 5. Arizani bazaga saqlash va Savatni o'chirish
        orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);

        redirectAttributes.addFlashAttribute("successMessage", "Buyurtmangiz muvaffaqiyatli rasmiylashtirildi!");

        // Arizani topshirgach, mijozni o'zining "Mening buyurtmalarim" sahifasiga yo'naltiramiz
        return "redirect:/orders";
    }

    // 2. Foydalanuvchining o'z buyurtmalari ro'yxati (Mening buyurtmalarim sahifasi)
    @GetMapping("/orders")
    public String showMyOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/auth/login";
        }

        // Telefon raqami orqali mijozning arizalarini servisdan olamiz
        List<Order> orders = orderService.getUserOrders(userDetails.getUsername());
        model.addAttribute("orders", orders);

        return "clients/orders"; // templates/clients/orders.html sahifasi
    }
}