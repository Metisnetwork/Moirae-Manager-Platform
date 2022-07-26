package com.datum.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.chain.platon.contract.DataTokenTemplateContract;
import com.datum.platform.chain.platon.contract.IUniswapV2FactoryContract;
import com.datum.platform.common.enums.DataOrderByEnum;
import com.datum.platform.manager.*;
import com.datum.platform.mapper.domain.*;
import com.datum.platform.mapper.enums.MetaDataCertificateTypeEnum;
import com.datum.platform.mapper.enums.MetaDataFileTypeEnum;
import com.datum.platform.mapper.enums.TokenTypeEnum;
import com.datum.platform.service.DataService;
import com.datum.platform.service.dto.data.UserWLatCredentialDto;
import com.datum.platform.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataServiceImpl implements DataService {

    @Resource
    private IUniswapV2FactoryContract uniswapV2FactoryContract;
    @Resource
    private DataTokenTemplateContract dataTokenTemplateContract;
    @Resource
    private MetaDataManager metaDataManager;
    @Resource
    private MetaDataColumnManager metaDataColumnManager;
    @Resource
    private TokenManager tokenManager;
    @Resource
    private ModelManager modelManager;
    @Resource
    private PsiManager psiManager;
    @Resource
    private StatsMetaDataManager statsTokenManager;
    @Resource
    private MetaDataCertificateManager metaDataCertificateManager;
    @Resource
    private MetaDataCertificateUserManager metaDataCertificateUserManager;
    @Resource
    private MetaDataMarketplaceManager metaDataMarketplaceManager;
    @Resource
    private MetaDataUserManager metaDataUserManager;

    @Override
    public MetaData statisticsOfGlobal() {
        return metaDataManager.statisticsOfGlobal();
    }

    @Override
    public IPage<MetaData> listMetaDataByOrg(Long current, Long size, String identityId) {
        Page<MetaData> page = new Page<>(current, size);
        return metaDataManager.getDataListByOrg(page, identityId);
    }

    @Override
    public IPage<MetaData> listMetaDataByCondition(Long current, Long size, String keyword, String industry, MetaDataFileTypeEnum fileType, Long minSize, Long maxSize, DataOrderByEnum orderBy) {
        Page<MetaData> page = new Page<>(current, size);
        return metaDataManager.getDataList(page, keyword, industry, fileType.getValue(), minSize, maxSize, orderBy.getSqlValue());
    }

    @Override
    @Cacheable("getMetaDataById-1")
    public MetaData getMetaDataById(String metaDataId, boolean isNeedDetails) {
        MetaData metaData = metaDataManager.getDataDetails(metaDataId);
        if(isNeedDetails){
            List<MetaDataColumn> columnList = metaDataColumnManager.getList(metaDataId);
            metaData.setColumnsList(columnList);
        }
        return metaData;
    }

    @Override
    public IPage<MetaData> getUserDataList(Long current, Long size, String identityId, String keyword) {
        Page<MetaData> page = new Page<>(current, size);
        return metaDataManager.getUserDataList(page, UserContext.getCurrentUser().getAddress(), identityId, keyword);
    }

    @Override
    public IPage<MetaData> getUserAuthDataList(Long current, Long size, String keyword) {
        Page<MetaData> page = new Page<>(current, size);
        return metaDataManager.getUserAuthDataList(page, UserContext.getCurrentUser().getAddress(), keyword);
    }

    @Override
    public List<String> listMetaDataOrgIdByUser(String address) {
        return metaDataManager.listDataOrgIdByUser(address);
    }

    @Override
    public MetaDataColumn getDataColumnByIds(String metaDataId, int columnIndex) {
        return metaDataColumnManager.getById(metaDataId, columnIndex);
    }

    @Override
    public List<String> listERC20TokenAddress() {
        return tokenManager.getERC20TokenAddressList();
    }

    @Override
    public List<Token> listTokenByNeedSyncedInfo(int size) {
        return tokenManager.getNeedSyncedTokenList(size);
    }

    @Override
    public Token getTokenById(String tokenAddress) {
        return tokenManager.getById(tokenAddress);
    }

    @Override
    public boolean saveToken(Token token) {
        return tokenManager.save(token);
    }

    @Override
    public boolean updateTokenById(Token token) {
        if(token.getType() == TokenTypeEnum.ERC20 && StringUtils.isNotBlank(token.getName())){
            metaDataCertificateManager.updateNameByTokenAddress(token.getAddress(), token.getName());
        }
        return tokenManager.updateById(token);
    }

    @Override
    public boolean saveOrUpdateBatchStatsToken(List<StatsMetaData> saveList) {
        return statsTokenManager.saveOrUpdateBatch(saveList);
    }

    @Override
    public List<Model> listModelByUser(Long algorithmId, String identityId) {
        User user = UserContext.getCurrentUser();
        return modelManager.queryAvailableModel(user.getAddress(), algorithmId, identityId);
    }

    @Override
    public Model getModelById(String modelId) {
        return modelManager.getById(modelId);
    }

    @Override
    public Model getModelByOrgAndTrainTaskId(String identity, String taskId){
        return modelManager.getModelByOrgAndTrainTaskId(identity, taskId);
    }

    @Override
    public boolean saveBatchModel(List<Model> modelList) {
        return modelManager.saveBatch(modelList);
    }

    @Override
    public boolean saveBatchPsi(List<Psi> psiList) {
        return psiManager.saveBatch(psiList);
    }

    @Override
    @Transactional
    public void batchReplace(List<MetaData> metaDataList, List<MetaDataColumn> metaDataColumnList, List<Token> tokenList, List<MetaDataCertificate> metaDataCertificateList) {
        metaDataManager.saveOrUpdateBatch(metaDataList);
        LambdaQueryWrapper<MetaDataColumn> wrapper = Wrappers.lambdaQuery();
        wrapper.in(MetaDataColumn::getMetaDataId, metaDataList.stream().map(MetaData::getMetaDataId).collect(Collectors.toSet()));
        metaDataColumnManager.remove(wrapper);
        metaDataColumnManager.saveBatch(metaDataColumnList);
        for (Token token: tokenList) {
            if(tokenManager.getById(token.getAddress()) == null){
                tokenManager.save(token);
            }
        }

        for (MetaDataCertificate metaDataCertificate: metaDataCertificateList) {
            if(metaDataCertificateManager.existNoAttributes(metaDataCertificate.getMetaDataId(), metaDataCertificate.getTokenAddress())){
                metaDataCertificateManager.save(metaDataCertificate);
            }
        }
    }

    @Override
    public UserWLatCredentialDto getUserWLatCredential() {
        UserWLatCredentialDto result = new UserWLatCredentialDto();
        Token token = tokenManager.getById(uniswapV2FactoryContract.WETH());
        result.setTokenAddress(token.getAddress());
        result.setTokenName(token.getName());
        result.setTokenSymbol(token.getSymbol());
        result.setTokenDecimal(token.getDecimal());
        result.setTokenBalance(dataTokenTemplateContract.balanceOf(uniswapV2FactoryContract.WETH(),  UserContext.getCurrentUser().getAddress()).toString());
        result.setAuthorizeBalance(dataTokenTemplateContract.allowance(uniswapV2FactoryContract.WETH(),  UserContext.getCurrentUser().getAddress()).toString());
        return result;
    }

    @Override
    public List<MetaDataColumn> listMetaDataColumnByIdAndIndex(String metaDataId, List<Integer> selectedColumnsV2) {
        return metaDataColumnManager.listByIdAndIndex(metaDataId, selectedColumnsV2);
    }

    @Override
    public Model getModelByTaskId(String taskId) {
        return modelManager.getModelByTaskId(taskId);
    }

    @Override
    public MetaDataCertificate getNoAttributeCredentialByMetaDataId(String metaDataId) {
        return metaDataCertificateManager.getNoAttributeCredentialByMetaDataId(metaDataId);
    }

    @Override
    public MetaDataCertificate getNoAttributeCredentialByMetaDataIdAndUser(String metaDataId) {
        return metaDataCertificateManager.getNoAttributeCredentialByMetaDataIdAndUser(metaDataId,UserContext.getCurrentUser().getAddress());
    }

    @Override
    public IPage<MetaDataCertificate> pageHaveAttributesCertificateByMetaDataId(Long current, Long size, String metaDataId) {
        Page<MetaDataCertificate> page = new Page<>(current, size);
        return metaDataCertificateManager.pageHaveAttributesCertificateByMetaDataId(page, metaDataId);
    }

    @Override
    public IPage<MetaDataCertificate> pageHaveAttributesCertificateByMetaDataIdAndUser(Long current, Long size, String metaDataId) {
        Page<MetaDataCertificate> page = new Page<>(current, size);
        return metaDataCertificateManager.pageHaveAttributesCertificateByMetaDataIdAndUser(page, metaDataId, UserContext.getCurrentUser().getAddress());
    }

    @Override
    public List<MetaDataCertificate> listHaveAttributesCertificateByMetaDataIdAndUser(String metaDataId) {
        return metaDataCertificateManager.listHaveAttributesCertificateByMetaDataIdAndUser(metaDataId, UserContext.getCurrentUser().getAddress());
    }

    @Override
    public boolean isMetaDataOwner(String metaDataId) {
        return metaDataManager.isOwner(metaDataId, UserContext.getCurrentUser().getAddress());
    }

    @Override
    public List<MetaDataCertificate> listMetaDataCertificateUser(List<Long> credentialIdList) {
        return metaDataCertificateManager.listCertificateByMetaDataIdListAndUser(credentialIdList, UserContext.getCurrentUser().getAddress());
    }

    @Override
    public List<MetaData> listMetaDataOfNeedSyncedMetaDataCertificate() {
        return metaDataManager.listDataOfNeedSyncedMetaDataCertificate();
    }

    @Override
    public boolean saveOrUpdateOrDeleteBatchMetaDataCertificate(String metaDataId, List<MetaDataCertificate> insertMetaDataCertificateList, List<MetaDataCertificate> updateMetaDataCertificateList, List<Long> deleteIdList) {
        return metaDataCertificateManager.saveOrUpdateOrDeleteBatch(metaDataId, insertMetaDataCertificateList, updateMetaDataCertificateList, deleteIdList);
    }

    @Override
    public List<MetaDataCertificate> listMetaDataCertificate() {
        return metaDataCertificateManager.listKey();
    }

    @Override
    public boolean existMetaDataCertificateUser(String address, Long metaDataCertificateId) {
        return metaDataCertificateUserManager.getById(metaDataCertificateId, address) != null;
    }

    @Override
    public boolean saveOrUpdateBatchMetaDataCertificateUser(String address, List<MetaDataCertificateUser> metaDataCertificateUserList) {
        return metaDataCertificateUserManager.saveOrUpdateBatchMetaDataCertificateUser(address, metaDataCertificateUserList);
    }

    @Override
    public String getMetaDataCertificateName(MetaDataCertificateTypeEnum metaDataCertificateTypeEnum, String metaDataId, String consumeTokenAddress, String consumeTokenId) {
        return metaDataCertificateManager.getName(metaDataCertificateTypeEnum, metaDataId, consumeTokenAddress, consumeTokenId);
    }

    @Override
    public boolean existMetaData(String metaDataId) {
        return metaDataManager.exist(metaDataId);
    }

    @Override
    public List<String> listMetaDataIdOfPublished() {
        return metaDataManager.listIdOfPublished();
    }

    @Override
    public boolean isTradable(String metaDataId) {
        List<MetaDataCertificate> metaDataCertificateList = metaDataCertificateManager.listByMetaDataId(metaDataId);
        long countOfHaveAttributes = metaDataCertificateList.stream().filter(metaDataCertificate -> metaDataCertificate.getType() == MetaDataCertificateTypeEnum.HAVE_ATTRIBUTES).count();
        if(countOfHaveAttributes > 0){
            return true;
        }
        Optional<MetaDataCertificate> noAttributesMetaDataCertificate = metaDataCertificateList.stream().filter(metaDataCertificate -> metaDataCertificate.getType() == MetaDataCertificateTypeEnum.NO_ATTRIBUTES).findFirst();
        if(noAttributesMetaDataCertificate.isPresent()){
            return tokenManager.isAddLiquidity(noAttributesMetaDataCertificate.get().getTokenAddress());
        }else{
            return false;
        }
    }

    @Override
    public boolean batchReplaceMetaDataMarketplace(List<MetaDataMarketplace> metaDataMarketplaceList) {
        return metaDataMarketplaceManager.batchReplace(metaDataMarketplaceList);
    }

    @Override
    public List<MetaDataCertificate> listMetaDataCertificateByMetaDataId(String metaDataId) {
        return metaDataCertificateManager.listByMetaDataId(metaDataId);
    }

    @Override
    public boolean setMetaDataUser(String address) {
        // 查询用户存在的凭证列表
        List<Long> metaDataCertificateIdList = metaDataCertificateUserManager.listMetaDataCertificateIdByAddress(address);
        // 查询凭证对应元数据id
        List<String> metaDataIdList = new ArrayList<>();
        if(metaDataCertificateIdList.size() > 0){
            metaDataIdList = metaDataCertificateManager.listMetaDataIdByIds(metaDataCertificateIdList);
        }
        // 设置用户的可见
        metaDataUserManager.saveOrDeleteBatch(address, metaDataIdList);
        return true;
    }

    @Override
    public String getMetaDataName(String metaDataId) {
        return metaDataManager.getName(metaDataId);
    }
}
