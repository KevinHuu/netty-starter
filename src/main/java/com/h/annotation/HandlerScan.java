package com.h.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author      ：Hukaiwen
 * @description ：处理类扫描类
 * @date        ：2022/1/25 17:08
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Import(HandlerScanRegistrar.class)
public @interface HandlerScan {
    @AliasFor("basePackages")
    String[] value() default {};
    
    @AliasFor("value")
    String[] basePackages() default {};
}
