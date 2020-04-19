package com.edu.college.dao;

import com.edu.college.pojo.Author;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Author record);

    int insertSelective(Author record);

    Author selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Author record);

    int updateByPrimaryKey(Author record);

    void insertList(List<Author> authors);

    void remove(Integer id);
}