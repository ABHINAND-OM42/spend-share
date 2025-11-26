package com.spendshare.spendshare.user.service;

import com.spendshare.spendshare.exception.ValidationException;
import com.spendshare.spendshare.user.dto.UserLoginRequestDTO;
import com.spendshare.spendshare.user.dto.UserLoginResponseDTO;
import com.spendshare.spendshare.user.dto.UserRegistrationDTO;
import com.spendshare.spendshare.user.entity.AppUser;
import com.spendshare.spendshare.user.repository.UserRepository;
import com.spendshare.spendshare.user.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<AppUser> getAllUsers(){
        return userRepository.findAll();
    }

    public UserLoginResponseDTO loginUser(UserLoginRequestDTO loginDto){
        AppUser user = userRepository.findByEmail(loginDto.getIdentifier()).orElse(null);

        if(user == null){
            user = userRepository.findByMobileNumber(loginDto.getIdentifier()).orElseThrow(() -> new ValidationException("User not found"));
        }

        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            throw new ValidationException("Invalid password");
        }
        return new UserLoginResponseDTO(user.getId(),user.getName(), user.getEmail(),"Login Successful");

    }


}
