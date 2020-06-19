package org.example.datasource;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置
 */
@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
@EnableTransactionManagement
public class DataSourceConfig extends ApplicationObjectSupport {

    @Bean(initMethod = "init")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DruidDataSource newDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(initMethod = "init")
    @ConfigurationProperties(prefix = "spring.datasource.read")
    public DruidDataSource oldDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
     * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
     */
    @Bean
    @Primary
    public DynamicDataSource dataSource(@Qualifier("newDataSource") DataSource newDataSource,
                                        @Qualifier("oldDataSource") DataSource oldDataSource) {
        //按照目标数据源名称和目标数据源对象的映射存放在Map中
        Map<Object, Object> allDataSources = new HashMap<>();
        allDataSources.put(DataSourceEnum.A.getDatasource(), newDataSource);
        allDataSources.put(DataSourceEnum.B.getDatasource(), oldDataSource);

        //采用是想AbstractRoutingDataSource的对象包装多数据源
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(allDataSources);
        //设置默认的数据源，当拿不到数据源时，使用此配置
        dataSource.setDefaultTargetDataSource(newDataSource);
        return dataSource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DynamicDataSource dynamicDataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        return sqlSessionFactoryBean;
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
