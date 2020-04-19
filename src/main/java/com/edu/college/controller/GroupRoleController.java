package com.edu.college.controller;

import com.edu.college.common.annotations.LoginRequire;
import com.edu.college.common.ret.Response;
import com.edu.college.pojo.Group;
import com.edu.college.pojo.Role;
import com.edu.college.service.GroupService;
import com.edu.college.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("groupAndRole")
@Api(tags = "权限管理，需要 管理员 角色登录")
public class GroupRoleController {
    @Autowired
    private GroupService groupService;
    @Autowired
    private RoleService roleService;

    @GetMapping("group")
    @LoginRequire("管理员")
    @ApiOperation("查询所有的组")
    public Response getGroups() {
        final List<Group> groups = groupService.groups(null);
        return Response.success(groups);
    }

    @GetMapping("user/group/{userId}")
    @LoginRequire("管理员")
    @ApiOperation("查询用户所在的组")
    public Response getUserGroups(@PathVariable final Integer userId) {
        final List<Group> groups = groupService.groups(userId);
        return Response.success(groups);
    }

    @GetMapping("group/user/{groupId}")
    @LoginRequire("管理员")
    @ApiOperation("查询某个组下所有的用户")
    public Response getUsersInGroup(@PathVariable final Integer groupId) {
        final List<Group> groups = groupService.groups(groupId);
        return Response.success(groups);
    }

    @GetMapping("role")
    @LoginRequire("管理员")
    @ApiOperation("查询所有的角色")
    public Response getRoles() {
        final List<Group> groups = roleService.roles(null);
        return Response.success(groups);
    }

    @GetMapping("group/role/{groupId}")
    @LoginRequire("管理员")
    @ApiOperation("查询组下所有的角色")
    public Response getGroupRoles(@PathVariable final Integer groupId) {
        final List<Group> groups = roleService.roles(groupId);
        return Response.success(groups);
    }

    @GetMapping("user/role/{userId}")
    @LoginRequire("管理员")
    @ApiOperation("查询用户拥有的所有角色")
    public Response getUserRoles(@PathVariable final Integer userId) {
        final List<Role> roles = roleService.userRoles(userId);
        return Response.success(roles);
    }

    @PostMapping("group/{name}")
    @LoginRequire("管理员")
    @ApiOperation("新建组")
    public Response addGroup(@PathVariable String name) {
        groupService.save(Group.builder().name(name).build());
        return Response.success();
    }

    @DeleteMapping("group/{name}")
    @LoginRequire("管理员")
    @ApiOperation("删除组")
    public Response removeGroup(@PathVariable String name) {
        groupService.delete(name);
        return Response.success();
    }

    @PutMapping("group/{name}/{newName}")
    @LoginRequire("管理员")
    @ApiOperation("重命名组")
    public Response renameGroup(@PathVariable String name, @PathVariable final String newName) {
        groupService.update(name, newName);
        return Response.success();
    }

    @PostMapping("role/{name}")
    @LoginRequire("管理员")
    @ApiOperation("新增角色")
    public Response addRole(@PathVariable final String name) {
        roleService.save(Role.builder().name(name).build());
        return Response.success();
    }

    @DeleteMapping("role/{name}")
    @LoginRequire("管理员")
    @ApiOperation("删除角色")
    public Response removeRole(@PathVariable final String name) {
        roleService.delete(name);
        return Response.success();
    }

    @PutMapping("role/{name}/{newName}")
    @LoginRequire("管理员")
    @ApiOperation("重命名角色")
    public Response renameRole(@PathVariable final String name, @PathVariable final String newName) {
        roleService.update(name, newName);
        return Response.success();
    }

    @PutMapping("group/role/{groupId}/{roleIds}")
    @LoginRequire("管理员")
    @ApiOperation("为组重新分配角色")
    public Response assignRole(@PathVariable final Integer groupId, @PathVariable final List<Integer> roleIds) {
        groupService.assignRole(groupId, roleIds);
        return Response.success();
    }

    @PutMapping("user/group/{userId}/{groupIds}")
    @LoginRequire("管理员")
    @ApiOperation("为用户重新分配组")
    public Response assignGroup(@PathVariable final Integer userId, @PathVariable final List<Integer> groupIds) {
        groupService.assignGroup(userId, groupIds);
        return Response.success();
    }
}
