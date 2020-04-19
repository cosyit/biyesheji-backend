package com.edu.college.dao;

import com.edu.college.pojo.GroupRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupRole record);

    int insertSelective(GroupRole record);

    GroupRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupRole record);

    int updateByPrimaryKey(GroupRole record);

    void clearRoles(Integer groupId);

    void assignRole(@Param("groupId") Integer groupId, @Param("roleIds") List<Integer> roleIds);
}