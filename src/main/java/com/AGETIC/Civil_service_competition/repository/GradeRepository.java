// GradeRepository.java
package com.AGETIC.Civil_service_competition.repository;

import com.AGETIC.Civil_service_competition.model.Grade;
import com.AGETIC.Civil_service_competition.model.GradeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, GradeId> {
    List<Grade> findByCandidateId(Long candidateId);
    List<Grade> findByTestExamId(Long examId); // traverses test -> exam
}
