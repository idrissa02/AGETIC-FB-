package com.AGETIC.Civil_service_competition.service;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.AGETIC.Civil_service_competition.dto.ApplicationCreateRequest;
import com.AGETIC.Civil_service_competition.dto.ApplicationResponse;
import com.AGETIC.Civil_service_competition.dto.StatusResponse;

public interface ApplicationService {

    ApplicationResponse create(ApplicationCreateRequest req);

    void uploadDocuments(Long applicationId, MultipartFile picture, MultipartFile documents) throws IOException;

    StatusResponse getStatus(Long applicationId);

    Page<ApplicationResponse> myApplications(String ninaNumber, Pageable pageable);

    // Admin
    void validateAndCreateCandidate(Long applicationId);

    void reject(Long applicationId, String reason);
}
