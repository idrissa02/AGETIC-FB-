package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.*;
import com.AGETIC.Civil_service_competition.service.ExamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService service;

    public ExamController(ExamService service) {
        this.service = service;
    }

    // ----- Public -----

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    // Home page: only exams whose applicationDeadline >= today
    @GetMapping("/opening")
    public Page<ExamResponse> listOpen(Pageable pageable) {
        return service.listOpen(pageable);
    }

    @GetMapping("/by-category")
    public Page<ExamResponse> byCategory(@RequestParam Long categoryId, Pageable pageable) {
        return service.listByCategory(categoryId, pageable);
    }

    @GetMapping("/search")
    public Page<ExamResponse> search(@RequestParam String q, Pageable pageable) {
        return service.searchByTitle(q, pageable);
    }

    // ----- Admin ------

    @PostMapping
    public ResponseEntity<ExamResponse> create(@RequestBody ExamCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamResponse> update(@PathVariable Long id, @RequestBody ExamUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
