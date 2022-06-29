package com.datum.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.chain.platon.contract.IUniswapV2FactoryContract;
import com.datum.platform.common.enums.DataOrderByEnum;
import com.datum.platform.manager.*;
import com.datum.platform.mapper.domain.*;
import com.datum.platform.mapper.enums.MetaDataCertificateTypeEnum;
import com.datum.platform.mapper.enums.MetaDataFileTypeEnum;
import com.datum.platform.service.DataService;
import com.datum.platform.service.dto.data.DatumNetworkLatInfoDto;
import com.datum.platform.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataServiceImpl implements DataService {

    @Resource
    private IUniswapV2FactoryContract uniswapV2FactoryContract;
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
    private StatsTokenManager statsTokenManager;
    @Resource
    private MetaDataCertificateManager metaDataCertificateManager;


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
    public Map<String, MetaData> getMetaDataId2MetaDataMap(Set<String> metaDataId) {
        List<MetaData> metaDataList = metaDataManager.listByIds(metaDataId);
        if(metaDataList.size() == 0){
            return new HashMap<>();
        }
        Set<String> tokenIdList = metaDataList.stream()
                .filter(item -> StringUtils.isNotBlank(item.getTokenAddress()))
                .map(MetaData::getTokenAddress)
                .collect(Collectors.toSet());
        Map<String, Token> tokenMap = tokenManager.listByIds(tokenIdList).stream().collect(Collectors.toMap(Token::getAddress, item -> item));

        metaDataList.stream().forEach(item -> {
            if(tokenMap.containsKey(item.getTokenAddress())){
                item.setTokenName(tokenMap.get(item.getTokenAddress()).getName());
            }
        });

        return metaDataList.stream().collect(Collectors.toMap(MetaData::getMetaDataId, item -> item));
    }

    @Override
    public List<MetaData> listMetaDataByIds(Set<String> metaDataIdList) {
        return metaDataManager.listByIds(metaDataIdList);
    }

    @Override
    public List<MetaData> listMetaDataByTokenAddress(String tokenAddress) {
        return metaDataManager.listDataByTokenAddress(tokenAddress);
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
    public List<Token> listERC721Token() {
        return tokenManager.listERC721Token();
    }

    @Override
    public List<Token> listTokenByIds(Collection<String> tokenIdList) {
        return tokenManager.listByIds(tokenIdList);
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
    public Token getDatumNetworkToken() {
        return getTokenById(uniswapV2FactoryContract.WETH());
    }

    @Override
    public boolean saveToken(Token token) {
        return tokenManager.save(token);
    }

    @Override
    public boolean updateTokenById(Token token) {
        return tokenManager.updateById(token);
    }

    @Override
    public TokenHolder getTokenHolderById(String tokenAddress, String userAddress) {
        return tokenHolderManager.getById(tokenAddress, userAddress);
    }

    @Override
    @Transactional
    public boolean saveOrUpdateBatchTokenHolder(String address, List<TokenHolder> tokenHolderList) {
        return tokenHolderManager.batchInsertOrUpdateByUser(address, tokenHolderList);
    }

    @Override
    public boolean saveOrUpdateBatchStatsToken(List<StatsToken> saveList) {
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
    public List<Psi> listPsiByTrainTaskId(String taskId) {
        return psiManager.listByTrainTaskId(taskId);
    }

    @Override
    public boolean saveBatchPsi(List<Psi> psiList) {
        return psiManager.saveBatch(psiList);
    }

    @Override
    @Transactional
    public void batchReplace(List<MetaData> metaDataList, List<MetaDataColumn> metaDataColumnList, List<Token> tokenList) {
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
    }

    @Override
    public DatumNetworkLatInfoDto getUserDatumNetworkLatInfo() {
        DatumNetworkLatInfoDto result = new DatumNetworkLatInfoDto();
        Token token = tokenManager.getById(uniswapV2FactoryContract.WETH());
        TokenHolder tokenHolder = tokenHolderManager.getById(uniswapV2FactoryContract.WETH(), UserContext.getCurrentUser().getAddress());
        result.setTokenAddress(token.getAddress());
        result.setTokenName(token.getName());
        result.setTokenSymbol(token.getSymbol());
        result.setTokenDecimal(token.getDecimal());
        if(tokenHolder != null){
            result.setTokenBalance(tokenHolder.getBalance());
            result.setAuthorizeBalance(tokenHolder.getAuthorizeBalance());
        }else{
            result.setTokenBalance("0");
            result.setAuthorizeBalance("0");
        }
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
    public boolean existTokeHolder(String address, String token) {
        return tokenHolderManager.getById(token, address) != null;
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
    @Transactional
    public boolean saveOrUpdateBatchTokenInventory(String address, List<TokenInventory> tokenInventoryList) {
        return tokenInventoryManager.saveOrUpdateBatchByTokenAddress(address, tokenInventoryList);
    }

    @Override
    public boolean isMetaDataOwner(String metaDataId) {
        return metaDataManager.isOwner(metaDataId, UserContext.getCurrentUser().getAddress());
    }
}
