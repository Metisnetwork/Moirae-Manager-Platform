package com.datum.platform.service.impl;

import com.datum.platform.common.constants.SysConfig;
import com.datum.platform.manager.AlgorithmClassifyManager;
import com.datum.platform.manager.AlgorithmCodeManager;
import com.datum.platform.manager.AlgorithmManager;
import com.datum.platform.manager.AlgorithmVariableManager;
import com.datum.platform.mapper.domain.Algorithm;
import com.datum.platform.mapper.domain.AlgorithmClassify;
import com.datum.platform.mapper.enums.AlgorithmTypeEnum;
import com.datum.platform.mapper.enums.CalculationProcessTaskAlgorithmSelectEnum;
import com.datum.platform.service.AlgService;
import com.datum.platform.service.utils.TreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlgServiceImpl implements AlgService {

    @Resource
    private SysConfig sysConfig;
    @Resource
    private AlgorithmClassifyManager algorithmClassifyManager;
    @Resource
    private AlgorithmManager algorithmManager;
    @Resource
    private AlgorithmCodeManager algorithmCodeManager;
    @Resource
    private AlgorithmVariableManager algorithmVariableManager;

    @Override
    @Cacheable("getAlgorithmClassifyTree-1")
    public AlgorithmClassify getAlgorithmClassifyTree(boolean isNeedDetails) {
        return getAlgTree(isNeedDetails, 1L);
    }

    @Override
    @Cacheable("getAlgorithmClassifyTree-2")
    public AlgorithmClassify getAlgorithmClassifyTree(boolean isNeedDetails, Long id) {
        if(id == null){
            return getAlgTree(isNeedDetails, 1L);
        }else{
            return getAlgTree(isNeedDetails, id);
        }
    }

    @Override
    @Cacheable("getAlgorithm-1")
    public Algorithm getAlgorithm(Long algorithmId, boolean isNeedDetails) {
        AlgorithmClassify algorithmClassify = algorithmClassifyManager.getById(algorithmId);
        return getAlgorithmById(algorithmClassify, isNeedDetails);
    }

    @Override
    public Algorithm getAlgorithmOfRelativelyPrediction(Long algorithmId) {
        AlgorithmClassify algorithmClassify = algorithmClassifyManager.getById(algorithmId);
        List<AlgorithmClassify> algorithmClassifyList = algorithmClassifyManager.listByParentId(algorithmClassify.getParentId());
        List<Algorithm> algorithmList = algorithmManager.listByIds(algorithmClassifyList.stream().map(AlgorithmClassify::getId).collect(Collectors.toList()));
        return algorithmList.stream().filter(item -> item.getInputModel()).findFirst().get();
    }

    @Override
    public Algorithm findAlgorithm(CalculationProcessTaskAlgorithmSelectEnum algorithmSelect, AlgorithmClassify rootTree, AlgorithmClassify selectedTree) {
        switch(algorithmSelect){
            case USER_TRAIN_ALG:
                return selectedTree.getChildrenList().stream().filter(item -> item.getAlg().getOutputModel()).findFirst().get().getAlg();
            case USER_PREDICT_ALG:
                return selectedTree.getChildrenList().stream().filter(item -> item.getAlg().getInputModel()).findFirst().get().getAlg();
            case BUILD_IN_ALG:
                return TreeUtils.findSubTree(rootTree, sysConfig.getDefaultPsi()).getAlg();
            default:
                return selectedTree.getAlg();
        }
    }

    @Override
    public AlgorithmTypeEnum getAlgorithmType(AlgorithmClassify selectedTree) {
        if(selectedTree.getIsExistAlgorithm()){
            return selectedTree.getAlg().getType();
        }

        if(selectedTree.getChildrenList().size() > 0){
            return selectedTree.getChildrenList().get(0).getAlg().getType();
        }

        return null;
    }

    private AlgorithmClassify getAlgTree(boolean isNeedDetails, Long rootId) {
        List<AlgorithmClassify> algorithmClassifyList = algorithmClassifyManager.list();
        algorithmClassifyList.forEach(algorithmClassify->{
            if(algorithmClassify.getIsExistAlgorithm()){
                algorithmClassify.setAlg(getAlgorithmById(algorithmClassify, isNeedDetails));
            }
        });
        return TreeUtils.buildTreeByRecursive(algorithmClassifyList, rootId);
    }

    private Algorithm getAlgorithmById(AlgorithmClassify algorithmClassify, boolean isNeedDetails){
        Algorithm algorithm = algorithmManager.getById(algorithmClassify.getId());
        algorithm.setAlgorithmName(algorithmClassify.getName());
        algorithm.setAlgorithmNameEn(algorithmClassify.getNameEn());
        if(isNeedDetails){
            algorithm.setAlgorithmVariableList(algorithmVariableManager.getByAlgorithmId(algorithmClassify.getId()));
            algorithm.setAlgorithmCode(algorithmCodeManager.getById(algorithmClassify.getId()));
        }
        return algorithm;
    }
}
