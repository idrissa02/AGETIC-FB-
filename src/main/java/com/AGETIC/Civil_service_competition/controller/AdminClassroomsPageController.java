// src/main/java/com/AGETIC/Civil_service_competition/controller/AdminClassroomsPageController.java
package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.*;
import com.AGETIC.Civil_service_competition.service.CenterService;
import com.AGETIC.Civil_service_competition.service.ClassroomService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/classrooms")
public class AdminClassroomsPageController {

  private final ClassroomService classroomService;
  private final CenterService centerService;

  public AdminClassroomsPageController(ClassroomService classroomService, CenterService centerService) {
    this.classroomService = classroomService;
    this.centerService = centerService;
  }

  @GetMapping
  public String listPage(Model model,
                         @ModelAttribute("flashSuccess") String flashSuccess,
                         @ModelAttribute("flashError") String flashError) {

    if (!model.containsAttribute("form")) {
      model.addAttribute("form", new ClassroomForm(null, "", 30, null));
    }
   model.addAttribute("actionUrl", "/admin/classrooms/create");

  model.addAttribute("centers", centerService.getAllCenters());
  model.addAttribute("classrooms", classroomService.getAllClassrooms());
  model.addAttribute("flashSuccess", flashSuccess);
  model.addAttribute("flashError", flashError);
  return "admin-classrooms";
  }

  @PostMapping("/create")
  public String create(@Valid @ModelAttribute("form") ClassroomForm form,
                       BindingResult br,
                       RedirectAttributes ra) {
    if (br.hasErrors()) {
      ra.addFlashAttribute("org.springframework.validation.BindingResult.form", br);
      ra.addFlashAttribute("form", form);
      ra.addFlashAttribute("flashError", "Veuillez corriger les erreurs du formulaire.");
      return "redirect:/admin/classrooms";
    }
    classroomService.addClassroom(new ClassroomCreateRequest(form.number(), form.capacity(), form.centerId()));
    ra.addFlashAttribute("flashSuccess", "Salle créée avec succès ✅");
    return "redirect:/admin/classrooms";
  }

  @GetMapping("/{id}/edit")
  public String edit(@PathVariable Long id, Model model) {
    var c = classroomService.getClassroomById(id);
     model.addAttribute("form", new ClassroomForm(c.id(), c.number(), c.capacity(), c.centerId()));

    model.addAttribute("actionUrl", "/admin/classrooms/" + id + "/update");

  model.addAttribute("centers", centerService.getAllCenters());
  model.addAttribute("classrooms", classroomService.getAllClassrooms());
  return "admin-classrooms";
  }

  @PostMapping("/{id}/update")
  public String update(@PathVariable Long id,
                       @Valid @ModelAttribute("form") ClassroomForm form,
                       BindingResult br,
                       RedirectAttributes ra) {
    if (br.hasErrors()) {
      ra.addFlashAttribute("org.springframework.validation.BindingResult.form", br);
      ra.addFlashAttribute("form", form);
      ra.addFlashAttribute("flashError", "Veuillez corriger les erreurs du formulaire.");
      return "redirect:/admin/classrooms/" + id + "/edit";
    }
    classroomService.updateClassroom(id, new ClassroomCreateRequest(form.number(), form.capacity(), form.centerId()));
    ra.addFlashAttribute("flashSuccess", "Salle mise à jour ✅");
    return "redirect:/admin/classrooms";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id, RedirectAttributes ra) {
    classroomService.deleteClassroom(id);
    ra.addFlashAttribute("flashSuccess", "Salle supprimée ✅");
    return "redirect:/admin/classrooms";
  }
}
