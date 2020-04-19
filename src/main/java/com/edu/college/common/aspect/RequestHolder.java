package com.edu.college.common.aspect;


import com.edu.college.common.ret.Response;
import com.edu.college.pojo.User;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("unused")
public class RequestHolder {
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }


    private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void add(User user) {
        userHolder.set(user);
    }

    public static User getCurrentUser() {
        return userHolder.get();
    }

    private static final ThreadLocal<Response> responseHolder = new ThreadLocal<>();

    public static void add(Response sysUser) {
        responseHolder.set(sysUser);
    }

    public static Response getCurrentResponse() {
        return responseHolder.get();
    }

    public static void remove() {
        userHolder.remove();
        requestHolder.remove();
        responseHolder.remove();
    }
}
