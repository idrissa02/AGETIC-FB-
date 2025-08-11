package com.AGETIC.Civil_service_competition.model;


import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categories",
       indexes = {
         @Index(name = "idx_category_type", columnList = "type", unique = true)
       })
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type", nullable = false, unique = true, length = 100)
    private String type;   // e.g. "A", "B+", "A+"



    // --- Relationships ---

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exam> exams = new ArrayList<>();



    // --- Constructors ---

    public Category() {}

    public Category(Long id, String type) {
        this.id = id;
        this.type = type;
    }



    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<Exam> getExams() { return exams; }
    public void setExams(List<Exam> exams) { this.exams = exams; }


    // --- Helpers ---

    public void addExam(Exam exam) {
        if (!this.exams.contains(exam)) {
            this.exams.add(exam);
            exam.setCategory(this);
        }
    }

    // --- equals & hashCode ---
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- toString ---
    @Override
    public String toString() {
        return "Category{id=" + id +
               ", type='" + type + '\'' +
               '}';
    }
}
