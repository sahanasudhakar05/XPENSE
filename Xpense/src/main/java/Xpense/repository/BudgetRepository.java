package Xpense.repository;

import Xpense.models.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    Optional<Budget> findByUserIdAndCategoryAndMonthAndYear(
            UUID userId, String category, int month, int year
    );
    List<Budget> findByUserIdAndMonthAndYear(
            UUID userId, int month, int year
    );
}