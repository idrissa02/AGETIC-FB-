package com.AGETIC.Civil_service_competition.web;

import com.AGETIC.Civil_service_competition.exception.AlreadyAppliedException;
import com.AGETIC.Civil_service_competition.model.Exam;
import com.AGETIC.Civil_service_competition.repository.ExamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class UiExceptionHandler {

    private final ExamRepository examRepository;

    public UiExceptionHandler(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @ExceptionHandler(AlreadyAppliedException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public String handleDuplicate(AlreadyAppliedException ex, Model model) {
        String examTitle = examRepository.findById(ex.getExamId())
                .map(Exam::getTitle)     // adapt to your field if different
                .orElse("Concours n°" + ex.getExamId());

        model.addAttribute("message", "Vous avez déjà postulé à ce concours.");
        model.addAttribute("ninaNumber", ex.getNinaNumber());
        model.addAttribute("examTitle", examTitle);
        model.addAttribute("examId", ex.getExamId());

        // Render a specific page
        return "apply-duplicate";
    }
}
