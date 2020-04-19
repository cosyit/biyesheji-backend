package com.edu.college.controller;

import com.edu.college.common.annotations.LoginRequire;
import com.edu.college.common.annotations.UpdateRequire;
import com.edu.college.common.ret.Response;
import com.edu.college.common.util.verify.LoginToken;
import com.edu.college.pojo.User;
import com.edu.college.pojo.dto.LoginDTO;
import com.edu.college.pojo.dto.UserDTO;
import com.edu.college.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("user")
@Api(tags = "用户模块")
public class UserController {
    @Autowired
    private LoginToken loginToken;
    @Autowired
    private UserService service;

    @ApiOperation("用户登录 返回token和用户拥有的角色")
    @PostMapping("login")
    public Response login(@RequestBody LoginDTO login) {
        final String telephone = login.getTelephone();
        final User user = service.getByPhone(telephone);
        Assert.notNull(user, "用户不存在");
        final List<String> roles = service.getRoles(user.getId());
        Assert.isTrue(Objects.equals(login.getPassword(), user.getPassword()), "账户或密码不正确");
        final Map<String, Object> data = new HashMap<>(2);
        data.put("X-Authentication-Token", loginToken.saveAndGetToken(user));
        data.put("roles", roles);
        return Response.success(data);
    }

    @ApiOperation("查看其他用户的信息，需要管理员登陆")
    @GetMapping("{telephone}")
    @LoginRequire(requireRoles = "管理员")
    public Response information(@PathVariable final String telephone) {
        final User user = service.getByPhone(telephone);
        return Response.success(user);
    }

    @ApiOperation("用户查看自己的信息，需要登陆")
    @GetMapping
    @LoginRequire
    public Response information(@ApiIgnore User user) {
        return Response.success(user);
    }

    @LoginRequire
    @UpdateRequire
    @PutMapping
    @ApiOperation("用户修改自己的信息，需要登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名，可选"),
            @ApiImplicitParam(name = "gender", value = "性别，可选"),
            @ApiImplicitParam(name = "age", value = "年龄，可选"),
            @ApiImplicitParam(name = "newTelephone", value = "手机号，可选"),
            @ApiImplicitParam(name = "password", value = "密码，可选")
    })
    public Response update(@ApiIgnore User user, @RequestBody UserDTO newUser) {
        updateUser(newUser, user);
        service.update(user);
        return Response.success(user);
    }

    @LoginRequire("管理员")
    @ApiOperation("查询所有用户，需要管理员登录")
    @GetMapping("list")
    public Response list() {
        List<User> users = service.list();
        return Response.success(users);
    }

    @LoginRequire("管理员")
    @UpdateRequire
    @PutMapping("{telephone}")
    @ApiOperation("管理员修改其他用户的信息，需要登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名，可选"),
            @ApiImplicitParam(name = "gender", value = "性别，可选"),
            @ApiImplicitParam(name = "age", value = "年龄，可选"),
            @ApiImplicitParam(name = "newTelephone", value = "手机号，可选"),
            @ApiImplicitParam(name = "password", value = "密码，可选"),
            @ApiImplicitParam(name = "telephone", value = "被修改用户的手机号，必选"),
    })
    public Response update(@RequestBody UserDTO newUser, @PathVariable String telephone) {
        final User user = service.getByPhone(telephone);
        Assert.notNull(user, "找不到用户");
        updateUser(newUser, user);
        service.update(user);
        return Response.success(user);
    }

    private void updateUser(@RequestBody final UserDTO newUser, final User user) {
        if (StringUtils.isNotBlank(newUser.getName())) {
            user.setName(newUser.getName());
        }
        if (Objects.nonNull(newUser.getAge())) {
            user.setAge(newUser.getAge());
        }
        if (Objects.nonNull(newUser.getGender())) {
            user.setGender(newUser.getGender());
        }
        if (Objects.nonNull(newUser.getNewTelephone())) {
            user.setTelephone(newUser.getNewTelephone());
        }
        if (Objects.nonNull(newUser.getPassword())) {
            user.setPassword(newUser.getPassword());
        }
    }

    @PostMapping
    @ApiOperation("添加用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名，可选"),
            @ApiImplicitParam(name = "gender", value = "性别，可选"),
            @ApiImplicitParam(name = "age", value = "年龄，可选"),
            @ApiImplicitParam(name = "newTelephone", value = "手机号，可选"),
            @ApiImplicitParam(name = "password", value = "密码，可选"),
    })
    @LoginRequire("管理员")
    public Response add(@RequestBody UserDTO dto) {
        final User user = User.builder().age(dto.getAge()).gender(dto.getGender()).name(dto.getName()).telephone(dto.getNewTelephone()).password(dto.getPassword()).build();
        service.save(user);
        return Response.success();
    }

    @DeleteMapping("{telephone}")
    @ApiOperation("删除用户")
    @LoginRequire("管理员")
    @ApiImplicitParam(name = "telephone", value = "被删除用户的手机号")
    public Response remove(@PathVariable final String telephone) {
        service.removeByTelephone(telephone);
        return Response.success();
    }

    @GetMapping("search/name/{name}")
    @ApiOperation("通过名称搜索")
    @LoginRequire
    @ApiImplicitParam(name = "name", value = "搜索关键字")
    public Response searchByName(@PathVariable final String name) {
        final List<User> users = service.searchByName(name);
        return Response.success(users);
    }

    @GetMapping("search/telephone/{telephone}")
    @ApiOperation("通过手机号搜索")
    @LoginRequire
    @ApiImplicitParam(name = "telephone", value = "搜索关键字")
    public Response search(@PathVariable final String telephone) {
        final List<User> users = service.searchByTelephone(telephone);
        return Response.success(users);
    }
}