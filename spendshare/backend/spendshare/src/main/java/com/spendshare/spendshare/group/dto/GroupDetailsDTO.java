package com.spendshare.spendshare.group.dto;

import com.spendshare.spendshare.user.dto.UserSummaryDTO;
import lombok.Data;

import java.util.List;

@Data
public class GroupDetailsDTO {

    private Long id;
    private String name;
    private String description;
    private List<UserSummaryDTO> members;

}
