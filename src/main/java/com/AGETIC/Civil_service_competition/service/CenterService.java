package com.AGETIC.Civil_service_competition.service;

import com.AGETIC.Civil_service_competition.dto.CenterCreateRequest;
import com.AGETIC.Civil_service_competition.dto.CenterResponse;
import java.util.List;

public interface CenterService {
    
    List<CenterResponse> getAllCenters();
    CenterResponse getCenterById(Long id);
    CenterResponse addCenter(CenterCreateRequest request);
    CenterResponse updateCenter(Long id, CenterCreateRequest request);
    void deleteCenter(Long id);
    List<CenterResponse> searchByName(String name);
    List<CenterResponse> searchByLocation(String location);
}