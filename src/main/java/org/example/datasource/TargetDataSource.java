
package org.example.datasource;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface TargetDataSource
{
    /**
     * 数据源名称
     * @return
     */
    DataSourceEnum value();
}