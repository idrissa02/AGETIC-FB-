package com.AGETIC.Civil_service_competition.repositories;
import com.AGETIC.Civil_service_competition.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    // Find exams by title
    List<Exam> findByTitleContainingIgnoreCase(String title);
    // Find exams by category ID
    List<Exam> findByCategoryId(Long categoryId);
    // Find exams after a certain date
    List<Exam> findByDateAfter(LocalDate date);
    // Find exams before applcation deadline
    List<Exam> findByApplicationDeadlineBefore(LocalDate deadline);
}
