package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.CenterCreateRequest;
import com.AGETIC.Civil_service_competition.dto.CenterResponse;
import com.AGETIC.Civil_service_competition.services.CenterService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/centers")
public class CenterController {
    private final CenterService centerService;

    public CenterController(CenterService centerService) {
        this.centerService = centerService;
    }

    @GetMapping
    public List<CenterResponse> getAllCenters() {
        return centerService.getAllCenters();
    }

    @GetMapping("/{id}")
    public CenterResponse getCenterById(@PathVariable Long id) {
        return centerService.getCenterById(id);
    }

    @PostMapping
    public CenterResponse addCenter(@RequestBody CenterCreateRequest request) {
        return centerService.addCenter(request);
    }

    @PutMapping("/{id}")
    public CenterResponse updateCenter(@PathVariable Long id, @RequestBody CenterCreateRequest request) {
        return centerService.updateCenter(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCenter(@PathVariable Long id) {
        centerService.deleteCenter(id);
    }

    @GetMapping("/search")
    public List<CenterResponse> searchCentersByName(@RequestParam String name) {
        return centerService.searchByName(name);
    }

    @GetMapping("/location")
    public List<CenterResponse> searchCentersByLocation(@RequestParam String location) {
        return centerService.searchByLocation(location);
    }
}