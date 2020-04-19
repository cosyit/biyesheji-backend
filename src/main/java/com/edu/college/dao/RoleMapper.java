package com.edu.college.dao;

import com.edu.college.pojo.Group;
import com.edu.college.pojo.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Group> roles(Integer groupId);

    List<Role> getRoles(Integer userId);

    void rename(String name, String newName);

    void deleteByName(String name);
}