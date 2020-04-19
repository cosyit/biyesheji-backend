package com.edu.college.service.impl;

import com.edu.college.dao.GroupMapper;
import com.edu.college.dao.GroupRoleMapper;
import com.edu.college.dao.UserGroupMapper;
import com.edu.college.pojo.Group;
import com.edu.college.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupMapper mapper;
    @Autowired
    private GroupRoleMapper groupRoleMapper;
    @Autowired
    private UserGroupMapper userGroupMapper;

    @Override
    public List<Group> groups(final Integer userId) {
        return mapper.groups(userId);
    }

    @Override
    public void save(final Group group) {
        try {
            mapper.insertSelective(group);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("已经有同名的组了");
        }
    }

    @Override
    public void delete(final String name) {
        mapper.deleteByName(name);
    }

    @Override
    public void update(final String name, final String newName) {
        try {
            mapper.rename(name, newName);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("已经有同名的组了");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRole(final Integer groupId, final List<Integer> roleIds) {
        groupRoleMapper.clearRoles(groupId);
        groupRoleMapper.assignRole(groupId, roleIds);
    }

    @Override
    public void assignGroup(final Integer userId, final List<Integer> groupIds) {
        userGroupMapper.clearGroups(userId);
        userGroupMapper.assignGroup(userId, groupIds);
    }
}
