package com.AGETIC.Civil_service_competition.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.AGETIC.Civil_service_competition.model.Candidate;


public interface CandidateRepository extends JpaRepository<Candidate, Long> { 
        Optional<Candidate> findByCandidateNumber(String candidateNumber);
    boolean existsByCandidateNumber(String candidateNumber);
    Optional<Candidate> findByApplicationId(Long applicationId);
}
