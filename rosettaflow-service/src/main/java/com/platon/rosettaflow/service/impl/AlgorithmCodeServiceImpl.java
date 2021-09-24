package com.platon.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.mapper.AlgorithmCodeMapper;
import com.platon.rosettaflow.mapper.domain.AlgorithmCode;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeCode;
import com.platon.rosettaflow.service.IAlgorithmCodeService;
import com.platon.rosettaflow.service.IWorkflowNodeCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 算法实现类
 * @author admin
 * @date 2021/8/23
 */
@Slf4j
@Service
public class AlgorithmCodeServiceImpl extends ServiceImpl<AlgorithmCodeMapper, AlgorithmCode> implements IAlgorithmCodeService {

    @Resource
    private IWorkflowNodeCodeService workflowNodeCodeService;

    @Override
    public void addAlgorithmCode(AlgorithmCode algorithmCode) {
        this.save(algorithmCode);
    }

    @Override
    public void updateAlgorithmCode(AlgorithmCode algorithmCode) {
        LambdaUpdateWrapper<AlgorithmCode> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AlgorithmCode::getAlgorithmId, algorithmCode.getAlgorithmId());
        updateWrapper.eq(AlgorithmCode::getStatus, StatusEnum.VALID.getValue());
        this.update(algorithmCode, updateWrapper);

    }

    @Override
    public AlgorithmCode getByAlgorithmId(Long algorithmId) {
        LambdaUpdateWrapper<AlgorithmCode> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(AlgorithmCode::getAlgorithmId, algorithmId);
        wrapper.eq(AlgorithmCode::getStatus, StatusEnum.VALID.getValue());
        return this.getOne(wrapper);
    }

    @Override
    public void copySaveAlgorithmCode(Long oldAlgorithmId,Long newAlgorithmId, Long oldNodeId){
        WorkflowNodeCode workflowNodeCode = workflowNodeCodeService.getByWorkflowNodeId(oldNodeId);
        // 创建算法代码对象
        AlgorithmCode newAlgorithmCode = new AlgorithmCode();
        newAlgorithmCode.setAlgorithmId(newAlgorithmId);
        // 节点算法代码
        if (Objects.nonNull(workflowNodeCode)) {
            newAlgorithmCode.setEditType(workflowNodeCode.getEditType());
            newAlgorithmCode.setCalculateContractCode(workflowNodeCode.getCalculateContractCode());
            newAlgorithmCode.setDataSplitContractCode(workflowNodeCode.getDataSplitContractCode());
            this.save(newAlgorithmCode);
            return;
        }
        // 算法代码
        AlgorithmCode algorithmCodeOld = this.getByAlgorithmId(oldAlgorithmId);
        if (Objects.nonNull(algorithmCodeOld)) {
            newAlgorithmCode.setEditType(algorithmCodeOld.getEditType());
            newAlgorithmCode.setCalculateContractCode(algorithmCodeOld.getCalculateContractCode());
            newAlgorithmCode.setDataSplitContractCode(algorithmCodeOld.getDataSplitContractCode());
            this.save(newAlgorithmCode);
        }
    }

    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }

}
