// service/ExamService.java
package com.AGETIC.Civil_service_competition.service;

import com.AGETIC.Civil_service_competition.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExamService {
    
    ExamResponse create(ExamCreateRequest req);
    ExamResponse update(Long id, ExamUpdateRequest req);
    void delete(Long id);

    ExamResponse get(Long id);
    Page<ExamResponse> listOpen(Pageable pageable);                 // deadline >= today
    Page<ExamResponse> listByCategory(Long categoryId, Pageable p);
    Page<ExamResponse> searchByTitle(String q, Pageable p);
}
