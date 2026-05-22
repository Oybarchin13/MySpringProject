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

    @GetMapping
    public String showApplications(Model model) {
        List<Order> applications = orderService.getNewApplications();
        model.addAttribute("orders", applications);

        return "applications";
    }

    @PostMapping("/{id}/accept")
    public String acceptOrder(@PathVariable String id) {
        orderService.updateStatus(id, OrderStatus.ACCEPTED);
        return "redirect:/arizalar";
    }
}