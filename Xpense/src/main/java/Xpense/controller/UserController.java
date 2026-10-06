package Xpense.controller;

import Xpense.models.User;
import Xpense.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private Xpense.repository.UserRepository userRepository;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            User user = userService.createUser(
                    body.get("email"),
                    body.get("password"),
                    body.get("displayName"),
                    body.get("securityQuestion"),
                    body.get("securityAnswer")
            );
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody Map<String, String> body) {
        return userService.login(body.get("email"), body.get("password"))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }

    @PostMapping("/onboarding")
    public ResponseEntity<User> onboarding(@RequestBody Map<String, String> body) {
        try {
            User user = userService.completeOnboarding(
                    body.get("userId"),
                    body.get("incomeSource"),
                    body.get("incomeRange"),
                    body.get("incomeStability"),
                    body.get("financialGoal"),
                    body.get("savingFor")
            );
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> body) {
        return userService.getUserById(body.get("userId"))
                .map(u -> {
                    u.setDisplayName(body.get("displayName"));
                    return ResponseEntity.ok(userRepository.save(u));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body) {
        return userService.getUserById(body.get("userId"))
                .map(u -> {
                    if (!u.getPassword().equals(body.get("currentPassword"))) {
                        return ResponseEntity.badRequest().body("Incorrect current password");
                    }
                    u.setPassword(body.get("newPassword"));
                    return ResponseEntity.ok(userRepository.save(u));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable String id) {
        try {
            userRepository.deleteById(UUID.fromString(id));
            return ResponseEntity.ok("Account deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String name) {
        return ResponseEntity.ok(userService.searchByName(name));
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out");
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        try {
            String question = userService.getSecurityQuestion(body.get("email"));
            return ResponseEntity.ok(Map.of("question", question));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Email not found");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        try {
            userService.resetPassword(
                    body.get("email"),
                    body.get("securityAnswer"),
                    body.get("newPassword")
            );
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/income-setup")
    public ResponseEntity<User> incomeSetup(@RequestBody Map<String, String> body) {
        try {
            User user = userService.completeIncomeSetup(
                    body.get("userId"),
                    Double.parseDouble(body.get("monthlyIncome")),
                    Double.parseDouble(body.get("savingsTarget")),
                    Double.parseDouble(body.get("investmentTarget")),
                    Double.parseDouble(body.get("fixedExpenses"))
            );
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}