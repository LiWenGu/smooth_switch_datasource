package org.example.datasource;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源实现类
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource{

    /**
     * 本地线程共享对象
     * 动态数据源持有者，负责利用ThreadLocal存取数据源名称
     */
    // private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 支持父子线程增强版，也可以使用自带的 InheritableThreadLocal，原理相似
     */
    private static final TransmittableThreadLocal<String> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void putDataSource(String name) {
        THREAD_LOCAL.set(name);
    }

    public static String getDataSource() {
        return THREAD_LOCAL.get();
    }

    public static void removeDataSource() {
        THREAD_LOCAL.remove();
    }

    /**
     * 数据源路由，此方用于产生要选取的数据源逻辑名称，在 mapper 执行时调用，而不是 service！
     */
    @Override
    protected Object determineCurrentLookupKey() {
        //从共享线程中获取数据源名称
        return getDataSource();
    }
}
