package com.spendshare.spendshare.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDTO {

    @NotBlank(message = "{user.name.required}")
    private String name;

    @Email(message = "{user.email.invalid}")
    @NotBlank(message = "{user.email.required}")
    private String email;

    @NotBlank(message = "{user.mobile.required}")
    @Pattern(regexp = "^\\d{10}$", message = "{user.mobile.invalid}")
    private String mobileNumber;

    @Size(min = 6, message = "{user.password.size}")
    private String password;

    @NotBlank(message = "{user.password.confirm.required}")
    private String confirmPassword;

}
