package com.spendshare.spendshare.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateGroupDto {

    @NotBlank(message = "Group name is required")
    private String name;

    private String description;

    @NotEmpty(message = "Group must have at least one member")
    private List<Long> memberIds;
}
