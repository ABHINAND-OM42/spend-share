package com.spendshare.spendshare.expense.entity;

import com.spendshare.spendshare.group.entity.ExpenseGroup;
import com.spendshare.spendshare.user.entity.AppUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EXPENSES")
@Data
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private SplitType splitType;

    private LocalDateTime createdAt;

    //who paid
    @ManyToOne
    @JoinColumn(name = "paid_by_user_id", nullable = false)
    private AppUser paidBy;

    //which group
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private ExpenseGroup group;

    //The splits(One expense-> many splits)
    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ExpenseSplit> splits = new ArrayList<>();


    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }
}
