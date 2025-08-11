package com.AGETIC.Civil_service_competition.model;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.AGETIC.Civil_service_competition.Enum.ApplicationStatus;

@Entity
@Table(name = "applications",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_nina_exam", columnNames = {"nina_number", "exam_id"})
       },
       indexes = {
           @Index(name = "idx_nina_number", columnList = "nina_number")
       })
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nina_number", nullable = false, length = 50)
    private String ninaNumber;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "surname", nullable = false, length = 100)
    private String surname;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(name = "picture_url", length = 500)
    private String pictureUrl;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "documents_url", length = 500)
    private String documentsUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // --- Relationships ---

    // One application belongs to one exam
    @ManyToOne(optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    // One application can have zero or one candidate (once accepted)
    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    private Candidate candidate;

    // One application can have many notifications
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    // --- Constructors ---
    public Application() {}

    public Application(Long id, String ninaNumber, String name, String surname, String phone,
                       String email, ApplicationStatus status, String pictureUrl,
                       LocalDate birthDate, String documentsUrl,
                       LocalDateTime createdAt, Exam exam) {
        this.id = id;
        this.ninaNumber = ninaNumber;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.status = status;
        this.pictureUrl = pictureUrl;
        this.birthDate = birthDate;
        this.documentsUrl = documentsUrl;
        this.createdAt = createdAt;
        this.exam = exam;
    }

    

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNinaNumber() { return ninaNumber; }
    public void setNinaNumber(String ninaNumber) { this.ninaNumber = ninaNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public String getPictureUrl() { return pictureUrl; }
    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getDocumentsUrl() { return documentsUrl; }
    public void setDocumentsUrl(String documentsUrl) { this.documentsUrl = documentsUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }

    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }

    public List<Notification> getNotifications() { return notifications; }
    public void setNotifications(List<Notification> notifications) { this.notifications = notifications; }

    // --- Helpers ---
    /**
     * Ensures both sides of the Application ↔ Notification relationship are in sync.
     * Adds the notification to this application and sets this application in the notification.
     */
    public void addNotification(Notification notif) {
        if (!this.notifications.contains(notif)) {
            this.notifications.add(notif);
            notif.setApplication(this);
        }
    }

    // --- equals & hashCode ---
    /**
     * Equality is based solely on the primary key (id).
     * This ensures that two Application objects referring to the same database row
     * are considered equal, even if other fields differ.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Application)) return false;
        Application that = (Application) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- toString ---
    @Override
    public String toString() {
        return "Application{id=" + id +
               ", ninaNumber='" + ninaNumber + '\'' +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", status=" + status +
               '}';
    }
}
