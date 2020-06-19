package org.example.manager.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.manager.BizManager;
import org.example.service.ServiceA;
import org.example.service.ServiceB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class BizManagerImpl implements BizManager {

    @Autowired
    private ServiceA serviceA;

    @Autowired
    private ServiceB serviceB;

    @Override
    public void create() {
        log.info("没有事务：两个库同时插入查找是正常的");
        log.info("A创建成功：" + serviceA.save(1L));
        log.info("此时的A库中有：" + serviceA.select());
        log.info("B创建成功：" + serviceB.save(2L));
        log.info("此时的B库中有：" + serviceB.select());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createWithTransaction() {
        log.info("有事务：两库切换出现异常");
        log.info("A创建成功：" + serviceA.save(10L));
        log.info("此时的A库中有：" + serviceA.select() + "，但是实际是没有入库的");
        log.error("B创建失败，找不到对应的表，说明是没有切换成功的，此时看A库发现数据10没有，说明A库是正常回滚的");
        log.info("B创建成功：" + serviceB.save(20L));
        log.info("此时的B库中有：" + serviceB.select());
    }

    @Override
    public void select() {
        log.info("此时的A库中有：" + serviceA.select());
        log.info("此时的B库中有：" + serviceB.select());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void selectWithTransaction() {
        log.info("此时的A库中有：" + serviceA.select());
        log.error("B查找失败，找不到对应的表，说明是没有切换成功的");
        log.info("此时的B库中有：" + serviceB.select());
    }

    /**
     * 事务内部强制切换数据源，只有第一个数据源会正常回滚，无关顺序。被强制切换数据源的 sql 是不会回滚的，
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void selectWithTransaction2() {
        log.info("有事务：两库切换正常，但是异常回滚只能回滚第一个库");
        log.info("A创建成功：" + serviceA.save(100L));
        log.info("此时的A库中有：" + serviceA.select() + "，但是实际是没有入库的");
        log.info("下面会触发：org.springframework.jdbc.datasource.DataSourceTransactionManager.doSuspend 事务管理器的挂起，从而实现的切面");
        log.info("B创建成功：" + serviceB.save2(200L));
        log.info("此时的B库中有：" + serviceB.select2());
        log.info("触发异常，只有A回滚，B是回滚不了的");
        log.info("A又创建成功：" + serviceA.save(101L));
        log.info("此时的A库中有：" + serviceA.select() + "，但是实际是没有入库的");
        int a = 1/0;
    }

    /**
     * 子线程，线程池的问题只会在切面的方法才会触发。因为切面是切的 sevice，因此触发条件是在 service 中使用子线程
     * 实际的场景可能是用 spring 异步，本质也是子线程
     */
    @Override
    public void selectWithThread() {
        log.info("会报错，找不到表，说明数据源切换失效");
        log.info("找到了：" + serviceB.selectWithThread());
    }
}
