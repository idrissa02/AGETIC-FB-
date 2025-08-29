
package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.ExamCreateRequest;
import com.AGETIC.Civil_service_competition.dto.ExamResponse;
import com.AGETIC.Civil_service_competition.service.CategoryService;
import com.AGETIC.Civil_service_competition.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/exams")
public class AdminExamsPageController {

    private final ExamService examService;
    private final CategoryService categoryService;

    public AdminExamsPageController(ExamService examService, CategoryService categoryService) {
        this.examService = examService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listPage(Model model,
                           @ModelAttribute("flashSuccess") String flashSuccess,
                           @ModelAttribute("flashError") String flashError) {
        if (!model.containsAttribute("form")) {
            // default empty form
            model.addAttribute("form", new ExamForm(
                    null, "", null, null, "", null, null, null
            ));
        }
        List<ExamResponse> exams = examService.getAllExams(); // make sure this exists in your service
        model.addAttribute("exams", exams);
        model.addAttribute("categories", categoryService.getAll()); // for the select
        model.addAttribute("flashSuccess", flashSuccess);
        model.addAttribute("flashError", flashError);
        // Which URL should the form POST to when creating:
        model.addAttribute("actionUrl", "/admin/exams/create");
        return "admin-exams";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("form") ExamForm form,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.form", br);
            ra.addFlashAttribute("form", form);
            ra.addFlashAttribute("flashError", "Veuillez corriger les erreurs du formulaire.");
            return "redirect:/admin/exams";
        }
        // Map form -> DTO used by service
        ExamCreateRequest req = new ExamCreateRequest(
                form.title(),
                form.date(),
                form.applicationDeadline(),
                form.condition(),
                form.quota(),
                form.hours(),
                form.categoryId(),
                form.createdByAdminId()
        );
        examService.create(req);
        ra.addFlashAttribute("flashSuccess", "Concours créé avec succès ✅");
        return "redirect:/admin/exams";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        ExamResponse e = examService.get(id);
        model.addAttribute("form", new ExamForm(
                e.id(), e.title(), e.date(), e.applicationDeadline(),
                "", e.quota(), e.hours(), e.categoryId()
        ));
        model.addAttribute("exams", examService.getAllExams());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("actionUrl", "/admin/exams/" + id + "/update");
        return "admin-exams";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") ExamForm form,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.form", br);
            ra.addFlashAttribute("form", form);
            ra.addFlashAttribute("flashError", "Veuillez corriger les erreurs du formulaire.");
            return "redirect:/admin/exams/" + id + "/edit";
        }
        examService.update(id, new com.AGETIC.Civil_service_competition.dto.ExamUpdateRequest(
                form.title(), form.date(), form.applicationDeadline(), form.condition(),
                form.quota(), form.hours(), form.categoryId()
        ));
        ra.addFlashAttribute("flashSuccess", "Concours mis à jour ✅");
        return "redirect:/admin/exams";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        examService.delete(id);
        ra.addFlashAttribute("flashSuccess", "Concours supprimé ✅");
        return "redirect:/admin/exams";
    }

    // Simple form class bound to the Thymeleaf form (separate from your API DTO)
    public record ExamForm(
            Long id,
            String title,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate applicationDeadline,
            String condition,
            Integer quota,
            Integer hours,
            Long categoryId
            // NOTE: we’ll set createdByAdminId server-side below to avoid null problem
    ) {
        public Long createdByAdminId() {
            // If you have authentication, read current admin id here.
            // For now, return a fixed admin id or null if your service ignores it.
            return 1L; // <- change if needed
        }
    }
}
