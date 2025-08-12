// src/main/java/com/AGETIC/Civil_service_competition/repository/ApplicationRepository.java
package com.AGETIC.Civil_service_competition.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.AGETIC.Civil_service_competition.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    Optional<Application> findByNinaNumberAndExamId(String ninaNumber, Long examId);
    boolean existsByNinaNumberAndExamId(String ninaNumber, Long examId);
    Page<Application> findAllByNinaNumber(String ninaNumber, Pageable pageable);
}
