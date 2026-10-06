package Xpense.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;
    private String password;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "total_xp")
    private int totalXp;

    @Column(name = "income_source")
    private String incomeSource;

    @Column(name = "income_range")
    private String incomeRange;

    @Column(name = "income_stability")
    private String incomeStability;

    @Column(name = "financial_goal")
    private String financialGoal;

    @Column(name = "saving_for")
    private String savingFor;

    @Column(name = "onboarding_complete")
    private boolean onboardingComplete;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "monthly_income")
    private Double monthlyIncome;

    @Column(name = "savings_target")
    private Double savingsTarget;

    @Column(name = "investment_target")
    private Double investmentTarget;

    @Column(name = "fixed_expenses")
    private Double fixedExpenses;

    @Column(name = "income_setup_complete")
    private boolean incomeSetupComplete;
    @Column(name = "security_question")
    private String securityQuestion;

    @Column(name = "security_answer")
    private String securityAnswer;
}