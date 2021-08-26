package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.dto.AlgorithmDto;
import com.platon.rosettaflow.mapper.AlgorithmMapper;
import com.platon.rosettaflow.mapper.domain.Algorithm;
import com.platon.rosettaflow.mapper.domain.AlgorithmCode;
import com.platon.rosettaflow.service.IAlgorithmCodeService;
import com.platon.rosettaflow.service.IAlgorithmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author admin
 * @date 2021/8/23
 * @description 算法实现类
 */
@Slf4j
@Service
public class AlgorithmServiceImpl extends ServiceImpl<AlgorithmMapper, Algorithm> implements IAlgorithmService{

    @Resource
    AlgorithmMapper algorithmMapper;

    @Resource
    IAlgorithmCodeService algorithmCodeService;

    @Override
    public void saveAlgorithm(AlgorithmDto algorithmDto) {
        Algorithm algorithm = new Algorithm();
        BeanUtils.copyProperties(algorithmDto, algorithm);
        // 新增
        if (null == algorithmDto.getId() || algorithmDto.getId() == 0) {
            // 保存算法
            this.save(algorithm);
            // 保存算法代码
            AlgorithmCode algorithmCode = new AlgorithmCode();
            algorithmCode.setAlgorithmId(algorithm.getId());
            algorithmCode.setEditType(algorithmDto.getEditType());
            algorithmCode.setCalculateContractCode(algorithmDto.getAlgorithmCode());
            algorithmCodeService.addAlgorithmCode(algorithmCode);
        }
        // 修改
        if (null != algorithmDto.getId() && algorithmDto.getId() > 0) {
            // 修改算法
            this.updateById(algorithm);
            // 修改算法代码
            AlgorithmCode algorithmCode = new AlgorithmCode();
            algorithmCode.setAlgorithmId(algorithmDto.getId());
            algorithmCode.setEditType(algorithmDto.getEditType());
            algorithmCode.setCalculateContractCode(algorithmDto.getAlgorithmCode());
            algorithmCodeService.updateAlgorithmCode(algorithmCode);
        }

    }

    @Override
    public List<AlgorithmDto> queryAlgorithmList(Long userId, String algorithmName) {
        return algorithmMapper.queryAlgorithmList(userId, algorithmName);
    }

    @Override
    public AlgorithmDto queryAlgorithmDetails(Long algorithmId) {
        return algorithmMapper.queryAlgorithmDetails(algorithmId);
    }

}
