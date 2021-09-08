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
import com.platon.rosettaflow.mapper.domain.WorkflowNodeTemp;
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
    public List<Map<String, Object>> queryAlgorithmTreeList() {
//        Long userId = UserContext.get() == null ? null : UserContext.get().getId();
//        if (userId == null ||  userId == 0L) {
//            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_CACHE_LOST_ERROR.getMsg());
//        }
        List<AlgorithmDto> algorithmDtoList = algorithmMapper.queryAlgorithmList(1L, null);
        // 造三个父类型节点
        List<Map<String, Object>> treeList = new ArrayList<>();
        Map<String, Object> param1 = new HashMap<>(4);
        param1.put("algorithmId", 1);
        param1.put("algorithmName", "统计分析");
        Map<String, Object> param2 = new HashMap<>(4);
        param2.put("algorithmId", 2);
        param2.put("algorithmName", "特征工程");
        Map<String, Object> param3 = new HashMap<>(4);
        param3.put("algorithmId", 3);
        param3.put("algorithmName", "机器学习");

        // 处理子节点
        List<Map<String, Object>> childList1 = new ArrayList<>();
        List<Map<String, Object>> childList2 = new ArrayList<>();
        List<Map<String, Object>> childList3 = new ArrayList<>();
        for (AlgorithmDto algorithmDto : algorithmDtoList) {
            if (algorithmDto.getAlgorithmType() == 1) {
                Map<String, Object> param = new HashMap<>(4);
                param.put("algorithmId", algorithmDto.getAlgorithmId());
                param.put("algorithmName", algorithmDto.getAlgorithmName());
                childList1.add(param);

            }
            if (algorithmDto.getAlgorithmType() == 2) {
                Map<String, Object> param = new HashMap<>(4);
                param.put("algorithmId", algorithmDto.getAlgorithmId());
                param.put("algorithmName", algorithmDto.getAlgorithmName());
                childList2.add(param);
            }
            if (algorithmDto.getAlgorithmType() == 3) {
                Map<String, Object> param = new HashMap<>(4);
                param.put("algorithmId", algorithmDto.getAlgorithmId());
                param.put("algorithmName", algorithmDto.getAlgorithmName());
                childList3.add(param);
            }
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
