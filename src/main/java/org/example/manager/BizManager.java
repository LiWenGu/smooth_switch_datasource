package org.example.manager;

public interface BizManager {

    void create();

    void createWithTransaction();

    void select();

    void selectWithTransaction();

    void selectWithTransaction2();

    void selectWithThread();
}
