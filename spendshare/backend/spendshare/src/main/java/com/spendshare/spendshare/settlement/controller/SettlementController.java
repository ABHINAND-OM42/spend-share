package com.spendshare.spendshare.settlement.controller;

import com.spendshare.spendshare.dto.ApiResponse;
import com.spendshare.spendshare.settlement.dto.SettlementDTO;
import com.spendshare.spendshare.settlement.entity.Settlement;
import com.spendshare.spendshare.settlement.service.SettlementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {

    @Autowired
    private SettlementService settlementService;

    @PostMapping
    public ResponseEntity<ApiResponse<Settlement>> recordSettlement(
            @Valid @RequestBody SettlementDTO dto
            ){
        Settlement settlement = settlementService.recordSettlement(dto);
        return ResponseEntity.ok(new ApiResponse<>(true,"Payment recorded successfully",settlement));
    }
}
