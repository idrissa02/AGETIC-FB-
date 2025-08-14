
package com.AGETIC.Civil_service_competition.service.impl;

import com.AGETIC.Civil_service_competition.dto.CenterCreateRequest;
import com.AGETIC.Civil_service_competition.dto.CenterResponse;
import com.AGETIC.Civil_service_competition.model.Center;
import com.AGETIC.Civil_service_competition.repository.CenterRepository;
import com.AGETIC.Civil_service_competition.service.CenterService;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CenterServiceImpl implements CenterService {

    private final CenterRepository repo;

    public CenterServiceImpl(CenterRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<CenterResponse> getAllCenters() {
        return repo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public CenterResponse getCenterById(Long id) {
        Center c = repo.findById(id).orElseThrow(() -> new RuntimeException("Center not found"));
        return toResponse(c);
    }

    @Override
    public CenterResponse addCenter(CenterCreateRequest request) {
        String name = safe(request.name());
        if (repo.existsByNameIgnoreCase(name)) {
            throw new RuntimeException("Center name already exists");
        }
        Center c = new Center();
        c.setName(name);
        c.setLocation(safe(request.location()));
        repo.save(c);
        return toResponse(c);
    }

    @Override
    public CenterResponse updateCenter(Long id, CenterCreateRequest request) {
        Center c = repo.findById(id).orElseThrow(() -> new RuntimeException("Center not found"));

        if (request.name() != null) {
            String name = safe(request.name());
            if (!name.equalsIgnoreCase(c.getName()) && repo.existsByNameIgnoreCase(name)) {
                throw new RuntimeException("Center name already exists");
            }
            c.setName(name);
        }
        if (request.location() != null) {
            c.setLocation(safe(request.location()));
        }
        return toResponse(c);
    }

    @Override
    public void deleteCenter(Long id) {
        if (!repo.existsById(id)) throw new RuntimeException("Center not found");
        repo.deleteById(id);
    }

    @Override
    public List<CenterResponse> searchByName(String name) {
        return repo.findByNameContainingIgnoreCase(safe(name)).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<CenterResponse> searchByLocation(String location) {
        return repo.findByLocationContainingIgnoreCase(safe(location)).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    // ---- helpers ----
    private CenterResponse toResponse(Center c) {
        return new CenterResponse(c.getId(), c.getName(), c.getLocation());
    }

    private String safe(String s) { return s == null ? null : s.trim(); }
}
