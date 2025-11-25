package com.spendshare.spendshare.group.repository;

import com.spendshare.spendshare.group.entity.ExpenseGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<ExpenseGroup,Long> {
}
