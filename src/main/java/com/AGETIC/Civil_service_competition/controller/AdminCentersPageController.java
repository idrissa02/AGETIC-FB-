// src/main/java/com/AGETIC/Civil_service_competition/controller/AdminCentersPageController.java
package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.CenterCreateRequest;
import com.AGETIC.Civil_service_competition.dto.CenterResponse;
import com.AGETIC.Civil_service_competition.service.CenterService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/centers")
public class AdminCentersPageController {

    private final CenterService service;

    public AdminCentersPageController(CenterService service) {
        this.service = service;
    }

   @GetMapping
  public String listPage(Model model,
                         @ModelAttribute("flashSuccess") String ok,
                         @ModelAttribute("flashError") String err) {
    if (!model.containsAttribute("form")) {
      model.addAttribute("form", new CenterForm(null, "", ""));
    }
    List<CenterResponse> centers = service.getAllCenters();
    model.addAttribute("centers", centers);
    model.addAttribute("flashSuccess", ok);
    model.addAttribute("flashError", err);
    return "admin-centers";
  }

     @PostMapping("/create")
  public String create(@Valid @ModelAttribute("form") CenterForm form,
                       BindingResult br,
                       RedirectAttributes ra) {
    if (br.hasErrors()) {
      ra.addFlashAttribute("org.springframework.validation.BindingResult.form", br);
      ra.addFlashAttribute("form", form);
      ra.addFlashAttribute("flashError", "Veuillez corriger les erreurs du formulaire.");
      return "redirect:/admin/centers";
    }
    service.addCenter(new CenterCreateRequest(form.name(), form.location()));
    ra.addFlashAttribute("flashSuccess", "Centre créé avec succès ✅");
    return "redirect:/admin/centers";
  }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        CenterResponse c = service.getCenterById(id);

        model.addAttribute("form", new CenterForm(c.id(), c.name(), c.location()));
        model.addAttribute("centers", service.getAllCenters());
        model.addAttribute("actionUrl", "/admin/centers/" + id + "/update");
        return "admin-centers";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") CenterForm form,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.form", br);
            ra.addFlashAttribute("form", form);
            ra.addFlashAttribute("flashError", "Veuillez corriger les erreurs du formulaire.");
            return "redirect:/admin/centers/" + id + "/edit";
        }

        service.updateCenter(id, new CenterCreateRequest(form.name(), form.location()));
        ra.addFlashAttribute("flashSuccess", "Centre mis à jour ✅");
        return "redirect:/admin/centers";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.deleteCenter(id);
        ra.addFlashAttribute("flashSuccess", "Centre supprimé ✅");
        return "redirect:/admin/centers";
    }

    // Simple form bean for Thymeleaf binding
    public record CenterForm(Long id, String name, String location) {}
}

