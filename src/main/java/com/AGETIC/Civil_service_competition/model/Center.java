package com.AGETIC.Civil_service_competition.model;


import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "centers",
       indexes = {
           @Index(name = "idx_center_name", columnList = "name", unique = true)
       })
public class Center {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 150)
    private String name;

    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @Column(name = "enabled", nullable = false)
private boolean enabled = true;

    // --- Relationships ---

    // One center has many classrooms
    @OneToMany(mappedBy = "center", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Classroom> classrooms = new ArrayList<>();

    // One center can host many exams (ManyToMany)
    @ManyToMany(mappedBy = "centers")
    private Set<Exam> exams = new HashSet<>();

    // --- Constructors ---
    public Center() {}

    public Center(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<Classroom> getClassrooms() { return classrooms; }
    public void setClassrooms(List<Classroom> classrooms) { this.classrooms = classrooms; }

    public Set<Exam> getExams() { return exams; }
    public void setExams(Set<Exam> exams) { this.exams = exams; }

public boolean isEnabled() { return enabled; }
public void setEnabled(boolean enabled) { this.enabled = enabled; }

    
    // --- Helpers ---
    /**
     * Ensures the bidirectional link between Center and Classroom is kept in sync.
     */
    public void addClassroom(Classroom classroom) {
        if (!this.classrooms.contains(classroom)) {
            this.classrooms.add(classroom);
            classroom.setCenter(this);
        }
    }

    public void addExam(Exam exam) {
        if (!this.exams.contains(exam)) {
            this.exams.add(exam);
            exam.getCenters().add(this);
        }
    }

    // --- equals & hashCode ---
    /**
     * Equality is based solely on the primary key (id).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Center)) return false;
        Center center = (Center) o;
        return Objects.equals(id, center.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- toString ---
    @Override
    public String toString() {
        return "Center{id=" + id +
               ", name='" + name + '\'' +
               ", location='" + location + '\'' +
               '}';
    }
}
