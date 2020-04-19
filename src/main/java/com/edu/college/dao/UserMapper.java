package com.edu.college.dao;

import com.edu.college.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<String> getRoles(Integer id);

    User getByPhone(String telephone);

    List<User> searchByName(String name);

    List<User> searchByTelephone(String telephone);

    void deleteByTelephone(String telephone);

    List<User> list();
}