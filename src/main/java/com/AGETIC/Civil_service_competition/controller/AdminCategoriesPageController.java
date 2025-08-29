// src/main/java/com/AGETIC/Civil_service_competition/controller/AdminCategoriesPageController.java
package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.*;
import com.AGETIC.Civil_service_competition.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoriesPageController {

    private final CategoryService service;

    public AdminCategoriesPageController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public String listPage(Model model,
                           @ModelAttribute("flashSuccess") String flashSuccess,
                           @ModelAttribute("flashError") String flashError) {

        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new CategoryForm(null, ""));
        }
        model.addAttribute("categories", service.getAll());
        model.addAttribute("flashSuccess", flashSuccess);
        model.addAttribute("flashError", flashError);
        return "admin-categories";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("form") CategoryForm form,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.form", br);
            ra.addFlashAttribute("form", form);
            ra.addFlashAttribute("flashError", "Veuillez corriger les erreurs.");
            return "redirect:/admin/categories";
        }
        service.create(new CategoryCreateRequest(form.type()));
        ra.addFlashAttribute("flashSuccess", "Catégorie créée avec succès ✅");
        return "redirect:/admin/categories";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        var c = service.getById(id);
        model.addAttribute("form", new CategoryForm(c.id(), c.type()));
        model.addAttribute("categories", service.getAll());
        return "admin-categories";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") CategoryForm form,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.form", br);
            ra.addFlashAttribute("form", form);
            ra.addFlashAttribute("flashError", "Veuillez corriger les erreurs.");
            return "redirect:/admin/categories/" + id + "/edit";
        }
        service.update(id, new CategoryCreateRequest(form.type()));
        ra.addFlashAttribute("flashSuccess", "Catégorie mise à jour ✅");
        return "redirect:/admin/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("flashSuccess", "Catégorie supprimée ✅");
        return "redirect:/admin/categories";
    }
}
