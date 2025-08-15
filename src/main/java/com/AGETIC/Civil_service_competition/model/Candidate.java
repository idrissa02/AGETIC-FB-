package com.AGETIC.Civil_service_competition.model;



import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import com.AGETIC.Civil_service_competition.Enum.CandidateStatus;

@Entity
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CandidateStatus status = CandidateStatus.PENDING;

    @Column(name = "candidate_number", nullable = false, unique = true, length = 30)
    private String candidateNumber ;// unique code for each candidate   
    
    @Column(name = "final_score")
    private Double finalScore; // Final score after all exams, null if not calculated yet

    // Each candidate comes from exactly one application (FK in candidates table)
    @OneToOne(optional = false)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private Application application;

    // One candidate can have many grades
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> grades = new ArrayList<>();

    // One candidate can have many notifications
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    // --- Constructors ---
    public Candidate() {}

    public Candidate(Long id, CandidateStatus status, Application application,Double finalScore) {
        this.id = id;
        this.status = status;
        this.application = application;
        this.finalScore = finalScore;
    }

    

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CandidateStatus getStatus() { return status; }
    public void setStatus(CandidateStatus status) { this.status = status; }

    public String getCandidateNumber() { return candidateNumber; }
    public void setCandidateNumber(String candidateNumber) { this.candidateNumber = candidateNumber; }

    public Application getApplication() { return application; }
    public void setApplication(Application application) { this.application = application; }

    public List<Grade> getGrades() { return grades; }
    public void setGrades(List<Grade> grades) { this.grades = grades; }

    public List<Notification> getNotifications() { return notifications; }
    public void setNotifications(List<Notification> notifications) { this.notifications = notifications; }

    public Double getFinalScore() { return finalScore; }
    public void setFinalScore(Double finalScore) { this.finalScore = finalScore; }

    // --- Helpers ---


    // Keep both sides in sync for Grade
    public void addGrade(Grade grade) {
        if (!this.grades.contains(grade)) {
            this.grades.add(grade);
            grade.setCandidate(this);
        }
    }

    // Keep both sides in sync for Notification
    public void addNotification(Notification notif) {
        if (!this.notifications.contains(notif)) {
            this.notifications.add(notif);
            notif.setCandidate(this);
        }
    }

    // --- equals & hashCode based on id ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Candidate)) return false;
        Candidate that = (Candidate) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Candidate{id=" + id +
               ", status=" + status +
               ", applicationId=" + (application != null ? application.getId() : null) +
               '}';
    }
}
