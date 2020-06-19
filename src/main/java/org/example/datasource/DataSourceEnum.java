package org.example.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 支持的数据源
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum  DataSourceEnum {

    A("A", "A库"),
    B("B", "B 库"),
    ;

    private String datasource;
    private String desc;

}
