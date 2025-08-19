package com.AGETIC.Civil_service_competition.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.AGETIC.Civil_service_competition.model.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    Optional<Candidate> findByCandidateNumber(String candidateNumber);

    boolean existsByCandidateNumber(String candidateNumber);

    Optional<Candidate> findByApplicationId(Long applicationId);

    List<Candidate> findAllByApplication_Exam_Id(Long examId);
}
