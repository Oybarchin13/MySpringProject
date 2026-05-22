package uz.pdp.foodswift.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.pdp.foodswift.config.security.CustomUserDetails;
import uz.pdp.foodswift.model.entity.CartItem;
import uz.pdp.foodswift.service.CartService;

import java.util.List;

@Controller
@RequestMapping("")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String showCart(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/auth/login";
        }

        // Tizimga kirgan userning savatidagi barcha mahsulotlarni olamiz
        List<CartItem> cartItems = cartService.getCartItemsByPrice(userDetails.getUsername());
        model.addAttribute("cartItems", cartItems);

        // Savatdagi barcha mahsulotlarning umumiy summasini hisoblaymiz
        double totalSum = cartItems.stream()
                .mapToDouble(item -> item.getFood().getPrice() * item.getQuantity())
                .sum();
        model.addAttribute("totalSum", totalSum);

        return "clients/cart"; // templates/cart.html faylini qidiradi
    }

    // CartController.java ichiga qo'shimcha metod:

    @PostMapping("/cart/update")
    public String updateCartItemQuantity(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestParam("foodId") String foodId,
                                         @RequestParam("change") Integer change) { // +1 yoki -1 keladi
        if (userDetails == null) {
            return "redirect:/auth/login";
        }

        // Savatdagi miqdorni o'zgartirish xizmatini chaqiramiz
        cartService.updateItemQuantity(userDetails.getUsername(), foodId, change);

        return "redirect:/cart"; // Miqdor o'zgargach, savat sahifasini o'zini qayta yangilaymiz
    }

@PostMapping("/cart/add")
public String addToCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                        @RequestParam("foodId") String foodId, // String emas, UUID qilindi
                        @RequestParam("quantity") Integer quantity) {
    if (userDetails == null) {
        return "redirect:/auth/login";
    }

    cartService.addToCart(userDetails.getUsername(), foodId, quantity);
    return "redirect:/cart"; // Savatga muvaffaqiyatli qo'shgach, to'g'ri savat sahifasiga o'tsin
}
}