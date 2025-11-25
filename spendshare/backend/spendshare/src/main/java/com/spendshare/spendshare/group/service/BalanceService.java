package com.spendshare.spendshare.group.service;

import com.spendshare.spendshare.expense.entity.Expense;
import com.spendshare.spendshare.expense.entity.ExpenseSplit;
import com.spendshare.spendshare.expense.repository.ExpenseRepository;
import com.spendshare.spendshare.group.dto.BalanceSheetDTO;
import com.spendshare.spendshare.settlement.entity.Settlement;
import com.spendshare.spendshare.settlement.repository.SettlementRepository;
import com.spendshare.spendshare.user.entity.AppUser;
import com.spendshare.spendshare.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class BalanceService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private UserRepository userRepository;
    public List<BalanceSheetDTO> calculateBalances(Long groupId) {
        // Step 1: Calculate Net Balance for every user in the group
        // Map <UserId, NetAmount>
        Map<Long, BigDecimal> netBalances = new HashMap<>();

        // --- A. Process Expenses ---
        // If you paid, you are owed money (+). If you split, you owe money (-).
        List<Expense> expenses = expenseRepository.findByGroupIdOrderByCreatedAtDesc(groupId);
        for (Expense expense : expenses) {
            // Add Total to Payer (Positive)
            addBalance(netBalances, expense.getPaidBy().getId(), expense.getTotalAmount());

            // Subtract Split Amount from Consumers (Negative)
            for (ExpenseSplit split : expense.getSplits()) {
                addBalance(netBalances, split.getUser().getId(), split.getAmount().negate());
            }
        }

        // --- B. Process Settlements ---
        // If you paid a settlement, your balance goes UP (+). If you received, it goes DOWN (-).
        List<Settlement> settlements = settlementRepository.findByGroupId(groupId);
        for (Settlement settlement : settlements) {
            addBalance(netBalances, settlement.getPayer().getId(), settlement.getAmount());
            addBalance(netBalances, settlement.getPayee().getId(), settlement.getAmount().negate());
        }

        // Step 2: Separate users into Debtors (-) and Creditors (+)
        Map<Long, BigDecimal> debtors = new HashMap<>();
        Map<Long, BigDecimal> creditors = new HashMap<>();

        for (Map.Entry<Long, BigDecimal> entry : netBalances.entrySet()) {
            // Check if value is significantly different from zero (ignore 0.00)
            if (entry.getValue().compareTo(new BigDecimal("0.01")) > 0) {
                creditors.put(entry.getKey(), entry.getValue());
            } else if (entry.getValue().compareTo(new BigDecimal("-0.01")) < 0) {
                debtors.put(entry.getKey(), entry.getValue().abs()); // Store as positive for easier math
            }
        }

        // Step 3: Match them up (Greedy Algorithm)
        List<BalanceSheetDTO> balanceSheet = new ArrayList<>();

        Iterator<Map.Entry<Long, BigDecimal>> debtorIter = debtors.entrySet().iterator();
        Iterator<Map.Entry<Long, BigDecimal>> creditorIter = creditors.entrySet().iterator();

        // Get the first person from each list
        Map.Entry<Long, BigDecimal> currentDebtor = debtorIter.hasNext() ? debtorIter.next() : null;
        Map.Entry<Long, BigDecimal> currentCreditor = creditorIter.hasNext() ? creditorIter.next() : null;

        while (currentDebtor != null && currentCreditor != null) {
            BigDecimal debtorAmount = currentDebtor.getValue();
            BigDecimal creditorAmount = currentCreditor.getValue();

            // Find the minimum amount we can settle right now
            BigDecimal settledAmount = debtorAmount.min(creditorAmount);

            // Create the Report Entry
            String debtorName = getUserName(currentDebtor.getKey());
            String creditorName = getUserName(currentCreditor.getKey());

            // Only add if amount is valid
            if (settledAmount.compareTo(BigDecimal.ZERO) > 0) {
                balanceSheet.add(new BalanceSheetDTO(debtorName, creditorName, settledAmount));
            }

            // Adjust remaining amounts
            debtorAmount = debtorAmount.subtract(settledAmount);
            creditorAmount = creditorAmount.subtract(settledAmount);

            // Update the current entries
            currentDebtor.setValue(debtorAmount);
            currentCreditor.setValue(creditorAmount);

            // If Debtor is fully paid off (approx 0), move to next Debtor
            if (debtorAmount.compareTo(new BigDecimal("0.01")) < 0) {
                currentDebtor = debtorIter.hasNext() ? debtorIter.next() : null;
            }
            // If Creditor is fully paid off (approx 0), move to next Creditor
            if (creditorAmount.compareTo(new BigDecimal("0.01")) < 0) {
                currentCreditor = creditorIter.hasNext() ? creditorIter.next() : null;
            }
        }

        return balanceSheet;
    }

    // Helper: Safely add values to the Map (handling nulls)
    private void addBalance(Map<Long, BigDecimal> map, Long userId, BigDecimal amount) {
        map.put(userId, map.getOrDefault(userId, BigDecimal.ZERO).add(amount));
    }

    // Helper: Get User Name from ID
    private String getUserName(Long userId) {
        return userRepository.findById(userId).map(AppUser::getName).orElse("Unknown User");
    }
}
