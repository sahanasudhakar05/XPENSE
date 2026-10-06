package Xpense.service;

import Xpense.models.Budget;
import Xpense.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    public Budget setBudget(String userIdStr, String category, double limit) {
        UUID userId = UUID.fromString(userIdStr);
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();

        Optional<Budget> existing = budgetRepository
                .findByUserIdAndCategoryAndMonthAndYear(userId, category, month, year);

        Budget budget = existing.orElse(new Budget());
        budget.setUserId(userId);
        budget.setCategory(category);
        budget.setMonthlyLimit(limit);
        budget.setMonth(month);
        budget.setYear(year);

        return budgetRepository.save(budget);
    }

    public List<Budget> getUserBudgets(String userIdStr) {
        UUID userId = UUID.fromString(userIdStr);
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        return budgetRepository.findByUserIdAndMonthAndYear(userId, month, year);
    }
}