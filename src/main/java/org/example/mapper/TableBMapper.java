package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.domain.TestBTable;

import java.util.List;

@Mapper
public interface TableBMapper {

    @Select("SELECT * FROM test_b_table")
    List<TestBTable> findAll();

    @Insert("INSERT INTO test_b_table(id) VALUES(#{id})")
    int save(@Param("id") long id);
}
