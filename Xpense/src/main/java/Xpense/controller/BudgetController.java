package Xpense.controller;

import Xpense.models.Budget;
import Xpense.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "*")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<Budget>> getBudgets(@RequestParam String userId) {
        return ResponseEntity.ok(budgetService.getUserBudgets(userId));
    }

    @PostMapping
    public ResponseEntity<Budget> setBudget(@RequestBody Map<String, String> body) {
        Budget budget = budgetService.setBudget(
                body.get("userId"),
                body.get("category"),
                Double.parseDouble(body.get("limit"))
        );
        return ResponseEntity.ok(budget);
    }
}