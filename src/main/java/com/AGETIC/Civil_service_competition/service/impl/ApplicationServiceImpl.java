package com.AGETIC.Civil_service_competition.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import com.AGETIC.Civil_service_competition.dto.ApplicationCreateRequest;
import com.AGETIC.Civil_service_competition.dto.ApplicationResponse;
import com.AGETIC.Civil_service_competition.dto.StatusResponse;
import com.AGETIC.Civil_service_competition.Enum.ApplicationStatus;
import com.AGETIC.Civil_service_competition.model.Application;
import com.AGETIC.Civil_service_competition.model.Exam;
import com.AGETIC.Civil_service_competition.repository.ApplicationRepository;
import com.AGETIC.Civil_service_competition.repository.ExamRepository;
import com.AGETIC.Civil_service_competition.service.ApplicationService;
import com.AGETIC.Civil_service_competition.service.FileStorageService;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ExamRepository examRepository;
    private final FileStorageService storage;

    //constructor

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
            ExamRepository examRepository,
            FileStorageService storage) {
        this.applicationRepository = applicationRepository;
        this.examRepository = examRepository;
        this.storage = storage;
    }

   
    @Override
    public ApplicationResponse create(ApplicationCreateRequest req) {
        Exam exam = examRepository.findById(req.examId())
                .orElseThrow(() -> new RuntimeException("Exam not found"));  //check if the exam exist 

        if (applicationRepository.existsByNinaNumberAndExamId(req.ninaNumber(), req.examId())) { // prevent from 2xApplication
            throw new RuntimeException("Already applied to this exam");
        }

        Application a = new Application();
        a.setExam(exam);
        a.setNinaNumber(req.ninaNumber());
        a.setName(req.name());
        a.setSurname(req.surname());
        a.setPhone(req.phone());
        a.setEmail(req.email());
        a.setBirthDate(req.birthDate());
        a.setStatus(ApplicationStatus.PENDING);
        a.setCreatedAt(LocalDateTime.now());

        applicationRepository.save(a);
        return toResponse(a);
    }

    @Override
    public void uploadDocuments(Long applicationId, MultipartFile picture, MultipartFile documents) throws IOException {
        Application a = byId(applicationId); //find the corresponding app
        String base = "applications/" + a.getNinaNumber() + "/" + a.getExam().getId();

        if (picture != null && !picture.isEmpty()) {
            a.setPictureUrl(storage.store(picture, base + "/picture"));
        }
        if (documents != null && !documents.isEmpty()) {
            a.setDocumentsUrl(storage.store(documents, base + "/documents"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public StatusResponse getStatus(Long applicationId) {
        Application a = byId(applicationId);
        return new StatusResponse(a.getId(), a.getStatus().name());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationResponse> myApplications(String ninaNumber, Pageable pageable) {
        return applicationRepository.findAllByNinaNumber(ninaNumber, pageable)
                .map(this::toResponse);
    }

    @Override
    public void validate(Long applicationId) {
        Application a = byId(applicationId);
        a.setStatus(ApplicationStatus.ACCEPTED);
    }

    @Override
    public void reject(Long applicationId, String reason) {
        if (reason == null || reason.isBlank()) {
            throw new RuntimeException("Rejection reason is required");
        }
        Application a = byId(applicationId);
        a.setStatus(ApplicationStatus.REFUSED);
    }

    // Helpers
    private Application byId(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    private ApplicationResponse toResponse(Application a) {
        return new ApplicationResponse(
                a.getId(),
                a.getExam().getId(),
                a.getNinaNumber(),
                a.getName(),
                a.getSurname(),
                a.getPhone(),
                a.getEmail(),
                a.getStatus().name(),
                a.getBirthDate(),
                a.getPictureUrl(),
                a.getDocumentsUrl(),
                a.getCreatedAt()
        );
    }
}
