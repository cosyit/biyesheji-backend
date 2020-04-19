package com.edu.college.common.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * <p>表明用户账户信息有改动，将改动同步到redis</p>
 * <p>必须处于登录状态</p>
 * <p>默认在接口出现异常时不进行更新</p>
 * <p>但是可以通过设置forceUpdate为true来强制更新</p>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface UpdateRequire {
    @AliasFor("value")
    boolean forceUpdate() default false;

    @AliasFor("forceUpdate")
    boolean value() default false;
}
