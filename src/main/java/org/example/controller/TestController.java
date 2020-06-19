package org.example.controller;

import org.example.datasource.DynamicDataSource;
import org.example.manager.BizManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private BizManager bizManager;

    @GetMapping("/create")
    public void create() {
        bizManager.create();
    }

    @GetMapping("/createWithTransaction")
    public void createWithTransaction() {
        bizManager.createWithTransaction();
    }

    @GetMapping("/select")
    public void select() {
        bizManager.select();
    }

    @GetMapping("/selectWithTransaction")
    public void selectWithTransaction() {
        bizManager.selectWithTransaction();
    }

    @GetMapping("/selectWithTransaction2")
    public void selectWithTransaction2() {
        bizManager.selectWithTransaction2();
    }

    /**
     * 此方法根据 {@link DynamicDataSource#THREAD_LOCAL} 的不同而产生不同的结果
     */
    @GetMapping("/selectWithThread")
    public void selectWithThread() {
        bizManager.selectWithThread();
    }
}
