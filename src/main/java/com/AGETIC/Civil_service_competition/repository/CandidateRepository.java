package com.AGETIC.Civil_service_competition.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import com.AGETIC.Civil_service_competition.model.Candidate;


public interface CandidateRepository extends JpaRepository<Candidate, Long> { }
