package uz.pdp.foodswift.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.foodswift.model.dto.AddFoodDto;
import uz.pdp.foodswift.model.dto.AddFoodSaveDto;
import uz.pdp.foodswift.model.dto.AddFoodUpdateDto;
import uz.pdp.foodswift.model.entity.AddFood;
import uz.pdp.foodswift.model.entity.enums.FoodCategory;
import uz.pdp.foodswift.service.AddFoodService;

import java.util.List;

@Controller
@RequestMapping("/foods")
@RequiredArgsConstructor
public class AddFoodController {

    private final AddFoodService addFoodService;

    // Ro'yxatni ko'rish — FOOD_READ kerak
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('FOOD_READ')")
    public String getAll(Model model) {
        List<AddFoodDto> foods = addFoodService.getAll();
        model.addAttribute("categories", FoodCategory.values());
        model.addAttribute("foods", foods);
        return "foodList";
    }

    // Qo'shish formasi — FOOD_CREATE kerak
    @GetMapping("/add")
    @PreAuthorize("hasAuthority('FOOD_CREATE')")
    public String addFoodPage(Model model) {
        model.addAttribute("categories", FoodCategory.values());
        model.addAttribute("addFoodSaveDto", new AddFoodSaveDto());
        return "addFood";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('FOOD_CREATE')")
    public String addFood(@ModelAttribute AddFoodSaveDto dto) {
        addFoodService.create(dto);
        return "redirect:/foods/list";
    }

    // Tahrirlash — FOOD_CREATE yoki ADMIN roli
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('FOOD_CREATE') or hasRole('ADMIN')")
    public ModelAndView editFood(@PathVariable String id) {
        AddFood addFood = addFoodService.get(id);
        ModelAndView mav = new ModelAndView("food-edit");
        mav.addObject("addFood", addFood);
        return mav;
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('FOOD_CREATE') or hasRole('ADMIN')")
    public String update(@PathVariable String id,
                         @ModelAttribute AddFoodUpdateDto dto) {
        addFoodService.update(id, dto);
        return "redirect:/foods/list";
    }

    // O'chirish — FOOD_DELETE kerak
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('FOOD_DELETE')")
    public String delete(@PathVariable String id) {
        addFoodService.delete(id);
        return "redirect:/foods/list";
    }
}