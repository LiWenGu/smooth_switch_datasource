
package org.example.datasource;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * 数据源切换切面
 */
@Slf4j
@Aspect
@Order(Integer.MIN_VALUE + 1)//保证该AOP在@Transactional之前执行
@Configuration
public class DataSourceAspect {

    /**
     * 建议在 service 层进行数据源切面
     * 当然你也可以在 manager 层，甚至 controller 层做数据源切换，但是不建议
     */
    @Pointcut("execution(* org.example.service.*..*(..))")
    public void dataSourcePointCut() {
    }

    /*
     * 通过连接点切入
     */
    @Before("dataSourcePointCut()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            String method = joinPoint.getSignature().getName();
            Object target = joinPoint.getTarget();
            Class<?>[] classz = target.getClass().getInterfaces();
            Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();

            // 如果有实现类
            if (classz.length != 0) {
                Method m = classz[0].getMethod(method, parameterTypes);
                // 优先拿实现类的方法上的注解
                if (m.isAnnotationPresent(TargetDataSource.class)) {
                    DataSourceEnum dataSourceEnum = m.getAnnotation(TargetDataSource.class).value();
                    DynamicDataSource.putDataSource(dataSourceEnum.getDatasource());
                } else if (classz[0].isAnnotationPresent(TargetDataSource.class)) {
                    // 否则拿实现类上的类注解
                    DataSourceEnum dataSourceEnum = classz[0].getAnnotation(TargetDataSource.class).value();
                    DynamicDataSource.putDataSource(dataSourceEnum.getDatasource());
                }
            } else {
                // 如果没有实现类
                Method m = target.getClass().getMethod(method, parameterTypes);
                // 优先拿类的方法上的注解
                if (m.isAnnotationPresent(TargetDataSource.class)) {
                    DataSourceEnum dataSourceEnum = m.getAnnotation(TargetDataSource.class).value();
                    DynamicDataSource.putDataSource(dataSourceEnum.getDatasource());
                } else if (target.getClass().isAnnotationPresent(TargetDataSource.class)) {
                    // 否则拿类上的类注解
                    DataSourceEnum dataSourceEnum = target.getClass().getAnnotation(TargetDataSource.class).value();
                    DynamicDataSource.putDataSource(dataSourceEnum.getDatasource());
                }
            }
        } catch (Throwable e) {
            log.error("数据源切面异常" + e.getMessage(), e);
        }
    }

    /**
     * 执行完切面后，将线程共享中的数据源名称清空
     */
    @After("dataSourcePointCut()")
    public void after(JoinPoint joinPoint) {
        DynamicDataSource.removeDataSource();
    }
}
