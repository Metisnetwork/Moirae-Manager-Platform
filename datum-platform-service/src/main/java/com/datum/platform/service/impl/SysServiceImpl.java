package com.datum.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.datum.platform.chain.platon.config.PlatONProperties;
import com.datum.platform.grpc.constant.GrpcConstant;
import com.datum.platform.manager.DataSyncManager;
import com.datum.platform.manager.PublicityManager;
import com.datum.platform.mapper.domain.DataSync;
import com.datum.platform.mapper.domain.Proposal;
import com.datum.platform.mapper.domain.Publicity;
import com.datum.platform.service.SysService;
import com.datum.platform.service.dto.sys.PlatONPropertiesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Service
public class SysServiceImpl implements SysService {

    @Resource
    private PlatONProperties platONProperties;
    @Resource
    private PublicityManager publicityManager;
    @Resource
    private DataSyncManager dataSyncManager;


    @Override
    public PlatONPropertiesDto getPlatONProperties() {
        PlatONPropertiesDto result = new PlatONPropertiesDto();
        result.setChainId(platONProperties.getChainId());
        result.setChainName(platONProperties.getChainName());
        result.setRpcUrl(platONProperties.getRpcUrl());
        result.setSymbol(platONProperties.getSymbol());
        result.setBlockExplorerUrl(platONProperties.getBlockExplorerUrl());
        result.setDatumNetworkPayAddress(platONProperties.getDatumNetworkPayAddress());
        result.setUniswapV2Router02(platONProperties.getUniswapV2Router02());
        result.setDexUrl(platONProperties.getDexUrl());
        result.setTofunftUrl(platONProperties.getTofunftUrl());
        return result;
    }

    @Override
    public List<Publicity> listNeedSyncPublicity() {
        return publicityManager.listNeedSync();
    }

    @Override
    public void batchUpdatePublicity(List<Publicity> updateList) {
        publicityManager.updateBatchById(updateList);
    }



    /**
     * @param dataType              需要同步的类型
     * @param grpcFunction          消费同步时间，调用grpc接口返回数据列表List<T>
     * @param bizProcessor          消费数据列表List<T>
     * @param latestSyncedGenerator 生成新的同步时间
     * @param <T>                   grpc接口返回的数据类型
     */
    @Override
    public <T> void syncFromDc(String dataType,
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

    @Override
    public <T> void sync(String dataType, String dataTypeDesc, BiFunction<Long, Long, List<T>> dbFunction, Function<List<T>, Long> bizProcessor) {
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
            grpcResponseList = dbFunction.apply(latestSynced, GrpcConstant.PAGE_SIZE);
            if (CollUtil.isEmpty(grpcResponseList)) {
                break;
            }

            //3.根据分页列表grpcResponseList实现实际业务逻辑
            latestSynced = bizProcessor.apply(grpcResponseList);

            //5.将最新的同步时间latestSynced存到数据库中
            dataSyncByType.setLatestSynced(latestSynced);
            this.updateDataSyncByType(dataSyncByType);
        } while (grpcResponseList.size() == GrpcConstant.PAGE_SIZE);//如果小于pageSize说明是最后一批了
    }

    private DataSync getDataSyncByType(String dataSyncType) {
        return dataSyncManager.getOneByType(dataSyncType);
    }

    private boolean updateDataSyncByType(DataSync dataSync) {
        LambdaUpdateWrapper<DataSync> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DataSync::getDataType, dataSync.getDataType());
        return dataSyncManager.update(dataSync, updateWrapper);
    }

    private boolean insertDataSync(DataSync dataSync) {
        return dataSyncManager.save(dataSync);
    }
}
