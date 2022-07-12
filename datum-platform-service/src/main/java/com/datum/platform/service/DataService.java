package com.datum.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.datum.platform.common.enums.DataOrderByEnum;
import com.datum.platform.mapper.domain.*;
import com.datum.platform.mapper.enums.MetaDataCertificateTypeEnum;
import com.datum.platform.mapper.enums.MetaDataFileTypeEnum;
import com.datum.platform.service.dto.data.UserWLatCredentialDto;

import java.util.List;

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
     * @param keyword
     * @return
     */
    IPage<MetaData> getUserDataList(Long current, Long size, String identityId, String keyword);


    /**
     * 查询用户可授权的元数据列表
     *
     * @param current    页数
     * @param size       大小
     * @param keyword  凭证名称 或 数据名称
     * @return
     */
    IPage<MetaData> getUserAuthDataList(Long current, Long size, String keyword);


    /**
     * 查询给定用户地址对应元数据的组织列表
     *
     * @param address
     * @return
     */
    List<String> listMetaDataOrgIdByUser(String address);

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
    List<String> listERC20TokenAddress();


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
     * 批量更新或保存
     *
     * @param saveList
     * @return
     */
    boolean saveOrUpdateBatchStatsToken(List<StatsMetaData> saveList);

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
     * 批量保存psi
     *
     * @param psiList
     * @return
     */
    boolean saveBatchPsi(List<Psi> psiList);

    /**
     * 批量保存元数据及token信息
     */
    void batchReplace(List<MetaData> metaDataList, List<MetaDataColumn> metaDataColumnList, List<Token> tokenList, List<MetaDataCertificate> metaDataCertificateList);

    /**
     * 获得用户的账户信息
     *
     * @return
     */
    UserWLatCredentialDto getUserWLatCredential();

    List<MetaDataColumn> listMetaDataColumnByIdAndIndex(String metaDataId, List<Integer> selectedColumnsV2);

    Model getModelByTaskId(String taskId);

    MetaDataCertificate getNoAttributeCredentialByMetaDataId(String metaDataId);

    MetaDataCertificate getNoAttributeCredentialByMetaDataIdAndUser(String metaDataId);

    IPage<MetaDataCertificate> pageHaveAttributesCertificateByMetaDataId(Long current, Long size, String metaDataId);

    IPage<MetaDataCertificate> pageHaveAttributesCertificateByMetaDataIdAndUser(Long current, Long size, String metaDataId);

    List<MetaDataCertificate> listHaveAttributesCertificateByMetaDataIdAndUser(String metaDataId);

    boolean isMetaDataOwner(String metaDataId);

    List<MetaDataCertificate> listMetaDataCertificateUser(List<Long> credentialIdList);

    /**
     * 查询元数据列表
     *
     * @return 对象中仅返回 metaDataId、erc721Address
     */
    List<MetaData> listMetaDataOfNeedSyncedMetaDataCertificate();

    boolean saveOrUpdateOrDeleteBatchMetaDataCertificate(String metaDataId, List<MetaDataCertificate> metaDataCertificateList);

    /**
     * 查询数据凭证列表
     *
     * @return 对象返回 id、type、token_address、token_id
     */
    List<MetaDataCertificate> listMetaDataCertificate();

    boolean existMetaDataCertificateUser(String address, Long metaDataCertificateId);

    boolean saveOrUpdateBatchMetaDataCertificateUser(String address, List<MetaDataCertificateUser> metaDataCertificateUserList);

    String getMetaDataCertificateName(MetaDataCertificateTypeEnum metaDataCertificateTypeEnum, String metaDataId, String consumeTokenAddress, String consumeTokenId);
}
