package com.spendshare.spendshare.settlement.entity;

import com.spendshare.spendshare.group.entity.ExpenseGroup;
import com.spendshare.spendshare.user.entity.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SETTLEMENTS")
@Data
@NoArgsConstructor
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payer_id", nullable = false)
    private AppUser payer;

    @ManyToOne
    @JoinColumn(name = "payee_id", nullable = false)
    private AppUser payee;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private ExpenseGroup group;

    @Column(nullable = false)
    private BigDecimal amount;

    private LocalDateTime settledAt;


    @PrePersist
    protected void createOn(){
        settledAt = LocalDateTime.now();
    }


}
