package com.AGETIC.Civil_service_competition.controller.web;

import com.AGETIC.Civil_service_competition.dto.CandidateProfileResponse;
import com.AGETIC.Civil_service_competition.service.CandidateService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.AGETIC.Civil_service_competition.repository.CandidateRepository;

/**
 * Thin MVC controller that calls your existing REST/service methods and renders
 * Thymeleaf views for dossier/resultats pages.
 */
@Controller
public class CandidateProfilePageController {

    private final CandidateService candidateService;
    private final CandidateRepository candidateRepo;

    public CandidateProfilePageController(CandidateService candidateService,CandidateRepository candidateRepo) {
        this.candidateService = candidateService;
        this.candidateRepo = candidateRepo;
    }

    /**
     * Dossier by applicationId – renders dossier.html with "profile" Uses your
     * existing service: candidateService.viewByApplicationId(applicationId)
     */
    @GetMapping("/applications/{applicationId}/profile")
    public String applicationProfile(@PathVariable Long applicationId, Model model) {
        CandidateProfileResponse res = candidateService.viewByApplicationId(applicationId);
        if (res != null) {
            model.addAttribute("profile", res);
        }
        // Reuse dossier.html to show the profile; it has safe empty states
        return "dossier";
    }

    /**
     * Dossier by candidateNumber (public) – only if results published (enforced
     * in service) Uses your existing service:
     * service.viewByCandidateNumber(candidateNumber)
     */
    @GetMapping("/{candidateNumber}/profile")
    public String publicCandidateProfile(@PathVariable String candidateNumber, Model model) {
        CandidateProfileResponse res = candidateService.viewByCandidateNumber(candidateNumber);
         if (res == null) {
            // Could be either "not found" OR "not yet published"
            boolean exists = candidateRepo.existsByCandidateNumber(candidateNumber);
            if (exists) {
                model.addAttribute("notPublished", true);
            } else {
                model.addAttribute("notFound", true);
            }
        } else {
            model.addAttribute("profile", res);
        }
        return "resultats"; // reuse the results.html template
    }

    //================================  Helpers  ==========================================

    /**
     * Small helper form handler from dossier.html (search by applicationId)
     * Redirects to the canonical /applications/{id}/profile route.
     */
    @GetMapping("/applications/search")
    public String searchByApplication(@RequestParam Long applicationId) {
        return "redirect:/applications/" + applicationId + "/profile";
    }

    /**
     * Small helper form handler from dossier.html (search by candidateNumber)
     * Redirects to the canonical /{candidateNumber}/profile route.
     */
    @GetMapping("/candidats/search")
    public String searchByCandidate(@RequestParam String candidateNumber) {
        return "redirect:/" + candidateNumber + "/profile";
    }

    /**
     * Results page posts/gets an applicationId and reuses the dossier fragment.
     * Route: /resultats?applicationId=...
     */
    // @GetMapping("/resultats/view")
    // public String showResults(@RequestParam Long applicationId, Model model) {
    //     CandidateProfileResponse res = candidateService.viewByApplicationId(applicationId);
    //     if (res != null) {
    //         model.addAttribute("profile", res);
    //     }
    //     return "resultats";
    // }

    /**
     * Optional: attestation download passthrough if you later expose a
     * generator endpoint. For now, this could 302 to a file or be implemented
     * later.
     */
    @GetMapping("/attestations/{applicationId}")
    public ResponseEntity<Void> downloadAttestation(@PathVariable Long applicationId) {
        // TODO: replace with your real implementation when backend is ready.
        return ResponseEntity.notFound().build();
    }
}
