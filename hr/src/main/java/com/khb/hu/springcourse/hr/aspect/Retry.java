package com.khb.hu.springcourse.hr.aspect;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface Retry {
    int times() default 3;
    long waitTime() default 1000;
}
