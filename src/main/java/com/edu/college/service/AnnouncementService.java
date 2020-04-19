package com.edu.college.service;

import com.edu.college.pojo.Announcement;

import java.util.List;

public interface AnnouncementService {
    void save(Announcement announcement);

    void update(Announcement announcement);

    void remove(List<Integer> id);

    List<Announcement> query(Integer userId);

    Announcement get(Integer id);
}
