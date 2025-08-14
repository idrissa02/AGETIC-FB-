package com.AGETIC.Civil_service_competition.service.impl;

import com.AGETIC.Civil_service_competition.dto.CategoryCreateRequest;
import com.AGETIC.Civil_service_competition.dto.CategoryResponse;
import com.AGETIC.Civil_service_competition.model.Category;
import com.AGETIC.Civil_service_competition.repository.CategoryRepository;
import com.AGETIC.Civil_service_competition.service.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;

    public CategoryServiceImpl(CategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<CategoryResponse> getAll() {
        return repo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getById(Long id) {
        return repo.findById(id).map(this::toResponse).orElse(null);
    }

    @Override
    public CategoryResponse create(CategoryCreateRequest req) {
        String type = safe(req.type());
        if (type == null || type.isBlank()) return null;
        if (repo.existsByTypeIgnoreCase(type)) return null;

        Category c = new Category();
        c.setType(type);
        repo.save(c);
        return toResponse(c);
    }

    @Override
    public CategoryResponse update(Long id, CategoryCreateRequest req) {
        return repo.findById(id).map(c -> {
            String t = safe(req.type());
            if (t != null && !t.isBlank()) {
                if (!t.equalsIgnoreCase(c.getType()) && repo.existsByTypeIgnoreCase(t)) return null;
                c.setType(t);
            }
            return toResponse(c);
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<CategoryResponse> searchByName(String q) {
        return repo.findByTypeContainingIgnoreCase(safe(q))
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // helpers
    private CategoryResponse toResponse(Category c) {
        return new CategoryResponse(c.getId(), c.getType());
    }

    private String safe(String s) {
        return s == null ? null : s.trim();
    }
}
