package com.edu.college.service.impl;

import com.edu.college.dao.UserMapper;
import com.edu.college.pojo.User;
import com.edu.college.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper mapper;

    @Override
    public User getByPhone(final String telephone) {
        return mapper.getByPhone(telephone);
    }

    @Override
    public List<String> getRoles(final Integer id) {
        return mapper.getRoles(id);
    }

    @Override
    public void update(final User user) {
        mapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<User> searchByName(final String name) {
        return mapper.searchByName(name);
    }

    @Override
    public List<User> searchByTelephone(final String telephone) {
        return mapper.searchByTelephone(telephone);
    }

    @Override
    public void save(final User user) {
        try {
            mapper.insertSelective(user);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("已经有相同的手机号了");
        }
    }

    @Override
    public void removeByTelephone(final String telephone) {
        mapper.deleteByTelephone(telephone);
    }

    @Override
    public List<User> list() {

        return mapper.list();
    }
}
