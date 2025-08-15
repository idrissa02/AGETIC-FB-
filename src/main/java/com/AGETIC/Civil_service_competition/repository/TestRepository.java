package com.AGETIC.Civil_service_competition.repository;

import com.AGETIC.Civil_service_competition.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {
    boolean existsByExamIdAndTitleIgnoreCase(Long examId, String title);
    boolean existsByExamIdAndTitleIgnoreCaseAndIdNot(Long examId, String title, Long id);

    List<Test> findByTitleContainingIgnoreCase(String titlePart);
    long countByExamId(Long examId);
    List<Test> findByExamId(Long examId);
    List<Test> findByExamIdAndTitleContainingIgnoreCase(Long examId, String titlePart);
    
}


