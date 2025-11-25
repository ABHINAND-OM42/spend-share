package com.spendshare.spendshare.group.controller;

import com.spendshare.spendshare.dto.ApiResponse;
import com.spendshare.spendshare.group.dto.BalanceSheetDTO;
import com.spendshare.spendshare.group.dto.CreateGroupDto;
import com.spendshare.spendshare.group.entity.ExpenseGroup;
import com.spendshare.spendshare.group.service.BalanceService;
import com.spendshare.spendshare.group.service.GroupService;
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
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private BalanceService balanceService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseGroup>> createGroup(
            @Valid @RequestBody CreateGroupDto groupDto
            ){
        ExpenseGroup createGroup = groupService.createGroup(groupDto);
        return ResponseEntity.ok(new ApiResponse<>(true,"Group created successfully", createGroup));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseGroup>>> getAllGroups() {
        List<ExpenseGroup> groups = groupService.getAllGroups();
        return ResponseEntity.ok(new ApiResponse<>(true, "Groups fetched successfully", groups));
    }

    @GetMapping("/{groupId}/balances")
    public ResponseEntity<ApiResponse<List<BalanceSheetDTO>>> getGroupBalances(@PathVariable Long groupId) {
        List<BalanceSheetDTO> balances = balanceService.calculateBalances(groupId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Balances calculated", balances));
    }
}
