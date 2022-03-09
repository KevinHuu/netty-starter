package com.h.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * @author ：Hukaiwen
 * @description ：注册@Handler标签标识的类
 * @date ：2022/1/25 16:48
 */
public class HandlerScanRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

    private static final String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackages";
    private BeanFactory beanFactory;

    /**
     * @return : void
     * @author : Hukaiwen
     * @description : 扫描注解@HandlerScan在指定包路径下由@Handler注解的类
     * @date : 2022/2/22 13:51
     * @param : [org.springframework.core.type.AnnotationMetadata, org.springframework.beans.factory.support.BeanDefinitionRegistry]
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes handlerScanAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(HandlerScan.class.getName()));
        if (handlerScanAttrs != null && !handlerScanAttrs.isEmpty()) {
            final String[] handlerScanPackages = handlerScanAttrs.getStringArray(BASE_PACKAGE_ATTRIBUTE_NAME);
            final ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner(registry);
            classPathBeanDefinitionScanner.addIncludeFilter(new AnnotationTypeFilter(Handler.class));
            classPathBeanDefinitionScanner.scan(handlerScanPackages);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
