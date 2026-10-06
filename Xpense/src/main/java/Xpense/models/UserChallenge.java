package Xpense.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_challenges")
public class UserChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "challenge_id")
    private UUID challengeId;

    private double progress;
    private boolean completed;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
}