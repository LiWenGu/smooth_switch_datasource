package org.example.service;

import org.example.datasource.DataSourceEnum;
import org.example.datasource.TargetDataSource;
import org.example.domain.TestATable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServiceA {

    @TargetDataSource(DataSourceEnum.A)
    int save(long id);

    @TargetDataSource(DataSourceEnum.A)
    List<TestATable> select();
}
