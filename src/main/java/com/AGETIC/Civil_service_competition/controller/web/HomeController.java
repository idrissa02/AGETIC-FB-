package com.AGETIC.Civil_service_competition.controller.web;



import com.AGETIC.Civil_service_competition.dto.ExamResponse;
import com.AGETIC.Civil_service_competition.service.ExamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ExamService examService;

    public HomeController(ExamService examService) {
        this.examService = examService;
    }

    /**
     * Home page – shows only exams whose applicationDeadline >= today.
     * Binds Page<ExamResponse> as "exams" for home.html.
     */
    @GetMapping({"/", "/index"})
    public String home(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<ExamResponse> open = examService.listOpen(pageable);
        model.addAttribute("exams", open);
        return "home";
    }

    /**
     * Simple landing to reuse the dossier view if user visits /mon-dossier directly.
     */
    @GetMapping("/mon-dossier")
    public String dossierLanding() {
        return "dossier";
    }

    /**
     * Simple landing to reuse the results view if user visits /resultats directly.
     */
    @GetMapping("/resultats")
    public String resultsLanding() {
        return "resultats";
    }
}
