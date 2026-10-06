package Xpense.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    private String category;
    private double amount;
    private String note;

    @Column(name = "expense_date")
    private LocalDate expenseDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}