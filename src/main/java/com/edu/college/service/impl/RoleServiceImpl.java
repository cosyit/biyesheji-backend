package com.edu.college.service.impl;

import com.edu.college.dao.RoleMapper;
import com.edu.college.pojo.Group;
import com.edu.college.pojo.Role;
import com.edu.college.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper mapper;

    @Override
    public List<Group> roles(final Integer groupId) {
        return mapper.roles(groupId);
    }

    @Override
    public List<Role> userRoles(final Integer userId) {
        return mapper.getRoles(userId);
    }

    @Override
    public void save(final Role role) {
        try {
            mapper.insertSelective(role);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("已经有同名的角色了");
        }
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
    public void delete(final String name) {
        mapper.deleteByName(name);
    }
}
