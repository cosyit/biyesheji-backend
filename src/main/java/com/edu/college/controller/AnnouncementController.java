package com.edu.college.controller;

import com.edu.college.common.annotations.LoginRequire;
import com.edu.college.common.ret.Response;
import com.edu.college.pojo.Announcement;
import com.edu.college.pojo.User;
import com.edu.college.pojo.dto.AnnouncementDTO;
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

@RestController
@RequestMapping("announcement")
@Api(tags = "公告模块")
public class AnnouncementController {
    @Autowired
    private AnnouncementService service;

    @GetMapping("user")
    @ApiOperation("用户查看自己的通知")
    @LoginRequire
    public Response list(@ApiIgnore User user) {
        Assert.notNull(user.getId(), "请重新登录");
        final List<Announcement> list = service.query(user.getId());
        return Response.success(list);
    }

    @GetMapping
    @ApiOperation("管理员查看所有通知（管理员创建的）")
    @LoginRequire("管理员")
    public Response list() {
        final List<Announcement> list = service.query(null);
        return Response.success(list);
    }

    @PostMapping
    @ApiOperation("管理员新增通知")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题"),
            @ApiImplicitParam(name = "content", value = "标题"),
    })
    @LoginRequire("管理员")
    public Response add(@ApiIgnore User user, @RequestBody AnnouncementDTO announcement) {
        service.save(Announcement.builder().title(announcement.getTitle()).content(announcement.getContent()).fromUserId(user.getId()).build());
        return Response.success();
    }

    @DeleteMapping("{ids}")
    @ApiOperation("管理员删除通知")
    @ApiImplicitParam(name = "id", value = "通知的id，可多选，以逗号分割")
    @LoginRequire("管理员")
    public Response add(@PathVariable final List<Integer> ids) {
        service.remove(ids);
        return Response.success();
    }
}
