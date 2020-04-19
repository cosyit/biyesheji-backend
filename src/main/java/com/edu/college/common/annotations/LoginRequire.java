package com.edu.college.common.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * NoAuth 注解的controller 不进行验证。
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LoginRequire {
    /**
     * <p>需要哪些角色</p>
     * * <p>优先级 2</p>
     */
    @AliasFor("value")
    String[] requireRoles() default {};

    @AliasFor("requireRoles")
    String[] value() default {};
}
