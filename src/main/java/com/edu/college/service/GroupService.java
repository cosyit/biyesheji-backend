package com.edu.college.service;

import com.edu.college.pojo.Group;

import java.util.List;

public interface GroupService {
    List<Group> groups(final Integer userId);

    void save(Group build);

    void delete(String name);

    void update(String name, final String newName);

    void assignRole(Integer groupId, List<Integer> roleIds);

    void assignGroup(Integer userId, List<Integer> groupIds);
}
