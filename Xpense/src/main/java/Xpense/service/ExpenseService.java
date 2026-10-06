package Xpense.service;

import Xpense.models.Expense;
import Xpense.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import Xpense.models.XPEvent;
import Xpense.repository.XPEventRepository;
import Xpense.repository.UserRepository;
import Xpense.models.User;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    @Lazy
    private XPService xpService;

    @Autowired
    private XPEventRepository xpEventRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Expense> getUserExpenses(String userId) {
        return expenseRepository.findByUserId(UUID.fromString(userId));
    }

    public Expense addExpense(String userId, String category,
                              double amount, String note, String expenseDate) {
        Expense expense = new Expense();
        expense.setUserId(UUID.fromString(userId));
        expense.setCategory(category);
        expense.setAmount(amount);
        expense.setNote(note);
        expense.setExpenseDate(expenseDate != null && !expenseDate.isEmpty()
                ? LocalDate.parse(expenseDate)
                : LocalDate.now());
        expense.setCreatedAt(java.time.LocalDateTime.now());

        Expense saved = expenseRepository.save(expense);

        List<Expense> allExpenses = expenseRepository.findByUserId(UUID.fromString(userId));
        if (allExpenses.size() == 1) {
            XPEvent event = new XPEvent();
            event.setUserId(UUID.fromString(userId));
            event.setSource("first_expense");
            event.setPoints(50);
            event.setDescription("Logged your first expense! 🎉");
            event.setCreatedAt(java.time.LocalDateTime.now());
            xpEventRepository.save(event);
            userRepository.findById(UUID.fromString(userId)).ifPresent(u -> {
                u.setTotalXp(u.getTotalXp() + 50);
                userRepository.save(u);
            });
        }

        xpService.checkAndAwardXP(userId, category);
        xpService.updateChallengeProgress(userId);
        return saved;
    }

    public void deleteExpense(String expenseId, String userId) {
        expenseRepository.findById(UUID.fromString(expenseId)).ifPresent(e -> {
            String category = e.getCategory();
            expenseRepository.deleteById(UUID.fromString(expenseId));
            xpService.checkAndAwardXP(userId, category);
        });
    }
}