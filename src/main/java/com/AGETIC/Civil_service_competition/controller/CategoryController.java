package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.CategoryCreateRequest;
import com.AGETIC.Civil_service_competition.dto.CategoryResponse;
import com.AGETIC.Civil_service_competition.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoryResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        CategoryResponse resp = service.getById(id);
        return resp != null ? ResponseEntity.ok(resp) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody CategoryCreateRequest req) {
        CategoryResponse created = service.create(req);
        return created != null ? ResponseEntity.status(HttpStatus.CREATED).body(created)
                               : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @RequestBody CategoryCreateRequest req) {
        CategoryResponse updated = service.update(id, req);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<CategoryResponse> search(@RequestParam String q) {
        return service.searchByName(q);
    }
}
