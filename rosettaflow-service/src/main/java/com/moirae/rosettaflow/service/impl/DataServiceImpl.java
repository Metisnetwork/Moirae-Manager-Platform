package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.chain.platon.contract.IUniswapV2FactoryContract;
import com.moirae.rosettaflow.common.enums.DataOrderByEnum;
import com.moirae.rosettaflow.manager.*;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.mapper.enums.MetaDataFileTypeEnum;
import com.moirae.rosettaflow.service.AlgService;
import com.moirae.rosettaflow.service.DataService;
import com.moirae.rosettaflow.service.dto.data.MetisLatInfoDto;
import com.moirae.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataServiceImpl implements DataService {

    @Resource
    private IUniswapV2FactoryContract uniswapV2FactoryContract;
    @Resource
    private AlgService algService;
    @Resource
    private MetaDataManager metaDataManager;
    @Resource
    private MetaDataColumnManager metaDataColumnManager;
    @Resource
    private TokenManager tokenManager;
    @Resource
    private TokenHolderManager tokenHolderManager;
    @Resource
    private ModelManager modelManager;
    @Resource
    private PsiManager psiManager;


    @Override
    public int countOfData() {
        return metaDataManager.countOfData();
    }

    @Override
    public IPage<MetaData> getDataListByOrg(Long current, Long size, String identityId) {
        Page<MetaData> page = new Page<>(current, size);
        return metaDataManager.getDataListByOrg(page, identityId);
    }

    @Override
    public IPage<MetaData> getDataList(Long current, Long size, String keyword, String industry, MetaDataFileTypeEnum fileType, Long minSize, Long maxSize, DataOrderByEnum orderBy) {
        Page<MetaData> page = new Page<>(current, size);
        return metaDataManager.getDataList(page, keyword, industry, fileType.getValue(), minSize, maxSize, orderBy.getSqlValue());
    }

    @Override
    public MetaData getDataDetails(String metaDataId) {
        MetaData metaData = metaDataManager.getDataDetails(metaDataId);
        List<MetaDataColumn> columnList = metaDataColumnManager.getList(metaDataId);
        metaData.setColumnsList(columnList);
        return metaData;
    }

    @Override
    public IPage<MetaData> getUserDataList(Long current, Long size) {
        Page<MetaData> page = new Page<>(current, size);
        return metaDataManager.getUserDataList(page, UserContext.getCurrentUser().getAddress());
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
    public Map<String, MetaData> getMetaDataId2MetaDataMap(Set<String> metaDataId) {
        List<MetaData> metaDataList = metaDataManager.listByIds(metaDataId);
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
    public List<Token> getNeedSyncedTokenList(int size) {
        return tokenManager.getNeedSyncedTokenList(size);
    }

    @Override
    public boolean updateToken(Token token) {
        return tokenManager.updateById(token);
    }

    @Override
    public List<String> getTokenIdList() {
        return tokenManager.getTokenIdList();
    }

    @Override
    @Transactional
    public boolean batchInsertOrUpdateTokenHolder(String address, List<TokenHolder> tokenHolderList) {
        return tokenHolderManager.batchInsertOrUpdateByUser(address, tokenHolderList);
    }

    @Override
    public List<Model> queryAvailableModel(Long algorithmId, String identityId) {
        User user = UserContext.getCurrentUser();
        return modelManager.queryAvailableModel(user.getAddress(), algorithmId, identityId);
    }

    @Override
    public Model getModelById(String modelId) {
        return modelManager.getById(modelId);
    }

    @Override
    public MetisLatInfoDto getUserMetisLatInfo() {
        MetisLatInfoDto result = new MetisLatInfoDto();
        Token token = tokenManager.getById(uniswapV2FactoryContract.WETH());
        TokenHolder tokenHolder = tokenHolderManager.getById(uniswapV2FactoryContract.WETH(), UserContext.getCurrentUser().getAddress());
        result.setTokenAddress(token.getAddress());
        result.setTokenName(token.getName());
        result.setTokenSymbol(token.getSymbol());
        result.setTokenDecimal(token.getDecimal());
        result.setTokenBalance(tokenHolder.getBalance());
        result.setAuthorizeBalance(tokenHolder.getAuthorizeBalance());
        return result;
    }

    @Override
    public boolean saveToken(Token token) {
       return tokenManager.save(token);
    }

    @Override
    public Token getTokenById(String tokenAddress) {
        return tokenManager.getById(tokenAddress);
    }

    @Override
    public Token getMetisToken() {
        return getTokenById(uniswapV2FactoryContract.WETH());
    }

    @Override
    public Token getTokenByMetaDataId(String metaDataId) {
        return getTokenById(metaDataManager.getById(metaDataId).getTokenAddress());
    }

    @Override
    public TokenHolder getTokenHolderById(String tokenAddress, String userAddress) {
        return tokenHolderManager.getById(tokenAddress, userAddress);
    }

    @Override
    public Model getModelByOrgAndTrainTaskId(String identity, String taskId){
        return modelManager.getModelByOrgAndTrainTaskId(identity, taskId);
    }

    @Override
    public List<Psi> listPsiByTrainTaskId(String taskId) {
        return psiManager.listByTrainTaskId(taskId);
    }

    @Override
    public MetaData getDataById(String metaDataId) {
        return metaDataManager.getById(metaDataId);
    }

    @Override
    public boolean saveBatchPsi(List<Psi> psiList) {
        return psiManager.saveBatch(psiList);
    }

    @Override
    public boolean saveBatchModel(List<Model> modelList) {
        return modelManager.saveBatch(modelList);
    }

    @Override
    public List<Model> listModelOfLatest(Integer size) {
        List<Model> modelList = modelManager.listOfLatest(size);
        if(modelList.size() == 0){
            return modelList;
        }

        List<AlgorithmClassify> algorithmClassifyList = algService.listAlglassifyByIds(modelList.stream().map(Model::getTrainAlgorithmId).collect(Collectors.toSet()));
        List<AlgorithmClassify> parentAlgorithmClassifyList = algService.listAlglassifyByIds(algorithmClassifyList.stream().map(AlgorithmClassify::getParentId).collect(Collectors.toSet()));
        Map<Long, AlgorithmClassify>  algorithmClassifyMap = algorithmClassifyList.stream().collect(Collectors.toMap(AlgorithmClassify::getId, item -> item));
        Map<Long, AlgorithmClassify>  parentAlgorithmClassifyMap = parentAlgorithmClassifyList.stream().collect(Collectors.toMap(AlgorithmClassify::getId, item -> item));
        for (Model model : modelList) {
            model.setAlgorithmName(parentAlgorithmClassifyMap.get(algorithmClassifyMap.get(model.getTrainAlgorithmId()).getParentId()).getName());
            model.setAlgorithmNameEn(parentAlgorithmClassifyMap.get(algorithmClassifyMap.get(model.getTrainAlgorithmId()).getParentId()).getNameEn());
        }
        return modelList;
    }

    @Override
    public MetaData statisticsOfGlobal() {
        return metaDataManager.statisticsOfGlobal();
    }
}
