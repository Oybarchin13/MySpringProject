package uz.pdp.foodswift.controller;

import lombok.RequiredArgsConstructor;
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

    @GetMapping("/add")
    public String addFoodPage(Model model) {
        model.addAttribute("categories", FoodCategory.values());
        model.addAttribute("addFoodSaveDto", new AddFoodSaveDto());
        return "addFood";
    }



    @GetMapping("/list")
    public String getAll(Model model) {
        List<AddFoodDto> foods = addFoodService.getAll();
        model.addAttribute("categories", FoodCategory.values());
        model.addAttribute("foods", foods);
        return "foodList";
    }
    @GetMapping("/edit/{id}")
    public ModelAndView editFood(@PathVariable(name = "id") String id){
        AddFood addFood = addFoodService.get(id);
//        List<CategoryDto> category = categoryService.getAll();
        ModelAndView modelAndView = new ModelAndView("food-edit");
        modelAndView.addObject("addFood",addFood);
//        modelAndView.addObject("category", category);
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id){
        addFoodService.delete(id);
        return "redirect:/foods/list";
    }
    @PostMapping("/edit/{id}")
    public String update ( @PathVariable(name = "id") String id, @ModelAttribute AddFoodUpdateDto dto){
        addFoodService.update(id, dto);
        return "redirect:/foods/list";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute AddFoodSaveDto dto) {
        addFoodService.create(dto);
        return "redirect:/foods/list";
    }

}