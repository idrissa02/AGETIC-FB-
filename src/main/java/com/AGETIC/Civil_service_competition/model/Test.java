package com.AGETIC.Civil_service_competition.model;



import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tests",
       indexes = {
         @Index(name = "idx_test_title", columnList = "title")
       })
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title; 



    // --- Relationships ---



    // Each test belongs to one exam
    @ManyToOne(optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    // One test can have many grades
    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> grades = new ArrayList<>();

    // --- Constructors ---
    public Test() {}

    public Test(Long id, String title, Exam exam) {
        this.id = id;
        this.title = title;
        this.exam = exam;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }

    public List<Grade> getGrades() { return grades; }
    public void setGrades(List<Grade> grades) { this.grades = grades; }



    // --- Helpers ---

    public void addGrade(Grade grade)
     {

        if (!this.grades.contains(grade)) { //avoid adding the same grade twice.
            this.grades.add(grade);
            grade.setTest(this);
        }
    }
    

    // --- equals & hashCode ---
    // Equality is based solely on the primary key (id).
    // This ensures that two Test objects representing the same database row
    // are considered equal, even if their other fields differ in memory.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Test)) return false;
        Test test = (Test) o;
        return Objects.equals(id, test.id);
    }

    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- toString ---
    @Override
    public String toString() {
        return "Test{id=" + id +
               ", title='" + title + '\'' +
               ", examId=" + (exam != null ? exam.getId() : null) +
               '}';
    }
}
