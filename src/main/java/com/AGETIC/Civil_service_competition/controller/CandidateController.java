// src/main/java/com/AGETIC/Civil_service_competition/controller/CandidateController.java
package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.CandidateProfileResponse;
import com.AGETIC.Civil_service_competition.dto.CandidateResponse;
import com.AGETIC.Civil_service_competition.service.CandidateService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateService service;

    public CandidateController(CandidateService service) {
        this.service = service;
    }

    // Lightweight view (admin/tools)
    @GetMapping("/{candidateNumber}")
    public ResponseEntity<CandidateResponse> get(@PathVariable String candidateNumber) {
        CandidateResponse res = service.getByCandidateNumber(candidateNumber);
        return res == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(res);
    }

    // Public result view (only if results published – enforced in service)
    @GetMapping("/{candidateNumber}/profile")
    public ResponseEntity<CandidateProfileResponse> profile(@PathVariable String candidateNumber) {
        CandidateProfileResponse res = service.viewByCandidateNumber(candidateNumber);
        return res == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(res);
    }


    
    // Admin: set final status (only if you keep manual override)
//     @PatchMapping("/{candidateNumber}/status")
//     public ResponseEntity<CandidateResponse> setStatus(@PathVariable String candidateNumber,
//                                                        @RequestParam String status) {
//         CandidateResponse res = service.setStatus(candidateNumber, status);
//         return res == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(res);
//     }


}
