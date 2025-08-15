package com.AGETIC.Civil_service_competition.repository;

import com.AGETIC.Civil_service_competition.model.Grade;
import com.AGETIC.Civil_service_competition.model.GradeId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, GradeId> {

    List<Grade> findByCandidateId(Long candidateId);

    List<Grade> findByTestId(Long testId);

    Optional<Grade> findByCandidateIdAndTestId(Long candidateId, Long testId);

    boolean existsByCandidateIdAndTestId(Long candidateId, Long testId);
}
