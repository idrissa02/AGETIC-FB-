package com.AGETIC.Civil_service_competition.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AGETIC.Civil_service_competition.dto.ApplicationCreateRequest;
import com.AGETIC.Civil_service_competition.dto.ApplicationResponse;
import com.AGETIC.Civil_service_competition.dto.StatusResponse;
import com.AGETIC.Civil_service_competition.service.ApplicationService;

import ch.qos.logback.core.model.Model;
import jakarta.validation.Valid;

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
        service.validateAndCreateCandidate(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id, @RequestParam String reason) {
        service.reject(id, reason);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/applications/submit")
public String submitApplication(@ModelAttribute ApplicationCreateRequest req,
                                RedirectAttributes redirectAttributes) {
    ApplicationResponse response = service.create(req);

    // Pass data to the success page
    redirectAttributes.addFlashAttribute("application", response);

    // Redirect to success.html
    return "redirect:/applications/success";
}

@GetMapping("/applications/success")
public String showSuccessPage(Model model) {
    // Model will contain "application" from redirectAttributes
    return "success";  // → templates/success.html
}

}
