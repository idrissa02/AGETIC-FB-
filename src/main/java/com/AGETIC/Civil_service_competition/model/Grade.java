package com.AGETIC.Civil_service_competition.model;



import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "grades")
public class Grade {

    @EmbeddedId
    private GradeId id = new GradeId();

    // --- Relationships ---

    // Candidate (linked through ID)
    @MapsId("candidateId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    
    // Test (linked through ID)
    @MapsId("testId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "graded_at")
    private LocalDateTime gradedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "correction_reason", length = 1000)
    private String correctionReason;

    // Admin who graded (optional)
    @ManyToOne
    @JoinColumn(name = "graded_by")
    private Admin gradedBy;

    // --- Constructors ---
    public Grade() {}

    public Grade(Candidate candidate, Test test, Double score, Admin gradedBy) {
        this.candidate = candidate;
        this.test = test;
        this.score = score;
        this.gradedBy = gradedBy;
        this.gradedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.id = new GradeId(candidate.getId(), test.getId());
    }

    // --- Getters & Setters ---
    public GradeId getId() { return id; }
    public void setId(GradeId id) { this.id = id; }

    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
        this.id.setCandidateId(candidate.getId());
    }

    public Test getTest() { return test; }
    public void setTest(Test test) {
        this.test = test;
        this.id.setTestId(test.getId());
    }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public LocalDateTime getGradedAt() { return gradedAt; }
    public void setGradedAt(LocalDateTime gradedAt) { this.gradedAt = gradedAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCorrectionReason() { return correctionReason; }
    public void setCorrectionReason(String correctionReason) { this.correctionReason = correctionReason; }

    public Admin getGradedBy() { return gradedBy; }
    public void setGradedBy(Admin gradedBy) { this.gradedBy = gradedBy; }

    // --- equals & hashCode ---
    /**
     * Grade is identified by composite key (candidate_id, test_id)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grade)) return false;
        Grade grade = (Grade) o;
        return Objects.equals(id, grade.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- toString ---
    @Override
    public String toString() {
        return "Grade{" +
               "candidateId=" + id.getCandidateId() +
               ", testId=" + id.getTestId() +
               ", score=" + score +
               ", gradedAt=" + gradedAt +
               '}';
    }
}
