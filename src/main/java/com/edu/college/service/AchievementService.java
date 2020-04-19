package com.edu.college.service;

import com.edu.college.pojo.Achievement;
import com.edu.college.pojo.User;
import com.edu.college.pojo.dto.AchievementDTO;
import com.edu.college.pojo.dto.ReviewDTO;
import com.edu.college.pojo.vo.AchievementVO;

import java.util.List;

public interface AchievementService {
    void add(AchievementDTO dto, final User user);

    void remove(Integer userId, Integer id);

    List<Achievement> list(Integer userId);

    AchievementVO get(Integer id);

    void review(Integer id, Integer userId, ReviewDTO review);
}
