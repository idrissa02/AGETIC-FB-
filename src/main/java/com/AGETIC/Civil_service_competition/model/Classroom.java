package com.AGETIC.Civil_service_competition.model;



import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "classrooms",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_classroom_number_per_center", columnNames = {"number", "center_id"})
       })
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number", nullable = false, length = 50)
    private String number;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    // --- Relationships ---


    // Each classroom belongs to a center
    @ManyToOne(optional = false)
    @JoinColumn(name = "center_id", nullable = false)
    private Center center;

    // --- Constructors ---
    public Classroom() {}

    public Classroom(Long id, String number, Integer capacity, Center center) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
        this.center = center;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Center getCenter() { return center; }
    public void setCenter(Center center) { this.center = center; }

    
    // --- equals & hashCode ---
    /**
     * Equality is based solely on the primary key (id).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Classroom)) return false;
        Classroom that = (Classroom) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- toString ---
    @Override
    public String toString() {
        return "Classroom{id=" + id +
               ", number='" + number + '\'' +
               ", capacity=" + capacity +
               ", center=" + (center != null ? center.getName() : null) +
               '}';
    }
}
