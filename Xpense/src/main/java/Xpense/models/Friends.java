package Xpense.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "friends")
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_a")
    private UUID userA;

    @Column(name = "user_b")
    private UUID userB;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}