package org.example.service.impl;

import org.example.domain.TestBTable;
import org.example.mapper.TableBMapper;
import org.example.service.ServiceB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class ServiceBImpl implements ServiceB {

    @Autowired
    private TableBMapper tableBMapper;

    @Override
    public int save(long id) {
        return tableBMapper.save(id);
    }

    @Override
    public List<TestBTable> select() {
        return tableBMapper.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int save2(long id) {
        return tableBMapper.save(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<TestBTable> select2() {
        return tableBMapper.findAll();
    }

    @Override
    public List<TestBTable> selectWithThread() {
        Object[] objects = new Object[1];
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            // mapper 才是真正执行 org.example.datasource.DynamicDataSource.determineCurrentLookupKey 的代码
            List<TestBTable>  result = tableBMapper.findAll();
            objects[0] = result;
            countDownLatch.countDown();
        }).start();
        try {
            countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
        return (List<TestBTable>) objects[0];
    }
}
