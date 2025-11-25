package com.spendshare.spendshare.expense.controller;


import com.spendshare.spendshare.dto.ApiResponse;
import com.spendshare.spendshare.expense.dto.ExpenseDto;
import com.spendshare.spendshare.expense.entity.Expense;
import com.spendshare.spendshare.expense.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ApiResponse<Expense>> addExpense(
            @Valid @RequestBody ExpenseDto dto
            ){
        Expense createdExpense = expenseService.addExpense(dto);
        return ResponseEntity.ok(new ApiResponse<>(true,"Expense Added successfully", createdExpense));
    }
    @GetMapping("/group/{groupId}")
    public ResponseEntity<ApiResponse<List<Expense>>> getGroupExpenses(
            @PathVariable Long groupId
    ){
        List<Expense> expense = expenseService.getGroupExpenses(groupId);
        return ResponseEntity.ok(new ApiResponse<>(true,"Expense fetched successfully", expense));
    }
}
