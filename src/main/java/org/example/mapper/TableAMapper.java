package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.domain.TestATable;

import java.util.List;

@Mapper
public interface TableAMapper {

    @Select("SELECT * FROM test_a_table")
    List<TestATable> findAll();

    @Insert("INSERT INTO test_a_table(id) VALUES(#{id})")
    int save(@Param("id") long id);
}
