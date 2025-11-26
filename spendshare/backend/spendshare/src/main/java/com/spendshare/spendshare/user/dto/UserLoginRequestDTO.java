package com.spendshare.spendshare.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequestDTO {

    @NotBlank(message = "Email or mobile number is required")
    private String identifier;

    @NotBlank(message = "{user.password.required}")
    private String password;
}
