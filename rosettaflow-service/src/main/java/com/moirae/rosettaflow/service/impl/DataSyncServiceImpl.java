package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.DataSyncTypeEnum;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.mapper.DataSyncMapper;
import com.moirae.rosettaflow.mapper.domain.DataSync;
import com.moirae.rosettaflow.service.IDataSyncService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Author liushuyu
 * @Date 2022/1/5 14:05
 * @Version
 * @Desc
 */

@Service
public class DataSyncServiceImpl extends ServiceImpl<DataSyncMapper, DataSync> implements IDataSyncService {


    @Override
    public DataSync getDataSyncByType(DataSyncTypeEnum typeEnum) {
        LambdaQueryWrapper<DataSync> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DataSync::getDataType, typeEnum.getDataType());
        return this.getOne(wrapper);
    }

    @Override
    public DataSync getDataSyncByType(String dataSyncType) {
        LambdaQueryWrapper<DataSync> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DataSync::getDataType, dataSyncType);
        return this.getOne(wrapper);
    }

    @Override
    public boolean updateDataSyncByType(DataSync dataSync) {
        LambdaUpdateWrapper<DataSync> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DataSync::getDataType, dataSync.getDataType());
        return update(dataSync, updateWrapper);
    }

    @Override
    public boolean insertDataSync(DataSync dataSync) {
        int insert = this.baseMapper.insert(dataSync);
        return insert > 0;
    }

    /**
     * @param dataType              需要同步的类型
     * @param grpcFunction          消费同步时间，调用grpc接口返回数据列表List<T>
     * @param bizProcessor          消费数据列表List<T>
     * @param latestSyncedGenerator 生成新的同步时间
     * @param <T>                   grpc接口返回的数据类型
     */
    @Override
    public <T> void sync(String dataType,
                         String dataTypeDesc,
                         Function<Long, List<T>> grpcFunction,
                         Consumer<List<T>> bizProcessor,
                         Function<List<T>, Long> latestSyncedGenerator) {
        //1.根据dataType同步类型获取新的同步时间DataSync
        DataSync dataSyncByType = this.getDataSyncByType(dataType);
        if (dataSyncByType == null) {//获取失败，则插入一条新的数据
            dataSyncByType = new DataSync();
            dataSyncByType.setDataType(dataType);
            dataSyncByType.setLatestSynced(0);
            dataSyncByType.setInfo(dataTypeDesc);
            this.insertDataSync(dataSyncByType);
        }
        long latestSynced = dataSyncByType.getLatestSynced();
        List<T> grpcResponseList;
        do {
            //2.根据新的同步时间latestSynced获取分页列表grpcResponseList
            grpcResponseList = grpcFunction.apply(latestSynced > 1000 ? latestSynced - 1000 : latestSynced );
            if (CollUtil.isEmpty(grpcResponseList)) {
                break;
            }

            //3.根据分页列表grpcResponseList实现实际业务逻辑
            bizProcessor.accept(grpcResponseList);

            //4.根据分页列表grpcResponseList获取最新的同步时间latestSynced
            latestSynced = latestSyncedGenerator.apply(grpcResponseList);
            //5.将最新的同步时间latestSynced存到数据库中
            dataSyncByType.setLatestSynced(latestSynced);
            this.updateDataSyncByType(dataSyncByType);
        } while (grpcResponseList.size() == GrpcConstant.PAGE_SIZE);//如果小于pageSize说明是最后一批了
    }

}
