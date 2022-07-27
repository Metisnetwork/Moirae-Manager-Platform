package com.datum.platform.service;

import com.datum.platform.mapper.domain.Publicity;
import com.datum.platform.mapper.enums.DataSyncTypeEnum;
import com.datum.platform.service.dto.sys.PlatONPropertiesDto;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public interface SysService {
    PlatONPropertiesDto getPlatONProperties();

    List<Publicity> listNeedSyncPublicity();

    void batchUpdatePublicity(List<Publicity> updateList);

    /**
     * 从数据中心到platform数据同步
     *
     * @param dataType              需要同步的类型 {@link DataSyncTypeEnum}
     * @param dataTypeDesc          需要同步的类型的描述
     * @param grpcFunction          消费同步时间，调用grpc接口返回数据列表List<T>
     * @param bizProcessor          消费数据列表List<T>
     * @param latestSyncedGenerator 生成新的同步时间
     * @param <T>                   grpc接口返回的数据类型
     */
    <T> void syncFromDc(String dataType,
                        String dataTypeDesc,
                        Function<Long, List<T>> grpcFunction,
                        Consumer<List<T>> bizProcessor,
                        Function<List<T>,Long> latestSyncedGenerator);

    /**
     * 从数据中心到platform数据同步
     *
     * @param dataType              需要同步的类型 {@link DataSyncTypeEnum}
     * @param dataTypeDesc          需要同步的类型的描述
     * @param dbFunction            消费同步时间，调用grpc接口返回数据列表List<T>
     * @param bizProcessor          消费数据列表List<T>
     * @param <T>                   grpc接口返回的数据类型
     */
    <T> void sync(String dataType,
                  String dataTypeDesc,
                  BiFunction<Long, Long, List<T>> dbFunction,
                  Function<List<T>,Long>  bizProcessor);
}
