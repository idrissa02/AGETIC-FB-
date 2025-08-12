package com.AGETIC.Civil_service_competition.controller;

import java.io.IOException;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import com.AGETIC.Civil_service_competition.dto.*;
import com.AGETIC.Civil_service_competition.service.ApplicationService;

@RestController
@RequestMapping("/api/applications")

public class ApplicationController {

    private final ApplicationService service ;

    public ApplicationController(ApplicationService service) {
    this.service = service;
}


    @PostMapping
    public ResponseEntity<ApplicationResponse> create(@RequestBody @Valid ApplicationCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @PostMapping(value = "/{id}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> upload(@PathVariable Long id,
                                       @RequestPart(value = "picture", required = false) MultipartFile picture,
                                       @RequestPart(value = "documents", required = false) MultipartFile documents)
            throws IOException {
        service.uploadDocuments(id, picture, documents);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/status")
    public StatusResponse status(@PathVariable Long id) {
        return service.getStatus(id);
    }

    @GetMapping("/me")
    public Page<ApplicationResponse> myApplications(@RequestParam String ninaNumber, Pageable pageable) {
        return service.myApplications(ninaNumber, pageable);
    }




    // --- Admin ---

    @PatchMapping("/{id}/validate")
    public ResponseEntity<Void> validate(@PathVariable Long id) {
        service.validate(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id, @RequestParam String reason) {
        service.reject(id, reason);
        return ResponseEntity.ok().build();
    }
}
