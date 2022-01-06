package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.common.enums.DataSyncTypeEnum;
import com.moirae.rosettaflow.mapper.domain.DataSync;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Author liushuyu
 * @Date 2022/1/5 14:05
 * @Version
 * @Desc
 */
public interface IDataSyncService extends IService<DataSync> {
    DataSync getDataSyncByType(DataSyncTypeEnum typeEnum);

    DataSync getDataSyncByType(String dataSyncType);

    boolean updateDataSyncByType(DataSync dataSync);

    boolean insertDataSync(DataSync dataSync);

    /**
     * @param dataType              需要同步的类型
     * @param grpcFunction          消费同步时间，调用grpc接口返回数据列表List<T>
     * @param bizProcessor          消费数据列表List<T>
     * @param latestSyncedGenerator 生成新的同步时间
     * @param <T>                   grpc接口返回的数据类型
     */
    <T> void sync(String dataType,
                  Function<Long, List<T>> grpcFunction,
                  Consumer<List<T>> bizProcessor,
                  Function<List<T>,Long> latestSyncedGenerator);
}