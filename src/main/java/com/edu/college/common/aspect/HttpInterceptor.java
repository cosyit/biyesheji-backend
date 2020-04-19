package com.edu.college.common.aspect;

import com.edu.college.common.annotations.LoginRequire;
import com.edu.college.common.annotations.UpdateRequire;
import com.edu.college.common.ret.Response;
import com.edu.college.common.util.json.JsonMapper;
import com.edu.college.common.util.redis.RedisUtil;
import com.edu.college.common.util.verify.LoginToken;
import com.edu.college.dao.UserMapper;
import com.edu.college.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

import static com.edu.college.common.constant.Constants.LOGIN_SENCONDS;

@Slf4j
@Component
public class HttpInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private LoginToken loginToken;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LoginRequire annotation;
        if (handler instanceof HandlerMethod) {
            log.info("访问控制器中的Action方法名称：{}", ((HandlerMethod) handler).getMethod().getName());
            annotation = ((HandlerMethod) handler).getMethodAnnotation(LoginRequire.class);
        } else {
            return true;
        }
        String authentication = request.getHeader("X-Authentication-Token");

        // 如果请求头不存在令牌，那么从参数中获取令牌。
        if (StringUtils.isBlank(authentication)) {
            authentication = request.getParameter("X-Authentication-Token");
        }
        // 有可能不需要登录的接口也能根据是否登录做出不同的动作
        User user = loginToken.getUser(authentication);
        RequestHolder.add(user);
        // @LoginRequire注解不存在，则不做登陆验证。
        if (annotation == null) {
            return true;
        }

        if (StringUtils.isBlank(authentication)) {
            Response fail = Response.fail("未登陆，请登陆后再访问该接口。");
            fail.setCode(403);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().print(JsonMapper.obj2String(fail));
            return false;
        }

//        Object user = loginToken.getUser(authentication);
        if (user == null) {
            //没有用户，继续登陆。
            Response fail = Response.fail("此 X-Authentication-Token 已失效，请重新登陆");
            fail.setCode(403);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().print(JsonMapper.obj2String(fail));
            return false;
        }
        if (annotation.requireRoles().length == 0) {
            return true;
        }

        final String[] requireRoles = annotation.requireRoles();
        final Method getId = user.getClass().getDeclaredMethod("getId");
        final Integer id = (Integer) getId.invoke(user);
        if (requireRoles.length > 0) {
            final List<String> roles = userMapper.getRoles(id);
            for (final String role : requireRoles) {
                if (!roles.contains(role)) {
                    Response fail = Response.fail(String.format("需要[%s]角色", role));
                    fail.setCode(402);
                    response.setContentType("application/json; charset=utf-8");
                    response.getWriter().print(JsonMapper.obj2String(fail));
                    return false;
                }
            }
        }
        //如果有用户，将用户和请求加入当前 请求线程。
        RequestHolder.add(request);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 如果方法上有@UpdateRequire注解，表明用户信息需要更新到redis中
        final Response currentResponse = RequestHolder.getCurrentResponse();
        if (handler instanceof HandlerMethod) {
            final UpdateRequire updateRequire = ((HandlerMethod) handler).getMethodAnnotation(UpdateRequire.class);
            if (updateRequire != null) {
                if (updateRequire.forceUpdate() || currentResponse.isRet()) {
                    final LoginRequire loginRequire = ((HandlerMethod) handler).getMethodAnnotation(LoginRequire.class);
                    // @LoginRequire注解不存在，则不做登陆验证。
                    Assert.notNull(loginRequire, "UpdateRequire注解必须和LoginRequire同时存在");
                    String authentication = request.getHeader("X-Authentication-Token");

                    // 如果请求头不存在令牌，那么从参数中获取令牌。
                    if (StringUtils.isBlank(authentication)) {
                        authentication = request.getParameter("X-Authentication-Token");
                    }
                    final User user = loginToken.getUser(authentication);
                    if (user != null) {
                        final Integer id = user.getId();
                        final User user0 = userMapper.selectByPrimaryKey(id);
                        redisUtil.putAndExpire(authentication, user0, LOGIN_SENCONDS);
                    }
                }
            }
        }

        //从用户线程中删除用户，在请求线程中删除请求。
        RequestHolder.remove();
    }

}
