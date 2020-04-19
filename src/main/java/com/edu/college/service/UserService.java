package com.edu.college.service;

import com.edu.college.pojo.User;

import java.util.List;

public interface UserService {
    User getByPhone(String telephone);

    List<String> getRoles(Integer id);

    void update(User user);

    List<User> searchByName(String name);

    List<User> searchByTelephone(String telephone);

    void save(User user);

    void removeByTelephone(String telephone);

    List<User> list();
}
