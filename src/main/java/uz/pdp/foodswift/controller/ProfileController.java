package uz.pdp.foodswift.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.pdp.foodswift.config.security.CustomUserDetails;
import uz.pdp.foodswift.service.ProfileService;

@Controller
public class ProfileController {

    private final ProfileService profileService;

    // Konstruktor orqali ProfileService ni ulaymiz
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/auth/login";
        }

        // CustomUserDetails ichidagi ma'lumotlarni modelga yuklaymiz
        model.addAttribute("fullName", userDetails.getFullName());
        model.addAttribute("phoneNumber", userDetails.getUsername());
        return "clients/profile"; // profile.html sahifasini ochadi
    }

    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @RequestParam String fullName,
                                @RequestParam String phoneNumber,
                                @RequestParam(required = false) String newPassword,
                                RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/auth/login";
        }

        try {
            // Service qatlamiga joriy telefon (identifikator) va yangi ma'lumotlarni yuboramiz
            profileService.updateProfile(userDetails.getUsername(), fullName, phoneNumber, newPassword);

            redirectAttributes.addFlashAttribute("successMessage", "Profil ma'lumotlari muvaffaqiyatli yangilandi!");
        } catch (Exception e) {
            // Xavfsiz xatolik xabarini sahifaga qaytaramiz (masalan, telefon raqam band bo'lsa)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/profile";
    }
}