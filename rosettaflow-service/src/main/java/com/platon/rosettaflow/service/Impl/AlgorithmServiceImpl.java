package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.dto.AlgorithmDto;
import com.platon.rosettaflow.mapper.AlgorithmMapper;
import com.platon.rosettaflow.mapper.domain.Algorithm;
import com.platon.rosettaflow.mapper.domain.AlgorithmCode;
import com.platon.rosettaflow.service.IAlgorithmCodeService;
import com.platon.rosettaflow.service.IAlgorithmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 算法实现类
 * @author admin
 * @date 2021/8/23
 */
@Slf4j
@Service
public class AlgorithmServiceImpl extends ServiceImpl<AlgorithmMapper, Algorithm> implements IAlgorithmService{

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
    public List<AlgorithmDto> queryAlgorithmList(Long userId, String algorithmName) {
        try {
            return algorithmMapper.queryAlgorithmList(userId, algorithmName);
        } catch (Exception e) {
            log.error("queryAlgorithmList--查询算法列表失败, 错误信息:{}", e.getMessage());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.QUERY_ALG_LIST_ERROR.getMsg());
        }
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

}
