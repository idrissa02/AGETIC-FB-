
package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.CandidateProfileResponse;
import com.AGETIC.Civil_service_competition.service.CandidateService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
public class ApplicationProfileController {

    private final CandidateService candidateService;

    public ApplicationProfileController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    // Applicant view by applicationId:
    // - PENDING/REFUSED -> only infos + status
    // - ACCEPTED       -> + candidateNumber + exam infos (no grades)
    @GetMapping("/{applicationId}/profile")
    public ResponseEntity<CandidateProfileResponse> profile(@PathVariable Long applicationId) {
        CandidateProfileResponse res = candidateService.viewByApplicationId(applicationId);
        return res == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(res);
    }
}
