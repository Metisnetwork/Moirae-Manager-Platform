package com.platon.rosettaflow.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.dto.AlgorithmDto;
import com.platon.rosettaflow.mapper.AlgorithmMapper;
import com.platon.rosettaflow.mapper.domain.Algorithm;
import com.platon.rosettaflow.mapper.domain.AlgorithmCode;
import com.platon.rosettaflow.mapper.domain.WorkflowNode;
import com.platon.rosettaflow.service.IAlgorithmCodeService;
import com.platon.rosettaflow.service.IAlgorithmService;
import com.platon.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 算法实现类
 *
 * @author admin
 * @date 2021/8/23
 */
@Slf4j
@Service
public class AlgorithmServiceImpl extends ServiceImpl<AlgorithmMapper, Algorithm> implements IAlgorithmService {

    @Resource
    AlgorithmMapper algorithmMapper;

    @Resource
    IAlgorithmCodeService algorithmCodeService;

    @Override
    public void addAlgorithm(AlgorithmDto algorithmDto) {
        try {
            Algorithm algorithm = new Algorithm();
            BeanUtils.copyProperties(algorithmDto, algorithm);
            // 保存算法
            this.save(algorithm);
            // 保存算法代码
            AlgorithmCode algorithmCode = new AlgorithmCode();
            algorithmCode.setAlgorithmId(algorithm.getId());
            algorithmCode.setEditType(algorithmDto.getEditType());
            algorithmCode.setCalculateContractCode(algorithmDto.getAlgorithmCode());
            algorithmCodeService.addAlgorithmCode(algorithmCode);
        } catch (Exception e) {
            log.error("addAlgorithm--新增算法失败, 错误信息:{}", e.getMessage());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ADD_ALG_ERROR.getMsg());
        }
    }

    @Override
    public void updateAlgorithm(AlgorithmDto algorithmDto) {
        try {
            Algorithm algorithm = new Algorithm();
            BeanUtils.copyProperties(algorithmDto, algorithm);
            // 算法id
            algorithm.setId(algorithmDto.getId());
            // 修改算法
            this.updateById(algorithm);
            // 修改算法代码
            AlgorithmCode algorithmCode = new AlgorithmCode();
            algorithmCode.setAlgorithmId(algorithmDto.getId());
            algorithmCode.setEditType(algorithmDto.getEditType());
            algorithmCode.setCalculateContractCode(algorithmDto.getAlgorithmCode());
            algorithmCodeService.updateAlgorithmCode(algorithmCode);
        } catch (Exception e) {
            log.error("updateAlgorithm--修改算法失败, 错误信息:{}", e.getMessage());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.UPDATE_ALG_ERROR.getMsg());
        }

    }

    @Override
    public List<AlgorithmDto> queryAlgorithmList(String algorithmName) {
        return algorithmMapper.queryAlgorithmList(algorithmName);
    }

    @Override
    public AlgorithmDto queryAlgorithmDetails(Long algorithmId) {
        try {
            return algorithmMapper.queryAlgorithmDetails(algorithmId);
        } catch (Exception e) {
            log.error("queryAlgorithmDetails--查询算法详情失败, 错误信息:{}", e.getMessage());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.QUERY_ALG_DETAILS_ERROR.getMsg());
        }

    }

    @Override
    public Long copySaveAlgorithm(WorkflowNode oldNode) {
        Algorithm oldAlgorithm = this.getById(oldNode.getId());
        if (Objects.isNull(oldAlgorithm)) {
            return null;
        }
        Algorithm newAlgorithm = BeanUtil.toBean(oldAlgorithm, Algorithm.class);
        newAlgorithm.setId(null);
        newAlgorithm.setAuthor(UserContext.get() == null ? "" : UserContext.get().getUserName());
        newAlgorithm.setCreateTime(null);
        newAlgorithm.setUpdateTime(null);
        this.save(newAlgorithm);
        return newAlgorithm.getId();
    }

    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }

}
