package com.spendshare.spendshare.user.validation;

import com.spendshare.spendshare.exception.ValidationException;
import com.spendshare.spendshare.user.dto.UserRegistrationDTO;
import com.spendshare.spendshare.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.spendshare.spendshare.user.constants.error_constants.UserErrorConstants.*;

@Component
public class UserValidator {

    @Autowired
    private UserRepository userRepository;

    public void validateRegistration(UserRegistrationDTO userDto){

        // check if password match
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())){
            throw new ValidationException(PASSWORD_DOES_NOT_MATCH);
        }

        // check if email already exists
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new ValidationException(USER_WITH_THIS_EMAIL_ALREADY_EXISTS);
        }

        // check if mobile number already exists
        if (userRepository.findByMobileNumber(userDto.getMobileNumber()).isPresent()){
            throw new ValidationException(USER_WITH_THIS_MOBILE_NUMBER_ALREADY_EXISTS);
        }
    }
}
