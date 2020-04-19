package com.edu.college.service.impl;

import com.edu.college.dao.AchievementMapper;
import com.edu.college.dao.AttachmentMapper;
import com.edu.college.dao.AuthorMapper;
import com.edu.college.pojo.Achievement;
import com.edu.college.pojo.Attachment;
import com.edu.college.pojo.User;
import com.edu.college.pojo.dto.AchievementDTO;
import com.edu.college.pojo.dto.ReviewDTO;
import com.edu.college.pojo.vo.AchievementVO;
import com.edu.college.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementServiceImpl implements AchievementService {
    @Autowired
    private AchievementMapper mapper;
    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private AttachmentMapper attachmentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(AchievementDTO dto, final User user) {
        final Achievement achievement = Achievement.of(dto);
        achievement.setUserId(user.getId());
        mapper.insertSelective(achievement);
        dto.getAuthors().forEach(author -> author.setAchievementId(achievement.getId()));
        authorMapper.insertList(dto.getAuthors());
        final List<Attachment> attachments = dto.getAttachments().stream().map(s -> new Attachment(achievement.getId(), s.getUrl(), s.getFilename())).collect(Collectors.toList());
        if (attachments.size() > 0) {
            attachmentMapper.insertList(attachments);
        }
    }

    @Override
    public void remove(final Integer userId, final Integer id) {
        final int delete = mapper.deleteByPrimaryKey(id);
        Assert.isTrue(delete > 0, "删除失败，成果不否存在或非成果提交者");
        attachmentMapper.remove(id);
        authorMapper.remove(id);
    }

    @Override
    public List<Achievement> list(final Integer userId) {
        return mapper.selectByUserId(userId);
    }

    @Override
    public AchievementVO get(final Integer id) {
        return mapper.get(id);
    }

    @Override
    public void review(final Integer id, final Integer userId, final ReviewDTO review) {
        mapper.review(id, userId, review);
    }

}
