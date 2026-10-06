package Xpense.service;

import Xpense.models.User;
import Xpense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(String email, String password, String displayName,
                           String securityQuestion, String securityAnswer) {
        // check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // check if display name already exists
        if (!userRepository.searchByName(displayName).isEmpty()) {
            throw new RuntimeException("Display name already taken");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setDisplayName(displayName);
        user.setSecurityQuestion(securityQuestion);
        user.setSecurityAnswer(securityAnswer);
        user.setTotalXp(0);
        user.setOnboardingComplete(false);
        user.setIncomeSetupComplete(false);
        return userRepository.save(user);
    }

    public Optional<User> login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }

    public User completeOnboarding(String userId, String incomeSource,
                                   String incomeRange, String incomeStability,
                                   String financialGoal, String savingFor) {
        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow();
        user.setIncomeSource(incomeSource);
        user.setIncomeRange(incomeRange);
        user.setIncomeStability(incomeStability);
        user.setFinancialGoal(financialGoal);
        user.setSavingFor(savingFor);
        user.setOnboardingComplete(true);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(UUID.fromString(id));
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public List<User> searchByName(String name) {
        return userRepository.searchByName(name);
    }
    public User completeIncomeSetup(String userId, double income,
                                    double savingsTarget, double investmentTarget,
                                    double fixedExpenses) {
        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow();
        user.setMonthlyIncome(income);
        user.setSavingsTarget(savingsTarget);
        user.setInvestmentTarget(investmentTarget);
        user.setFixedExpenses(fixedExpenses);
        user.setIncomeSetupComplete(true);
        return userRepository.save(user);
    }
    public User resetPassword(String email, String securityAnswer, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (!user.getSecurityAnswer().equalsIgnoreCase(securityAnswer.trim())) {
            throw new RuntimeException("Incorrect security answer");
        }
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    public String getSecurityQuestion(String email) {
        return userRepository.findByEmail(email)
                .map(User::getSecurityQuestion)
                .orElseThrow(() -> new RuntimeException("Email not found"));
    }
}