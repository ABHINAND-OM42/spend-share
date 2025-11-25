package com.spendshare.spendshare.user.service;

import com.spendshare.spendshare.user.dto.UserRegistrationDTO;
import com.spendshare.spendshare.user.entity.AppUser;
import com.spendshare.spendshare.user.repository.UserRepository;
import com.spendshare.spendshare.user.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidator userValidator;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AppUser registerUser(UserRegistrationDTO userDto){

        userValidator.validateRegistration(userDto);

        AppUser newUser = new AppUser();
        newUser.setName(userDto.getName());
        newUser.setEmail(userDto.getEmail());
        newUser.setMobileNumber(userDto.getMobileNumber());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(newUser);
    }
}
