package org.example.service;

import org.example.datasource.DataSourceEnum;
import org.example.datasource.TargetDataSource;
import org.example.domain.TestBTable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServiceB {

    @TargetDataSource(DataSourceEnum.B)
    int save(long id);

    @TargetDataSource(DataSourceEnum.B)
    List<TestBTable> select();

    @TargetDataSource(DataSourceEnum.B)
    int save2(long id);

    @TargetDataSource(DataSourceEnum.B)
    List<TestBTable> select2();

    @TargetDataSource(DataSourceEnum.B)
    List<TestBTable> selectWithThread();
}
