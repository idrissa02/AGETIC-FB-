package com.AGETIC.Civil_service_competition.model;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "exams",
       indexes = {
         @Index(name = "idx_exam_title", columnList = "title")
       })
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "exam_date", nullable = false)
    private LocalDate date; // Day of the exam

    @Column(name = "application_deadline", nullable = false)
    private LocalDate applicationDeadline;

    @Column(name = "conditions", length = 2000)
    private String condition; // Eligibility conditions

    @Column(name = "quota")
    private Integer quota; // Number of available spots

    @Column(name = "hours")
    private Integer hours; // Duration in hours

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "results_published", nullable = false)
    private boolean resultsPublished = false; // Whether results are published or not


    // --- Relationships ---

    // Each exam belongs to a category
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Admin who created the exam
    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private Admin createdBy;

    // Tests that belong to this exam
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Test> tests = new ArrayList<>();


    // Centers hosting the exam (ManyToMany)
    @ManyToMany
    @JoinTable(name = "exam_centers",
               joinColumns = @JoinColumn(name = "exam_id"),
               inverseJoinColumns = @JoinColumn(name = "center_id"))
    private Set<Center> centers = new HashSet<>();


    
    // --- Constructors ---
    public Exam() {}

    public Exam(Long id, String title, LocalDate date, LocalDate applicationDeadline,
                String condition, Integer quota, Integer hours,
                LocalDateTime createdAt, LocalDateTime updatedAt,
                Category category, Admin createdBy) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.applicationDeadline = applicationDeadline;
        this.condition = condition;
        this.quota = quota;
        this.hours = hours;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
        this.createdBy = createdBy;
    }

   
    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalDate getApplicationDeadline() { return applicationDeadline; }
    public void setApplicationDeadline(LocalDate applicationDeadline) { this.applicationDeadline = applicationDeadline; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public Integer getQuota() { return quota; }
    public void setQuota(Integer quota) { this.quota = quota; }

    public Integer getHours() { return hours; }
    public void setHours(Integer hours) { this.hours = hours; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Admin getCreatedBy() { return createdBy; }
    public void setCreatedBy(Admin createdBy) { this.createdBy = createdBy; }

    public List<Test> getTests() { return tests; }
    public void setTests(List<Test> tests) { this.tests = tests; }

    public Set<Center> getCenters() { return centers; }
    public void setCenters(Set<Center> centers) { this.centers = centers; }

    public boolean isResultsPublished() { return resultsPublished; }
    public void setResultsPublished(boolean resultsPublished) { this.resultsPublished = resultsPublished; }

    // --- Helpers ---
    public void addTest(Test test) {
        if (!this.tests.contains(test)) {
            this.tests.add(test);
            test.setExam(this);
        }
    }

    public void addCenter(Center center) {
        this.centers.add(center);
        center.getExams().add(this);
    }

    // --- equals & hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exam)) return false;
        Exam exam = (Exam) o;
        return Objects.equals(id, exam.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- toString ---
    @Override
    public String toString() {
        return "Exam{id=" + id +
               ", title='" + title + '\'' +
               ", date=" + date +
               ", applicationDeadline=" + applicationDeadline +
               '}';
    }
}
