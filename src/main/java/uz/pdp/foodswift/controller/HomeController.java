package uz.pdp.foodswift.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uz.pdp.foodswift.model.dto.AddFoodDto;
import uz.pdp.foodswift.model.entity.enums.FoodCategory;
import uz.pdp.foodswift.service.AddFoodService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final AddFoodService addFoodService;

    @GetMapping("/")
    public String homePage(Model model) {
        List<AddFoodDto> foods = addFoodService.getAll();
        model.addAttribute("categories", FoodCategory.values());
        model.addAttribute("foods", foods);
        return "home/index";
    }
}
