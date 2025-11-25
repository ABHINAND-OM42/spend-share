package com.spendshare.spendshare.group.service;

import com.spendshare.spendshare.group.dto.CreateGroupDto;
import com.spendshare.spendshare.group.entity.ExpenseGroup;
import com.spendshare.spendshare.group.repository.GroupRepository;
import com.spendshare.spendshare.group.validation.GroupValidator;
import com.spendshare.spendshare.user.entity.AppUser;
import com.spendshare.spendshare.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupValidator groupValidator;

    @Transactional
    public ExpenseGroup createGroup(CreateGroupDto groupDto){

        List<AppUser> members = userRepository.findAllById(groupDto.getMemberIds());

        groupValidator.validateGroup(groupDto,members);

        ExpenseGroup group = new ExpenseGroup();
        group.setName(groupDto.getName());
        group.setDescription(groupDto.getDescription());
        group.setMembers(new HashSet<>(members));
        return groupRepository.save(group);
    }

    public List<ExpenseGroup> getAllGroups(){
        return groupRepository.findAll();
    }

}
