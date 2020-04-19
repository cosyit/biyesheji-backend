package com.edu.college.controller;

import com.edu.college.common.annotations.LoginRequire;
import com.edu.college.common.ret.Response;
import com.edu.college.pojo.Achievement;
import com.edu.college.pojo.Announcement;
import com.edu.college.pojo.User;
import com.edu.college.pojo.dto.AchievementDTO;
import com.edu.college.pojo.dto.ReviewDTO;
import com.edu.college.pojo.vo.AchievementVO;
import com.edu.college.service.AchievementService;
import com.edu.college.service.AnnouncementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("achievement")
@Api(tags = "我的成果模块")
public class AchievementController {
    @Autowired
    private AchievementService service;
    @Autowired
    private AnnouncementService announcementService;

    @PostMapping
    @LoginRequire
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "项目标题"),
            @ApiImplicitParam(name = "number", value = "项目编号/获奖题目"),
            @ApiImplicitParam(name = "firstAuthor", value = "第一作者/完成人"),
            @ApiImplicitParam(name = "department", value = "所属单位"),
            @ApiImplicitParam(name = "college", value = "学校署名"),
            @ApiImplicitParam(name = "subject", value = "一级学科"),
            @ApiImplicitParam(name = "categories", value = "学课门类"),
            @ApiImplicitParam(name = "publishType", value = "刊物/著作类型/获奖类别"),
            @ApiImplicitParam(name = "publishArea", value = "出版地/发证机关"),
            @ApiImplicitParam(name = "publishTime", value = "发出/出版/获奖日期"),
            @ApiImplicitParam(name = "publishScope", value = "发布/出版范围/单位/获奖级别"),
            @ApiImplicitParam(name = "wordCount", value = "字数(万)/项目经费(万) 获奖人数"),
            @ApiImplicitParam(name = "translation", value = "是否译文"),
            @ApiImplicitParam(name = "language", value = "语种"),
            @ApiImplicitParam(name = "cnIssn", value = "CN或ISSN号"),
            @ApiImplicitParam(name = "isbn", value = "ISBN号"),
            @ApiImplicitParam(name = "type", value = "成果类型 1/4"),
            @ApiImplicitParam(name = "authors", value = "作者信息数组"),
            @ApiImplicitParam(name = "seq", value = "作者顺序，不可重复"),
            @ApiImplicitParam(name = "authorName", value = "作者姓名"),
            @ApiImplicitParam(name = "gender", value = "性别0其他 1男 2女"),
            @ApiImplicitParam(name = "department", value = "所属单位"),
            @ApiImplicitParam(name = "contribution", value = "贡献率，相加之和为100"),
            @ApiImplicitParam(name = "attachments", value = "附件地址数组"),
    })
    public Response add(@ApiIgnore User user, @RequestBody AchievementDTO dto) {
        service.add(dto, user);
        return Response.success();
    }

    @DeleteMapping("{id}")
    @LoginRequire
    @ApiOperation("删除自己的成果")
    @ApiImplicitParam(name = "id", value = "成果的id")
    public Response remove(@ApiIgnore User user, @PathVariable final Integer id) {
        service.remove(user.getId(), id);
        return Response.success();
    }

    @GetMapping("self")
    @LoginRequire
    @ApiOperation("查询自己的成果")
    public Response list(@ApiIgnore User user) {
        final List<Achievement> achievements = service.list(user.getId());
        return Response.success(achievements);
    }

    @GetMapping("list")
    @LoginRequire("系主任")
    @ApiOperation("系主任查看所有成果")
    public Response list() {
        final List<Achievement> achievements = service.list(null);
        return Response.success(achievements);
    }

    @GetMapping("{id}")
    @LoginRequire
    @ApiOperation("查看成果详情，如果返回的的id为空，代表是自己的成果，不可被审核")
    public Response get(@ApiIgnore User user, @PathVariable Integer id) {
        final AchievementVO achievement = service.get(id);
        if (user.getId().equals(achievement.getUserId())) {
            achievement.setId(null);
        }
        return Response.success(achievement);
    }

    @PutMapping("{id}")
    @LoginRequire("系主任")
    @ApiOperation("系主任审核成果")
    public Response review(@ApiIgnore User user, @PathVariable Integer id, @RequestBody ReviewDTO review) {
        final AchievementVO achievement = service.get(id);
        Assert.isTrue(!Objects.equals(achievement.getUserId(), user.getId()), "不可审核自己的成果");
        service.review(id, user.getId(), review);
        // 生成一条被审核的通知
        final Announcement announcement = Announcement.builder()
                .title("你的成果已被审核")
                .content("你的成果" + (review.getStatus() == 1 ? "已通过" : "未通过") + "审核")
                .fromUserId(user.getId())
                .toUserId(achievement.getUserId())
                .build();
        announcementService.save(announcement);
        return Response.success();
    }
}