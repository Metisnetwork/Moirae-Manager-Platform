package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.common.enums.DataOrderByEnum;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.mapper.enums.MetaDataFileTypeEnum;
import com.moirae.rosettaflow.service.dto.data.MetisLatInfoDto;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DataService {

    /**
     * 元数据全网统计
     *
     * @return totalSize & totalCount
     */
    MetaData statisticsOfGlobal();

    /**
     * 查询元数据列表
     *
     * @param current  页数
     * @param size     大小
     * @param identityId  组织id
     * @return
     */
    IPage<MetaData> listMetaDataByOrg(Long current, Long size, String identityId);

    /**
     * 查询元数据列表
     *
     * @param current  页数
     * @param size     大小
     * @param keyword  凭证名称 或 元数据id
     * @param industry 行业类型
     * @param fileType 文件类型
     * @param minSize  文件大小的最小值
     * @param maxSize  文件大小的最大值
     * @param orderBy  排序方式
     * @return
     */
    IPage<MetaData> listMetaDataByCondition(Long current, Long size, String keyword, String industry, MetaDataFileTypeEnum fileType, Long minSize, Long maxSize, DataOrderByEnum orderBy);

    /**
     * 查询元数据信息
     *
     * @param metaDataId  元数据id
     * @param isNeedDetails  是否包含详情（数据列）
     * @return
     */
    MetaData getMetaDataById(String metaDataId, boolean isNeedDetails);

    /**
     * 查询元数据列表（用户存在余额的）
     *
     * @param current    页数
     * @param size       大小
     * @param identityId  组织id
     * @return
     */
    IPage<MetaData> getUserDataList(Long current, Long size, String identityId);

    /**
     *  查询给定元数据id对应元数据集合，返回map结构
     *
     * @param metaDataIdList  元数据id
     * @return  返回元数据包括token名称
     */
    Map<String, MetaData> getMetaDataId2MetaDataMap(Set<String> metaDataIdList);

    /**
     * 查询给定元数据id对应元数据列表
     *
     * @param metaDataIdList 元数据id
     * @return
     */
    List<MetaData> listMetaDataByIds(Set<String> metaDataIdList);

    /**
     * 查询给的token对应的元数据列表
     *
     * @param tokenAddress 凭证地址
     * @return
     */
    List<MetaData> listMetaDataByTokenAddress(String tokenAddress);

    /**
     * 查询给的元数据列
     *
     * @param metaDataId  元数据id
     * @param columnIndex 列索引
     * @return
     */
    MetaDataColumn getDataColumnByIds(String metaDataId, int columnIndex);

    /**
     * 查询所有token地址列表
     *
     * @return
     */
    List<String> listTokenId();

    /**
     * 查询token列表
     *
     * @param tokenIdList 地址列表
     * @return
     */
    List<Token> listTokenByIds(Collection<String> tokenIdList);

    /**
     * 查询待同步的token列表
     *
     * @param size
     * @return
     */
    List<Token> listTokenByNeedSyncedInfo(int size);

    /**
     * 查询token信息
     *
     * @param tokenAddress 地址
     * @return
     */
    Token getTokenById(String tokenAddress);

    /**
     * 查询Metis token信息
     *
     * @return
     */
    Token getMetisToken();

    /**
     * 保存token信息
     *
     * @param token 保存的token
     * @return
     */
    boolean saveToken(Token token);

    /**
     * 更新token信息
     *
     * @param token 更新的token
     * @return
     */
    boolean updateTokenById(Token token);

    /**
     * 查询用户持有的token信息
     *
     * @param tokenAddress 合约地址
     * @param userAddress  用户地址
     * @return
     */
    TokenHolder getTokenHolderById(String tokenAddress, String userAddress);

    /**
     * 批量更新或保存
     *
     * @param address
     * @param tokenHolderList
     * @return
     */
    boolean saveOrUpdateBatchTokenHolder(String address, List<TokenHolder> tokenHolderList);

    /**
     * 批量更新或保存
     *
     * @param saveList
     * @return
     */
    boolean saveOrUpdateBatchStatsToken(List<StatsToken> saveList);

    /**
     * 查询用户可用的模型列表
     *
     * @param algorithmId 算法id
     * @param identityId  组织
     * @return
     */
    List<Model> listModelByUser(Long algorithmId, String identityId);

    /**
     * 查询模型信息
     *
     * @param modelId 模型元数据id
     * @return
     */
    Model getModelById(String modelId);

    /**
     * 查询模型信息
     *
     * @param identity 模型所属组织
     * @param taskId  训练的任务id
     * @return
     */
    Model getModelByOrgAndTrainTaskId(String identity, String taskId);

    /**
     * 批量保存模型
     *
     * @param modelList
     * @return
     */
    boolean saveBatchModel(List<Model> modelList);

    /**
     * 查询psi文件列表
     *
     * @param taskId 生成psi文件的任务id
     * @return
     */
    List<Psi> listPsiByTrainTaskId(String taskId);

    /**
     * 批量保存psi
     *
     * @param psiList
     * @return
     */
    boolean saveBatchPsi(List<Psi> psiList);

    /**
     * 批量保存元数据及token信息
     *
     * @param metaDataList
     * @param metaDataColumnList
     * @param tokenList
     */
    void batchReplace(List<MetaData> metaDataList, List<MetaDataColumn> metaDataColumnList, List<Token> tokenList);

    /**
     * 获得用户的账户信息
     *
     * @return
     */
    MetisLatInfoDto getUserMetisLatInfo();
}
