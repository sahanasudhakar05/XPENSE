package Xpense.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "challenges")
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String type;
    private String category;

    @Column(name = "target_value")
    private double targetValue;

    @Column(name = "reward_xp")
    private int rewardXp;

    @Column(name = "end_date")
    private LocalDate endDate;
}