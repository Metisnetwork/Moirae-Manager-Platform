package com.datum.platform.grpc.dynamic;

import lombok.Data;

import java.util.List;

@Data
public class MetadataOptionCsv {
    //原始数据Id
    private String originId;
    //当前数据的完整路径
    private String dataPath;
    //数据的大小 (单位: byte)
    private Integer size;
    //数据的行数
    private Integer rows;
    //数据的列数
    private Integer columns;
    //表示数据是否含有标题行, true: 含有, false: 没有
    private Boolean hasTitle;
    //数据的列信息
    private List<CsvColumns> metadataColumns;

    @Data
    public static class CsvColumns{
        //列索引 (从 1 下标开始)
        Integer index;
        //列名
        String name;
        //列类型, (string/ bool/ float/ int)
        String type;
        //列大小 (单位: byte)
        Integer size;
        //列描述
        String comment;
    }
}
