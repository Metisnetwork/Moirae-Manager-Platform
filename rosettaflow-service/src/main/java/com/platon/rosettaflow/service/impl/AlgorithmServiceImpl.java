package com.platon.rosettaflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.common.enums.AlgorithmTypeEnum;
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
import java.util.*;

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
            algorithmCode.setCalculateContractCode(algorithmDto.getCalculateContractCode());
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
            algorithm.setId(algorithmDto.getAlgorithmId());
            // 修改算法
            this.updateById(algorithm);
            // 修改算法代码
            AlgorithmCode algorithmCode = new AlgorithmCode();
            algorithmCode.setAlgorithmId(algorithmDto.getAlgorithmId());
            algorithmCode.setEditType(algorithmDto.getEditType());
            algorithmCode.setCalculateContractCode(algorithmDto.getCalculateContractCode());
            algorithmCodeService.updateAlgorithmCode(algorithmCode);
        } catch (Exception e) {
            log.error("updateAlgorithm--修改算法失败, 错误信息:{}", e.getMessage());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.UPDATE_ALG_ERROR.getMsg());
        }
    }

    @Override
    public List<AlgorithmDto> queryAlgorithmList(String algorithmName) {
        return this.baseMapper.queryAlgorithmList(algorithmName);
    }

    @Override
    public AlgorithmDto queryAlgorithmDetails(Long algorithmId) {
        try {
            return this.baseMapper.queryAlgorithmDetails(algorithmId);
        } catch (Exception e) {
            log.error("queryAlgorithmDetails--查询算法详情失败, 错误信息:{}", e.getMessage());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.QUERY_ALG_DETAILS_ERROR.getMsg());
        }
    }

    @Override
    public List<Map<String, Object>> queryAlgorithmTreeList() {
        // 造三个父类型节点
        List<Map<String, Object>> treeList = new ArrayList<>(SysConstant.INT_4);
        Map<String, Object> param1 = new HashMap<>(SysConstant.INT_4);
        param1.put("algorithmId", SysConstant.INT_1);
        param1.put("algorithmName", AlgorithmTypeEnum.ALGORITHM_TYPE_1.getName());
        Map<String, Object> param2 = new HashMap<>(SysConstant.INT_4);
        param2.put("algorithmId", SysConstant.INT_2);
        param2.put("algorithmName", AlgorithmTypeEnum.ALGORITHM_TYPE_2.getName());
        Map<String, Object> param3 = new HashMap<>(SysConstant.INT_4);
        param3.put("algorithmId", SysConstant.INT_3);
        param3.put("algorithmName", AlgorithmTypeEnum.ALGORITHM_TYPE_3.getName());
        List<AlgorithmDto> algorithmDtoList = this.baseMapper.queryAlgorithmList(null);
        if (algorithmDtoList == null || algorithmDtoList.size() == 0) {
            param1.put("algorithmName", AlgorithmTypeEnum.ALGORITHM_TYPE_1.getDesc());
            param2.put("algorithmName", AlgorithmTypeEnum.ALGORITHM_TYPE_2.getDesc());
            param3.put("algorithmName", AlgorithmTypeEnum.ALGORITHM_TYPE_3.getDesc());
            treeList.add(param1);
            treeList.add(param2);
            treeList.add(param3);
            return treeList;
        }
        // 处理子节点
        List<Map<String, Object>> childList1 = new ArrayList<>(SysConstant.INT_3);
        List<Map<String, Object>> childList2 = new ArrayList<>(SysConstant.INT_3);
        List<Map<String, Object>> childList3 = new ArrayList<>(SysConstant.INT_3);
        for (AlgorithmDto algorithmDto : algorithmDtoList) {
            if (SysConstant.INT_1 == algorithmDto.getAlgorithmType()) {
                Map<String, Object> param = new HashMap<>(SysConstant.INT_4);
                param.put("algorithmId", algorithmDto.getAlgorithmId());
                param.put("algorithmName", algorithmDto.getAlgorithmName());
                childList1.add(param);

            }
            if (SysConstant.INT_2 == algorithmDto.getAlgorithmType()) {
                Map<String, Object> param = new HashMap<>(SysConstant.INT_4);
                param.put("algorithmId", algorithmDto.getAlgorithmId());
                param.put("algorithmName", algorithmDto.getAlgorithmName());
                childList2.add(param);
            }
            if (SysConstant.INT_3 == algorithmDto.getAlgorithmType()) {
                Map<String, Object> param = new HashMap<>(SysConstant.INT_4);
                param.put("algorithmId", algorithmDto.getAlgorithmId());
                param.put("algorithmName", algorithmDto.getAlgorithmName());
                childList3.add(param);
            }
        }
        // 开发中的父节点，没有子节点算法
        if (childList1.size() == 0) {
            param1.put("algorithmName", AlgorithmTypeEnum.ALGORITHM_TYPE_1.getDesc());
        }
        if (childList2.size() == 0) {
            param2.put("algorithmName", AlgorithmTypeEnum.ALGORITHM_TYPE_2.getDesc());
        }
        if (childList3.size() == 0) {
            param3.put("algorithmName", AlgorithmTypeEnum.ALGORITHM_TYPE_3.getDesc());
        }
        param1.put("child", childList1);
        param2.put("child", childList2);
        param3.put("child", childList3);
        treeList.add(param1);
        treeList.add(param2);
        treeList.add(param3);
        return treeList;
    }

    @Override
    public Long copySaveAlgorithm(WorkflowNode oldNode) {
        AlgorithmDto algorithmDto = this.queryAlgorithmDetails(oldNode.getAlgorithmId());
        if (Objects.isNull(algorithmDto)) {
            return null;
        }
        Algorithm newAlgorithm = BeanUtil.toBean(algorithmDto, Algorithm.class);
        newAlgorithm.setAuthor(UserContext.get() == null ? "" : UserContext.get().getUserName());
        this.save(newAlgorithm);
        return newAlgorithm.getId();
    }

    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }

}
