package com.edu.college.service;

import com.edu.college.pojo.Group;
import com.edu.college.pojo.Role;

import java.util.List;

public interface RoleService {
    List<Group> roles(Integer groupId);

    List<Role> userRoles(Integer userId);

    void save(Role role);

    void update(String name, String newName);

    void delete(String name);
}
