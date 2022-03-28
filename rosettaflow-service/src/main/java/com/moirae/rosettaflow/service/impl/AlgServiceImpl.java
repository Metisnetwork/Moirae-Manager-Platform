package com.moirae.rosettaflow.service.impl;

import com.moirae.rosettaflow.manager.AlgorithmClassifyManager;
import com.moirae.rosettaflow.manager.AlgorithmCodeManager;
import com.moirae.rosettaflow.manager.AlgorithmManager;
import com.moirae.rosettaflow.manager.AlgorithmVariableManager;
import com.moirae.rosettaflow.mapper.domain.Algorithm;
import com.moirae.rosettaflow.mapper.domain.AlgorithmClassify;
import com.moirae.rosettaflow.service.AlgService;
import com.moirae.rosettaflow.service.utils.TreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class AlgServiceImpl implements AlgService {

    @Resource
    private AlgorithmClassifyManager algorithmClassifyManager;
    @Resource
    private AlgorithmManager algorithmManager;
    @Resource
    private AlgorithmCodeManager algorithmCodeManager;
    @Resource
    private AlgorithmVariableManager algorithmVariableManager;

    @Override
    public AlgorithmClassify queryAlgTreeList() {
        List<AlgorithmClassify> algorithmClassifyList = algorithmClassifyManager.list();
        algorithmClassifyList.forEach(item->{
            if(item.getIsExistAlgorithm()){
                Algorithm algorithm = algorithmManager.getById(item.getId());
                algorithm.setAlgorithmVariableList(algorithmVariableManager.getByAlgorithmId(item.getId()));
                algorithm.setAlgorithmCode(algorithmCodeManager.getById(item.getId()));
                item.setAlg(algorithm);
            }
        });
        return TreeUtils.buildTreeByRecursive(algorithmClassifyList, 1);
    }
}
