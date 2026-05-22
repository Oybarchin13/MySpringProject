package uz.pdp.foodswift.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.pdp.foodswift.criteria.BaseCriteria;
import uz.pdp.foodswift.model.dto.*;
import uz.pdp.foodswift.model.entity.enums.FoodCategory;
import uz.pdp.foodswift.service.AuthUserService;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class AuthUserController {

    private final AuthUserService authUserService;
    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "")   String search,
            Model model
    ) {
        BaseCriteria criteria = new BaseCriteria();
        criteria.setPage(page);
        criteria.setSize(size);
        criteria.setSearch(search);

        DataList<List<AuthUserDto>> result = authUserService.getAll(criteria);

        model.addAttribute("users",       result.getData());
        model.addAttribute("totalItems",  result.getAllElements());
        model.addAttribute("totalPages",  result.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("size",        size);
        model.addAttribute("search",      search);

        return "users/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("userSaveDto", new AuthUserSaveDto());
        return "users/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute AuthUserSaveDto dto, RedirectAttributes redirectAttributes) {
        try {
            AuthUserDto created = authUserService.create(dto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Foydalanuvchi muvaffaqiyatli yaratildi: " + created.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/create";
        }
        return "redirect:/users";
    }


    @GetMapping("/{id}/edit")
    public ModelAndView editForm(@PathVariable(name = "id") String id) {
        AuthUserDto userDto = authUserService.get(id);

        AuthUserSaveDto saveDto = new AuthUserSaveDto();
        saveDto.setFullName(userDto.getFullName());
        saveDto.setPhoneNumber(userDto.getPhoneNumber());
        saveDto.setPassword("");
        if (userDto.getRoleName() != null) {
            saveDto.setRoleName(userDto.getRoleName());
        }

        ModelAndView modelAndView = new ModelAndView("users/edit");
        modelAndView.addObject("userSaveDto", saveDto); // HTML formaga mos obyekt ismi
        return modelAndView;
    }

    @PostMapping("/{id}/edit")
    public String editForm(
            @PathVariable(name = "id") String id,
            @ModelAttribute AuthUserSaveDto dto,
            RedirectAttributes redirectAttributes
    ) {
        try {
            authUserService.update(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Foydalanuvchi muvaffaqiyatli yangilandi.");
            return "redirect:/users";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable String id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            authUserService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Foydalanuvchi o'chirildi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }
}