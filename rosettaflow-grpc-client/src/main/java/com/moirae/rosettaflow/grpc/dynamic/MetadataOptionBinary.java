package com.moirae.rosettaflow.grpc.dynamic;

import lombok.Data;

import java.util.List;


@Data
public class MetadataOptionBinary {
    //原始数据Id
    private String originId;
    //当前数据的完整路径
    private String dataPath;
    //数据的大小 (单位: byte)
    private Integer size;
}
