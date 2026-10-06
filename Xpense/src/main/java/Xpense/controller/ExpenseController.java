package Xpense.controller;

import Xpense.models.Expense;
import Xpense.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(@RequestParam String userId) {
        return ResponseEntity.ok(expenseService.getUserExpenses(userId));
    }

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Map<String, String> body) {
        Expense expense = expenseService.addExpense(
                body.get("userId"),
                body.get("category"),
                Double.parseDouble(body.get("amount")),
                body.get("note"),
                body.get("expenseDate")
        );
        return ResponseEntity.ok(expense);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable String id,
                                              @RequestParam String userId) {
        expenseService.deleteExpense(id, userId);
        return ResponseEntity.ok().build();
    }
}