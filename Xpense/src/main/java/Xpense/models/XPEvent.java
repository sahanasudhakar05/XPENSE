package Xpense.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "xp_events")
public class XPEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    private String source;
    private int points;
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}