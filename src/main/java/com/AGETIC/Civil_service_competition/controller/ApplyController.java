package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.ApplicationCreateRequest;
import com.AGETIC.Civil_service_competition.dto.ApplicationResponse;
import com.AGETIC.Civil_service_competition.model.Exam;
import com.AGETIC.Civil_service_competition.repository.ExamRepository;
import com.AGETIC.Civil_service_competition.service.ApplicationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/apply")
public class ApplyController {

    private final ApplicationService applicationService;
    private final ExamRepository examRepository;

    public ApplyController(ApplicationService applicationService, ExamRepository examRepository) {
        this.applicationService = applicationService;
        this.examRepository = examRepository;
    }

    /** Show the form (GET /apply?examId=42) */
    @GetMapping
    public String showForm(@RequestParam Long examId, Model model) {
        model.addAttribute("examId", examId);
        String examTitle = examRepository.findById(examId)
                .map(this::formatExamTitle)  // use real title/label if available
                .orElse("Concours n°" + examId);
        model.addAttribute("examTitle", examTitle);
        return "apply";
    }

    /** Handle submit, save to DB, upload files, then redirect to success */
    @PostMapping("/submit")
    public String submit(@ModelAttribute ApplicationCreateRequest req,
                         @RequestParam(value = "picture", required = false) MultipartFile picture,
                         @RequestParam(value = "documents", required = false) MultipartFile documents, Model model) throws Exception {
try{
    ApplicationResponse res = applicationService.create(req);              // save application
        if ((picture != null && !picture.isEmpty()) || (documents != null && !documents.isEmpty())) {
            applicationService.uploadDocuments(res.id(), picture, documents);
        }
        return "redirect:/apply/success/" + res.id();
    } catch (com.AGETIC.Civil_service_competition.exception.AlreadyAppliedException ex) {
    // build the same model your error page expects
    String examTitle = examRepository.findById(ex.getExamId())
        .map(Exam::getTitle)
        .orElse("Concours n°" + ex.getExamId());

    model.addAttribute("message", "Vous avez déjà postulé à ce concours.");
    model.addAttribute("ninaNumber", ex.getNinaNumber());
    model.addAttribute("examTitle", examTitle);
    model.addAttribute("examId", ex.getExamId());
    return "apply-duplicate"; // templates/apply-duplicate.html
  }
       
    }

    /** Success page: load application from DB + load its exam from DB */
    @GetMapping("/success/{id}")
    public String success(@PathVariable Long id, Model model) {
        ApplicationResponse a = applicationService.get(id);          // from DB

        String examTitle = examRepository.findById(a.examId())    // real exam applied
                .map(this::formatExamTitle)
                .orElse("Concours n°" + a.examId());

        model.addAttribute("application", a); 
          model.addAttribute("applicationId", a.id());
           model.addAttribute("name", a.name());
    model.addAttribute("surname", a.surname());                        // exposes name, surname, id, etc.
        model.addAttribute("examTitle", examTitle);
          model.addAttribute("examTitle", "Concours n°" + a.examId());
        model.addAttribute("applicantNumber",
                "APP-" + java.time.Year.now() + "-" + String.format("%06d", a.id()));

        return "success";
    }

    /* Helper to format how you want the exam to appear on the UI */
    private String formatExamTitle(Exam e) {
        // Adapt to your Exam fields. Examples:
        // return e.getTitle();
        // return e.getCode() + " — " + e.getTitle();
        // return e.getMinistry().getName() + " / " + e.getTitle();
        return e.getTitle();  // <- change if your field is different (e.g., getLabel() / getName())
    }
}

