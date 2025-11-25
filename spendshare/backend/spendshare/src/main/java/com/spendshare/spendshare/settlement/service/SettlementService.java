package com.spendshare.spendshare.settlement.service;

import com.spendshare.spendshare.exception.ValidationException;
import com.spendshare.spendshare.group.entity.ExpenseGroup;
import com.spendshare.spendshare.group.repository.GroupRepository;
import com.spendshare.spendshare.settlement.dto.SettlementDTO;
import com.spendshare.spendshare.settlement.entity.Settlement;
import com.spendshare.spendshare.settlement.repository.SettlementRepository;
import com.spendshare.spendshare.user.entity.AppUser;
import com.spendshare.spendshare.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettlementService {

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;


    @Transactional
    public Settlement recordSettlement(SettlementDTO dto){
        if(dto.getPayerId().equals(dto.getPayeeId())){
            throw new ValidationException("Payer and Payee cannot be the same person");
        }

        AppUser payer = userRepository.findById(dto.getPayerId())
                .orElseThrow(() -> new ValidationException("Payer not found"));

        AppUser payee = userRepository.findById(dto.getPayeeId())
                .orElseThrow(() -> new ValidationException("Payee not found"));

        ExpenseGroup group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new ValidationException("Group not found"));

        Settlement settlement = new Settlement();
        settlement.setPayer(payer);
        settlement.setPayee(payee);
        settlement.setGroup(group);
        settlement.setAmount(dto.getAmount());
        return settlementRepository.save(settlement);
    }
}
