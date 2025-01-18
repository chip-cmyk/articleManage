package com.itheima.anno;

import com.itheima.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented//元注解
@Target({ElementType.FIELD})//元注解
@Retention(RetentionPolicy.RUNTIME)//元注解
@Constraint(validatedBy = {StateValidation.class})//指定校验逻辑所在的类

public @interface State {
    //提供校验失败后的提示信息
    String message() default "state只能是已发布/草稿";

    //指定分组
    Class<?>[] groups() default {};

    //负载
    Class<? extends Payload>[] payload() default {};

}
