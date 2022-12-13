package com.high.springcloud.myhystrix.annotation;

import java.lang.annotation.*;

/**
 * 熔断切点注解
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FusePointcut {
}
