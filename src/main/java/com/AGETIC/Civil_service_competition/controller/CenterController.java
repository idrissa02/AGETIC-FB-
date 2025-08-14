// src/main/java/com/AGETIC/Civil_service_competition/controller/CenterController.java
package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.CenterCreateRequest;
import com.AGETIC.Civil_service_competition.dto.CenterResponse;
import com.AGETIC.Civil_service_competition.service.CenterService;

import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/centers")
public class CenterController {

    private final CenterService service;

    public CenterController(CenterService service) {
        this.service = service;
    }

    // List all
    @GetMapping
    public List<CenterResponse> getAll() {
        return service.getAllCenters();
    }

    // Get by id
    @GetMapping("/{id}")
    public CenterResponse getById(@PathVariable Long id) {
        return service.getCenterById(id);
    }

    // Create
    @PostMapping
    public ResponseEntity<CenterResponse> create(@RequestBody CenterCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addCenter(req));
    }

    // Update
    @PutMapping("/{id}")
    public CenterResponse update(@PathVariable Long id, @RequestBody CenterCreateRequest req) {
        return service.updateCenter(id, req);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCenter(id);
        return ResponseEntity.noContent().build();
    }

    // Search by name
    @GetMapping("/search/name")
    public List<CenterResponse> searchByName(@RequestParam String q) {
        return service.searchByName(q);
    }

    // Search by location
    @GetMapping("/search/location")
    public List<CenterResponse> searchByLocation(@RequestParam String q) {
        return service.searchByLocation(q);
    }
}
