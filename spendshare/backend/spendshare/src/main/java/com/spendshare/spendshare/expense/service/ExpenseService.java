package com.spendshare.spendshare.expense.service;

import com.spendshare.spendshare.exception.ValidationException;
import com.spendshare.spendshare.expense.dto.ExpenseDto;
import com.spendshare.spendshare.expense.entity.Expense;
import com.spendshare.spendshare.expense.entity.ExpenseSplit;
import com.spendshare.spendshare.expense.entity.SplitType;
import com.spendshare.spendshare.expense.repository.ExpenseRepository;
import com.spendshare.spendshare.group.entity.ExpenseGroup;
import com.spendshare.spendshare.group.repository.GroupRepository;
import com.spendshare.spendshare.user.entity.AppUser;
import com.spendshare.spendshare.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Expense addExpense(ExpenseDto dto){

        // 1. Validate group
        ExpenseGroup group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new ValidationException("Group not found"));

        // 2. Validate Payer
        AppUser payer = userRepository.findById(dto.getPaidByUserId())
                .orElseThrow(() -> new ValidationException("Payer not found"));

        // 3. Create Expense Header
        Expense expense = new Expense();
        expense.setDescription(dto.getDescription());
        expense.setTotalAmount(dto.getTotalAmount());
        expense.setSplitType(SplitType.valueOf(dto.getSplitTYpe()));
        expense.setGroup(group);
        expense.setPaidBy(payer);

        List<ExpenseSplit> splits;
        if(SplitType.EQUAL.name().equals(dto.getSplitTYpe())){
            splits = calculateEqualSplits(dto, expense);
        } else {
            splits = calculateExactSplits(dto, expense);
        }

        expense.setSplits(splits);
        return expenseRepository.save(expense);
    }


    private List<ExpenseSplit> calculateEqualSplits(ExpenseDto dto, Expense expense){

        List<ExpenseSplit> splits = new ArrayList<>();
        int count = dto.getSplits().size();

        // --- CRITICAL FIX HERE ---
        // We divide by count, keep 2 decimal places, and round down.
        // 100 / 3 = 33.33 (remainder ignored for now)
        BigDecimal splitAmount = dto.getTotalAmount().divide(
                BigDecimal.valueOf(count),
                2,
                RoundingMode.DOWN
        );

        // Calculate the "Penny Difference"
        // 33.33 * 3 = 99.99. Total is 100. Remainder = 0.01
        BigDecimal totalCalculated = splitAmount.multiply(BigDecimal.valueOf(count));
        BigDecimal remainder = dto.getTotalAmount().subtract(totalCalculated);

        for(int i = 0; i < count; i++){
            ExpenseDto.SplitDefinition def = dto.getSplits().get(i);

            ExpenseSplit split = new ExpenseSplit();
            split.setExpense(expense);
            split.setUser(userRepository.findById(def.getUserId())
                    .orElseThrow(() -> new ValidationException("User not found: " + def.getUserId())));

            // Add the remainder (0.01) to the first person
            if(i == 0){
                split.setAmount(splitAmount.add(remainder));
            } else {
                split.setAmount(splitAmount);
            }
            splits.add(split);
        }
        return splits;
    }

    private List<ExpenseSplit> calculateExactSplits(ExpenseDto dto, Expense expense) {
        List<ExpenseSplit> splits = new ArrayList<>();
        BigDecimal sum = BigDecimal.ZERO;

        for (ExpenseDto.SplitDefinition def : dto.getSplits()) {
            if (def.getAmount() == null) {
                throw new ValidationException("Amount is required for EXACT splits");
            }

            ExpenseSplit split = new ExpenseSplit();
            split.setExpense(expense);
            split.setUser(userRepository.findById(def.getUserId())
                    .orElseThrow(() -> new ValidationException("User not found: " + def.getUserId())));
            split.setAmount(def.getAmount());

            splits.add(split);
            sum = sum.add(def.getAmount());
        }

        // Validate total matches sum
        // Use compareTo (returns 0 if value is equal) to ignore scale differences (100.00 vs 100)
        if (sum.compareTo(dto.getTotalAmount()) != 0) {
            throw new ValidationException("Split amounts (" + sum + ") do not match total (" + dto.getTotalAmount() + ")");
        }
        return splits;
    }

    public List<Expense> getGroupExpenses(Long groupId) {
        return expenseRepository.findByGroupIdOrderByCreatedAtDesc(groupId);
    }
}