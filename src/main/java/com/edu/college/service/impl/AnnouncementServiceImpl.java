package com.edu.college.service.impl;

import com.edu.college.dao.AnnouncementMapper;
import com.edu.college.pojo.Announcement;
import com.edu.college.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    @Autowired
    private AnnouncementMapper mapper;

    @Override
    public void save(Announcement announcement) {
        mapper.insertSelective(announcement);
    }

    @Override
    public void update(Announcement announcement) {
        mapper.updateByPrimaryKeySelective(announcement);
    }

    @Override
    public void remove(List<Integer> ids) {
        mapper.deleteByIds(ids);
    }

    @Override
    public List<Announcement> query(Integer userId) {
        return mapper.selectByUserId(userId);
    }

    @Override
    public Announcement get(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }
}
