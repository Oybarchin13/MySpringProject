package uz.pdp.foodswift.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.foodswift.model.dto.AuthUserRegisterDto;
import uz.pdp.foodswift.service.AuthUserService;

@Controller
@RequestMapping("/auth")
public class AuthRegisterController {

    private final AuthUserService authUserService;

    public AuthRegisterController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }


    @GetMapping("/login")
    public ModelAndView loginPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("auth/login");
        return modelAndView;
    }
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userSaveDto", new AuthUserRegisterDto());
        return "auth/register";
    }
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userSaveDto") AuthUserRegisterDto dto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return "auth/register";
        }

        try {
            // Ma'lumotlarni saqlash uchun serviceni chaqiramiz
            authUserService.register(dto);
            return "redirect:/auth/login?success";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }

}
