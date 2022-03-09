package com.h.annotation;


import java.lang.annotation.*;

/**
 * @author      ：Hukaiwen
 * @description ：处理类标签
 * @date        ：2022/1/25 14:27
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface Handler {

    /**
     * 根据配置中的name标签进行约束配置
     */
    String value() default "";
}
