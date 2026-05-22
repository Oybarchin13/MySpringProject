package uz.pdp.foodswift.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.foodswift.model.entity.Order;
import uz.pdp.foodswift.model.entity.enums.OrderStatus;
import uz.pdp.foodswift.service.OrderService;

import java.util.List;

@Controller
@RequestMapping("/arizalar")
@RequiredArgsConstructor
public class OrderAdminController {

    private final OrderService orderService;

    // 1. Kelib tushgan arizalar sahifasini ochish
    @GetMapping
    public String showApplications(Model model) {
        List<Order> applications = orderService.getNewApplications();
        model.addAttribute("orders", applications);

        // Agar applications.html fayli admin papkasida bo'lsa: "admin/applications" qiling
        return "applications";
    }

    // 2. Arizani qabul qilish (ID va Status servisga to'g'ri uzatildi)
    @PostMapping("/{id}/accept")
    public String acceptOrder(@PathVariable String id) {
        // orderId (id) va enum qiymati birga beriladi
        orderService.updateStatus(id, OrderStatus.ACCEPTED);

        // Agar enumingizda ACCEPTED bo'lsa, unda mana buni yozing:
        // orderService.updateStatus(id, OrderStatus.ACCEPTED);

        return "redirect:/arizalar";
    }
}