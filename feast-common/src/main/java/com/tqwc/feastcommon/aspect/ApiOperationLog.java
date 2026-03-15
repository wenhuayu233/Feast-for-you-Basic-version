package com.tqwc.feastcommon.aspect;

/**
 * @author Tang
 * @data 2026/3/12 11:24
 */

import java.lang.annotation.*;

/**
 * 自定义注解类
 */

@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface ApiOperationLog {
    String value() default "";
}
