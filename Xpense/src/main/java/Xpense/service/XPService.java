package Xpense.service;

import Xpense.models.Budget;
import Xpense.models.Expense;
import Xpense.models.User;
import Xpense.models.XPEvent;
import Xpense.repository.BudgetRepository;
import Xpense.repository.ExpenseRepository;
import Xpense.repository.UserRepository;
import Xpense.repository.XPEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
@Service

public class XPService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private XPEventRepository xpEventRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Xpense.repository.UserChallengeRepository userChallengeRepository;

    @Autowired
    private Xpense.repository.ChallengeRepository challengeRepository;

    public void updateChallengeProgress(String userIdStr) {
        UUID userId = UUID.fromString(userIdStr);
        List<Xpense.models.UserChallenge> active = userChallengeRepository.findByUserId(userId)
                .stream().filter(uc -> !uc.isCompleted()).collect(Collectors.toList());

        for (Xpense.models.UserChallenge uc : active) {
            challengeRepository.findById(uc.getChallengeId()).ifPresent(challenge -> {
                int month = LocalDate.now().getMonthValue();
                int year = LocalDate.now().getYear();

                double spent = expenseRepository.findByUserIdAndCategory(userId, challenge.getCategory())
                        .stream()
                        .filter(e -> e.getExpenseDate() != null &&
                                e.getExpenseDate().getMonthValue() == month &&
                                e.getExpenseDate().getYear() == year)
                        .mapToDouble(Expense::getAmount)
                        .sum();

                uc.setProgress(spent);

                if (spent <= challenge.getTargetValue() &&
                        LocalDate.now().isAfter(challenge.getEndDate())) {
                    uc.setCompleted(true);
                    userRepository.findById(userId).ifPresent(user -> {
                        XPEvent event = new XPEvent();
                        event.setUserId(userId);
                        event.setSource("challenge_complete");
                        event.setPoints(challenge.getRewardXp());
                        event.setDescription("Completed challenge: " + challenge.getTitle());
                        event.setCreatedAt(LocalDateTime.now());
                        xpEventRepository.save(event);
                        user.setTotalXp(user.getTotalXp() + challenge.getRewardXp());
                        userRepository.save(user);
                    });
                }
                userChallengeRepository.save(uc);
            });
        }
    }
    public void checkAndAwardXP(String userIdStr, String category) {
        UUID userId = UUID.fromString(userIdStr);
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();

        Optional<Budget> budget = budgetRepository
                .findByUserIdAndCategoryAndMonthAndYear(userId, category, month, year);

        if (budget.isEmpty()) return;

        // only current month expenses
        List<Expense> expenses = expenseRepository.findByUserIdAndCategory(userId, category)
                .stream()
                .filter(e -> e.getExpenseDate() != null &&
                        e.getExpenseDate().getMonthValue() == month &&
                        e.getExpenseDate().getYear() == year)
                .collect(java.util.stream.Collectors.toList());

        double totalSpent = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return;
        User user = userOpt.get();

        if (totalSpent <= budget.get().getMonthlyLimit()) {
            XPEvent event = new XPEvent();
            event.setUserId(userId);
            event.setSource("budget_kept");
            event.setPoints(20);
            event.setDescription("Stayed within " + category + " budget");
            event.setCreatedAt(LocalDateTime.now());
            xpEventRepository.save(event);
            user.setTotalXp(Math.max(0, user.getTotalXp() + 20));
        } else {
            XPEvent event = new XPEvent();
            event.setUserId(userId);
            event.setSource("overspent");
            event.setPoints(-20);
            event.setDescription("Overspent on " + category + " budget");
            event.setCreatedAt(LocalDateTime.now());
            xpEventRepository.save(event);
            user.setTotalXp(Math.max(0, user.getTotalXp() - 20));
        }
        userRepository.save(user);
    }

    public List<XPEvent> getUserXPHistory(String userIdStr) {
        return xpEventRepository.findByUserId(UUID.fromString(userIdStr));
    }
}