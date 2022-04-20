package com.moirae.rosettaflow.service;

import com.moirae.rosettaflow.mapper.domain.DataSync;
import com.moirae.rosettaflow.mapper.enums.DataSyncTypeEnum;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Author liushuyu
 * @Date 2022/1/5 14:05
 * @Version
 * @Desc
 */
public interface DataSyncService{

    DataSync getDataSyncByType(String dataSyncType);

    boolean updateDataSyncByType(DataSync dataSync);

    boolean insertDataSync(DataSync dataSync);

    /**
     * @param dataType              需要同步的类型 {@link DataSyncTypeEnum}
     * @param dataTypeDesc          需要同步的类型的描述
     * @param grpcFunction          消费同步时间，调用grpc接口返回数据列表List<T>
     * @param bizProcessor          消费数据列表List<T>
     * @param latestSyncedGenerator 生成新的同步时间
     * @param <T>                   grpc接口返回的数据类型
     */
    <T> void sync(String dataType,
                  String dataTypeDesc,
                  Function<Long, List<T>> grpcFunction,
                  Consumer<List<T>> bizProcessor,
                  Function<List<T>,Long> latestSyncedGenerator);
}
