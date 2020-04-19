package com.edu.college.common.aspect;

import com.edu.college.common.ret.Response;
import com.edu.college.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * 1.对前端DTO进行参数校验[DTO 给出toString,方便日志输出。]。
 * 2.请求相关日志。
 * 3.调用时间--性能监控。
 * 4.并发处理。
 */


@Aspect
@Component
@Slf4j
public class CommonAspect {

    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.000");

    @Pointcut("(execution(* com.edu.college.controller.*Controller.*(..) ))")
    public void executeService() {
    }

    @Around("executeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object result;

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;

        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String ip = RequestUtil.getRemoteAddr(request);
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // 为Controller方法中的第一个User对象和SysUser对象注入当前的登陆对象
        final Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof User) {
                final User currentUser = RequestHolder.getCurrentUser();
                if (currentUser != null) {
                    args[i] = currentUser;
                }
                break;
            }
        }

        Map<String, String> map = RequestUtil.getAllParameter(request);

        log.info("请求开始，ip: {}, url: {}, method: {}, uri: {}, params: {}", ip, url, method, uri, map);

        long startTime;
        long endTime;
        //时间差值。
        long timeDifferenceValue = 0;
        try {
            startTime = System.currentTimeMillis();
            result = pjp.proceed(args);
            endTime = System.currentTimeMillis();
            timeDifferenceValue = (endTime - startTime);

        } catch (Exception e) {
            log.error(" System Exception : {} ,Error From Aspect", e.getMessage());
            result = Response.fail(e.getMessage());
            log.error("请求发生异常:{}", e.getMessage());
            log.error(uri, e);
        }
        double costTime = timeDifferenceValue / 1000D;
        String formatCostTime = DECIMAL_FORMAT.format(costTime);
        log.info("请求结束，Controller的返回值是:{},接口调用时间:{}s \n", result, formatCostTime);
        log.debug("  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
                request.getRequestURI(), Runtime.getRuntime().maxMemory() / 1024 / 1024, Runtime.getRuntime().totalMemory() / 1024 / 1024, Runtime.getRuntime().freeMemory() / 1024 / 1024,
                (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024);
        RequestHolder.add((Response) result);
        return result;
    }
}
