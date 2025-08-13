package com.AGETIC.Civil_service_competition.services;

import com.AGETIC.Civil_service_competition.dto.CenterCreateRequest;
import com.AGETIC.Civil_service_competition.dto.CenterResponse;
import com.AGETIC.Civil_service_competition.model.Center;
import com.AGETIC.Civil_service_competition.repositories.CenterRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CenterServiceImpl implements CenterService {
    private final CenterRepository centerRepository;

    public CenterServiceImpl(CenterRepository centerRepository) {
        this.centerRepository = centerRepository;
    }

    private CenterResponse toResponse(Center center) {
        if (center == null) return null;
        CenterResponse response = new CenterResponse();
        response.setId(center.getId());
        response.setName(center.getName());
        response.setLocation(center.getLocation());
        return response;
    }

    @Override
    public List<CenterResponse> getAllCenters() {
        return centerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CenterResponse getCenterById(Long id) {
        return toResponse(centerRepository.findById(id).orElse(null));
    }

    @Override
    public CenterResponse addCenter(CenterCreateRequest request) {
        Center center = new Center();
        center.setName(request.getName());
        center.setLocation(request.getLocation());
        return toResponse(centerRepository.save(center));
    }

    @Override
    public CenterResponse updateCenter(Long id, CenterCreateRequest request) {
        Center center = centerRepository.findById(id).orElse(null);
        if (center != null) {
            center.setName(request.getName());
            center.setLocation(request.getLocation());
            return toResponse(centerRepository.save(center));
        }
        return null;
    }

    @Override
    public void deleteCenter(Long id) {
        centerRepository.deleteById(id);
    }

    @Override
    public List<CenterResponse> searchByName(String name) {
        return centerRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CenterResponse> searchByLocation(String location) {
        return centerRepository.findByLocationContainingIgnoreCase(location)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}