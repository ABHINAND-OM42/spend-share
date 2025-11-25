package com.spendshare.spendshare.group.validation;

import com.spendshare.spendshare.exception.ValidationException;
import com.spendshare.spendshare.group.dto.CreateGroupDto;
import com.spendshare.spendshare.user.entity.AppUser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupValidator {

    public void validateGroup(CreateGroupDto groupDto, List<AppUser> members){

        if(members.size() != groupDto.getMemberIds().size()){
            throw new ValidationException("One or more user IDs were not found");
        }
    }
}
