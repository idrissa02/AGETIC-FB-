
package com.AGETIC.Civil_service_competition.service.impl;

import com.AGETIC.Civil_service_competition.dto.*;
import com.AGETIC.Civil_service_competition.model.Admin;
import com.AGETIC.Civil_service_competition.model.Category;
import com.AGETIC.Civil_service_competition.model.Exam;
import com.AGETIC.Civil_service_competition.repository.ExamRepository;
import com.AGETIC.Civil_service_competition.service.ExamService;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.AGETIC.Civil_service_competition.repository.CategoryRepository; 
import com.AGETIC.Civil_service_competition.repository.AdminRepository;   

@Service
@Transactional
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepo;
    private final CategoryRepository categoryRepo;
    private final AdminRepository adminRepo;

    public ExamServiceImpl(ExamRepository examRepo,
                           CategoryRepository categoryRepo,
                           AdminRepository adminRepo) {
        this.examRepo = examRepo;
        this.categoryRepo = categoryRepo;
        this.adminRepo = adminRepo;
    }

    @Override
    public ExamResponse create(ExamCreateRequest req) {
    

        validateDates(req.applicationDeadline(), req.date());

        Category cat = categoryRepo.findById(req.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Admin admin = adminRepo.findById(req.createdByAdminId())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (examRepo.existsByTitleAndDate(req.title(), req.date())) {
            throw new RuntimeException("Exam with same title and date already exists");
        }

        Exam e = new Exam();
        e.setTitle(req.title());
        e.setDate(req.date());
        e.setApplicationDeadline(req.applicationDeadline());
        e.setCondition(req.condition());
        e.setQuota(req.quota());
        e.setHours(req.hours());
        e.setCategory(cat);
        e.setCreatedBy(admin);
        e.setCreatedAt(LocalDateTime.now());

        examRepo.save(e);
        return toResponse(e);
    }

    @Override
    public ExamResponse update(Long id, ExamUpdateRequest req) {
        Exam e = examRepo.findById(id).orElseThrow(() -> new RuntimeException("Exam not found"));

        if (req.title() != null) e.setTitle(req.title());
        if (req.date() != null) e.setDate(req.date());
        if (req.applicationDeadline() != null) e.setApplicationDeadline(req.applicationDeadline());
        if (req.condition() != null) e.setCondition(req.condition());
        if (req.quota() != null) e.setQuota(req.quota());
        if (req.hours() != null) e.setHours(req.hours());
        if (req.categoryId() != null) {
            Category cat = categoryRepo.findById(req.categoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            e.setCategory(cat);
        }
        if (e.getApplicationDeadline() != null && e.getDate() != null) {
            validateDates(e.getApplicationDeadline(), e.getDate());
        }
        e.setUpdatedAt(LocalDateTime.now());

        return toResponse(e);
    }

    @Override
    public void delete(Long id) {
        if (!examRepo.existsById(id)) throw new RuntimeException("Exam not found");
        examRepo.deleteById(id);
    }

    @Override
    public ExamResponse get(Long id) {
        return examRepo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
    }

    @Override
    public Page<ExamResponse> listOpen(Pageable p) {
        return examRepo.findByApplicationDeadlineGreaterThanEqual(LocalDate.now(), p)
                .map(this::toResponse);
    }

    @Override
    public Page<ExamResponse> listByCategory(Long categoryId, Pageable p) {
        return examRepo.findByCategoryId(categoryId, p)
                .map(this::toResponse);
    }

    @Override
    public Page<ExamResponse> searchByTitle(String q, Pageable p) {
        return examRepo.findByTitleContainingIgnoreCase(q, p)
                .map(this::toResponse);
    }


    @Override
public void publishResults(Long examId){
  Exam e = examRepo.findById(examId).orElse(null);
  if (e!=null) e.setResultsPublished(true);
}


    
    // helpers
    private void validateDates(LocalDate deadline, LocalDate date) {
        if (deadline != null && date != null && deadline.isAfter(date)) {
            throw new RuntimeException("Application deadline must be on/before exam date");
        }
    }

    private ExamResponse toResponse(Exam e) {
        return new ExamResponse(
                e.getId(),
                e.getTitle(),
                e.getDate(),
                e.getApplicationDeadline(),
                e.getQuota(),
                e.getHours(),
                e.getCategory() != null ? e.getCategory().getId() : null,
                e.getCreatedBy() != null ? e.getCreatedBy().getId() : null,
                e.getTests() != null ? e.getTests().size() : 0,
                e.isResultsPublished(),
                e.getCondition()
        );
    }
}
