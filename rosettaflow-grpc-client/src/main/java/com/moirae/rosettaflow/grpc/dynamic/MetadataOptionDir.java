package com.moirae.rosettaflow.grpc.dynamic;

import lombok.Data;

import java.util.List;


@Data
public class MetadataOptionDir {
    //原始数据Id
    private String originId;
    //当前数据的完整路径
    private String dirPath;
    //当前目录的所有文件名列表
    private List<String> filePaths;
    //是否为最底层目录(即下面没有子目录), true: 是, false: 否
    private Boolean last;
    //当前目录的所有子目录信息
    private List<MetadataOptionDir> childs;
}
