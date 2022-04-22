package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.moirae.rosettaflow.common.constants.SysConfig;
import com.moirae.rosettaflow.manager.AlgorithmClassifyManager;
import com.moirae.rosettaflow.manager.AlgorithmCodeManager;
import com.moirae.rosettaflow.manager.AlgorithmManager;
import com.moirae.rosettaflow.manager.AlgorithmVariableManager;
import com.moirae.rosettaflow.mapper.domain.Algorithm;
import com.moirae.rosettaflow.mapper.domain.AlgorithmClassify;
import com.moirae.rosettaflow.mapper.enums.CalculationProcessTaskAlgorithmSelectEnum;
import com.moirae.rosettaflow.service.AlgService;
import com.moirae.rosettaflow.service.dto.alg.AlgTreeDto;
import com.moirae.rosettaflow.service.utils.TreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
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
    public AlgTreeDto getAlgTreeDto(boolean isNeedDetails) {
        AlgorithmClassify algorithmClassify = getAlgTree(isNeedDetails);
        return BeanUtil.copyProperties(algorithmClassify, AlgTreeDto.class);
    }

    @Override
    public AlgorithmClassify getAlgTree(boolean isNeedDetails) {
        return getAlgTree(isNeedDetails, 1L);
    }

    public AlgorithmClassify getAlgTree(boolean isNeedDetails, Long rootId) {
        List<AlgorithmClassify> algorithmClassifyList = algorithmClassifyManager.list();
        algorithmClassifyList.forEach(item->{
            if(item.getIsExistAlgorithm()){
                Algorithm algorithm = algorithmManager.getById(item.getId());
                algorithm.setAlgorithmName(item.getName());
                algorithm.setAlgorithmNameEn(item.getNameEn());

                if(isNeedDetails){
                    algorithm.setAlgorithmVariableList(algorithmVariableManager.getByAlgorithmId(item.getId()));
                    algorithm.setAlgorithmCode(algorithmCodeManager.getById(item.getId()));
                }
                item.setAlg(algorithm);
            }
        });
        return TreeUtils.buildTreeByRecursive(algorithmClassifyList, rootId);
    }

    @Override
    public Algorithm getAlg(Long algorithmId, boolean isNeedDetails) {
        Algorithm algorithm = algorithmManager.getById(algorithmId);
        AlgorithmClassify algorithmClassify = algorithmClassifyManager.getById(algorithmId);
        algorithm.setAlgorithmName(algorithmClassify.getName());
        algorithm.setAlgorithmNameEn(algorithmClassify.getNameEn());
        if(isNeedDetails){
            algorithm.setAlgorithmVariableList(algorithmVariableManager.getByAlgorithmId(algorithmId));
            algorithm.setAlgorithmCode(algorithmCodeManager.getById(algorithmId));
        }
        return algorithm;
    }

    @Override
    public Algorithm getAlgOfRelativelyPrediction(Long algorithmId) {
        AlgorithmClassify algorithmClassify = algorithmClassifyManager.getById(algorithmId);
        List<AlgorithmClassify> algorithmClassifyList = algorithmClassifyManager.listByParentId(algorithmClassify.getParentId());
        List<Algorithm> algorithmList = algorithmManager.listByIds(algorithmClassifyList.stream().map(AlgorithmClassify::getId).collect(Collectors.toList()));
        return algorithmList.stream().filter(item -> item.getInputModel()).findFirst().get();
    }

    @Override
    public List<AlgorithmClassify> listAlglassifyByIds(Set<Long> collect) {
        return algorithmClassifyManager.listByIds(collect);
    }

    @Override
    public Algorithm findAlg(CalculationProcessTaskAlgorithmSelectEnum algorithmSelect, AlgorithmClassify rootTree, AlgorithmClassify selectedTree) {
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
}
