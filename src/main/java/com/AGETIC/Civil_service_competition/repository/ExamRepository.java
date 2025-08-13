// src/main/java/com/AGETIC/Civil_service_competition/repository/ExamRepository.java
package com.AGETIC.Civil_service_competition.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.AGETIC.Civil_service_competition.model.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    // Home page: show only exams whose application deadline is today or later
    Page<Exam> findByApplicationDeadlineGreaterThanEqual(LocalDate today, Pageable pageable);

    // Filter by category
    Page<Exam> findByCategoryId(Long categoryId, Pageable pageable);

    // Simple search by title (case-insensitive)
    Page<Exam> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // Prevent duplicates (optional)
    boolean existsByTitleAndDate(String title, LocalDate date);
}
