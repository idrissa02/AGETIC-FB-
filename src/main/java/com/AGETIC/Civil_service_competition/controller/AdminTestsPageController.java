// src/main/java/com/AGETIC/Civil_service_competition/controller/AdminTestsPageController.java
package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.TestForm;
import com.AGETIC.Civil_service_competition.dto.TestRequest;
import com.AGETIC.Civil_service_competition.service.ExamService;
import com.AGETIC.Civil_service_competition.service.TestService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/tests")
public class AdminTestsPageController {

    private final TestService testService;
    private final ExamService examService;

    public AdminTestsPageController(TestService testService, ExamService examService) {
        this.testService = testService;
        this.examService = examService;
    }

    @GetMapping
    public String listPage(Model model,
                           @ModelAttribute("flashSuccess") String flashSuccess,
                           @ModelAttribute("flashError") String flashError) {

        // Default empty form (create mode) if none supplied via flash
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new TestForm(null, null, ""));
        }

        // Precompute action URL for CREATE mode
        model.addAttribute("actionUrl", "/admin/tests/create");

        // For selects & table
        model.addAttribute("exams", examService.getAllExams()); // change if your service method name differs
        model.addAttribute("tests", testService.getAllTests());

        // Flash messages
        model.addAttribute("flashSuccess", flashSuccess);
        model.addAttribute("flashError", flashError);

        return "admin-tests";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("form") TestForm form,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.form", br);
            ra.addFlashAttribute("form", form);
            ra.addFlashAttribute("flashError", "Veuillez corriger les erreurs.");
            return "redirect:/admin/tests";
        }
        // Basic server-side checks (optional but useful)
        if (form.examId() == null || form.title() == null || form.title().isBlank()) {
            ra.addFlashAttribute("form", form);
            ra.addFlashAttribute("flashError", "L’épreuve doit avoir un concours et un titre.");
            return "redirect:/admin/tests";
        }

        testService.addTest(new TestRequest(form.examId(), form.title().trim()));
        ra.addFlashAttribute("flashSuccess", "Épreuve créée avec succès ✅");
        return "redirect:/admin/tests";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        var t = testService.getTestById(id);

        // Fill the form with existing values
        model.addAttribute("form", new TestForm(t.id(), t.examId(), t.title()));

        // Precompute action URL for UPDATE mode
        model.addAttribute("actionUrl", "/admin/tests/" + id + "/update");

        // Refill selects & table
        model.addAttribute("exams", examService.getAllExams()); // change if needed
        model.addAttribute("tests", testService.getAllTests());

        return "admin-tests";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") TestForm form,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.form", br);
            ra.addFlashAttribute("form", form);
            ra.addFlashAttribute("flashError", "Veuillez corriger les erreurs.");
            return "redirect:/admin/tests/" + id + "/edit";
        }
        if (form.examId() == null || form.title() == null || form.title().isBlank()) {
            ra.addFlashAttribute("form", form);
            ra.addFlashAttribute("flashError", "L’épreuve doit avoir un concours et un titre.");
            return "redirect:/admin/tests/" + id + "/edit";
        }

        testService.updateTest(id, new TestRequest(form.examId(), form.title().trim()));
        ra.addFlashAttribute("flashSuccess", "Épreuve mise à jour ✅");
        return "redirect:/admin/tests";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        testService.deleteTest(id);
        ra.addFlashAttribute("flashSuccess", "Épreuve supprimée ✅");
        return "redirect:/admin/tests";
    }
}
