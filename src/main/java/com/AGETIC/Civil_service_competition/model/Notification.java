package com.AGETIC.Civil_service_competition.model;



import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notifications",
       indexes = {
           @Index(name = "idx_notification_sent_at", columnList = "sent_at")
       })
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();


    // --- Relationships ---

    // The admin who sent the notification (optional)
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    // The candidate who receives the notification (optional)
    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    // The application this notification is about (optional)
    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

    // --- Constructors ---
    public Notification() {}

    public Notification(Long id, String message, LocalDateTime sentAt, 
                        Admin admin, Candidate candidate, Application application) {
        this.id = id;
        this.message = message;
        this.sentAt = sentAt;
       
        this.admin = admin;
        this.candidate = candidate;
        this.application = application;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

   

    public Admin getAdmin() { return admin; }
    public void setAdmin(Admin admin) { this.admin = admin; }

    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }

    public Application getApplication() { return application; }
    public void setApplication(Application application) { this.application = application; }

    // --- equals & hashCode ---
    /**
     * Equality is based solely on the primary key (id).
     * Ensures two Notification objects representing the same database row
     * are considered equal, even if other fields differ.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- toString ---
    @Override
    public String toString() {
        return "Notification{id=" + id +
               ", message='" + message + '\'' +
               ", sentAt=" + sentAt +
               '}';
    }
}
