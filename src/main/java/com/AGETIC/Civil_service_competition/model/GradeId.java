package com.AGETIC.Civil_service_competition.model;



import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key for Grade: (candidate_id, test_id)
 */
@Embeddable
public class GradeId implements Serializable {

    private Long candidateId;
    private Long testId;

    public GradeId() {}

    public GradeId(Long candidateId, Long testId) {
        this.candidateId = candidateId;
        this.testId = testId;
    }

    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }

    public Long getTestId() { return testId; }
    public void setTestId(Long testId) { this.testId = testId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GradeId)) return false;
        GradeId that = (GradeId) o;
        return Objects.equals(candidateId, that.candidateId) &&
               Objects.equals(testId, that.testId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidateId, testId);
    }
}
