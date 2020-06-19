package org.example.service.impl;

import org.example.domain.TestATable;
import org.example.mapper.TableAMapper;
import org.example.service.ServiceA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceAImpl implements ServiceA {

    @Autowired
    private TableAMapper tableAMapper;

    @Override
    public int save(long id) {
        return tableAMapper.save(id);
    }

    @Override
    public List<TestATable> select() {
        return tableAMapper.findAll();
    }
}
