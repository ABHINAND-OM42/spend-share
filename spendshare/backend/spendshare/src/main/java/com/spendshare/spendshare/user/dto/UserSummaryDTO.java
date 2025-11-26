package com.spendshare.spendshare.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSummaryDTO {

    private Long id;
    private String name;
    private String email;
}
