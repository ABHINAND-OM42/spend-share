package com.spendshare.spendshare.group.service;

import com.spendshare.spendshare.group.dto.CreateGroupDto;
import com.spendshare.spendshare.group.dto.GroupDetailsDTO;
import com.spendshare.spendshare.group.entity.ExpenseGroup;
import com.spendshare.spendshare.group.repository.GroupRepository;
import com.spendshare.spendshare.group.validation.GroupValidator;
import com.spendshare.spendshare.user.dto.UserSummaryDTO;
import com.spendshare.spendshare.user.entity.AppUser;
import com.spendshare.spendshare.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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

    public GroupDetailsDTO getGroupDetails(Long groupId) {
        ExpenseGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        GroupDetailsDTO dto = new GroupDetailsDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());
        List<UserSummaryDTO> memberDtos = group.getMembers().stream()
                .map(user -> new UserSummaryDTO(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());

        dto.setMembers(memberDtos);
        return dto;
    }

    @Transactional
    public void removeMemberFromGroup(Long groupId, Long userId) {
        // 1. Fetch the Group
        ExpenseGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));

        // 2. Fetch the User
        AppUser userToRemove = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // 3. Check if user is actually in the group
        if (!group.getMembers().contains(userToRemove)) {
            throw new RuntimeException("User is not a member of this group");
        }

        // 4. Remove the user from the group's member list
        group.getMembers().remove(userToRemove);

        // 5. Save the group to trigger the database update (removes row from Join Table)
        groupRepository.save(group);
    }

    public List<ExpenseGroup> getAllGroups(){
        return groupRepository.findAll();
    }

}
