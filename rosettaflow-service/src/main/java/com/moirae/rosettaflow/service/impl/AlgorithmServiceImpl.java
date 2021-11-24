package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.enums.StatusEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.dto.AlgorithmDto;
import com.moirae.rosettaflow.mapper.AlgorithmMapper;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.service.IAlgorithmCodeService;
import com.moirae.rosettaflow.service.IAlgorithmService;
import com.moirae.rosettaflow.service.IAlgorithmTypeService;
import com.moirae.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
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

    @Resource
    IAlgorithmTypeService algorithmTypeService;

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
            if (e instanceof DuplicateKeyException) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ALG_NAME_EXISTED.getMsg());
            }
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
            if (e instanceof DuplicateKeyException) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ALG_NAME_EXISTED.getMsg());
            }
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.UPDATE_ALG_ERROR.getMsg());
        }
    }

    @Override
    public IPage<AlgorithmDto> queryAlgorithmList(Long current, Long size, String algorithmName) {
        Page<AlgorithmDto> page = new Page<>(current, size);
        return this.baseMapper.queryAlgorithmList(page, algorithmName);
    }


    @Override
    public Algorithm getAlgorithmById(Long id) {
        LambdaQueryWrapper<Algorithm> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Algorithm::getId, id);
        queryWrapper.eq(Algorithm::getStatus, StatusEnum.VALID.getValue());
        return this.getOne(queryWrapper);
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
        List<Map<String, Object>> treeList = new ArrayList<>();
        // 获取算法大类
        List<AlgorithmType> algorithmTypeList = algorithmTypeService.list();
        List<AlgorithmDto> algorithmDtoList;
        if (null != algorithmTypeList && algorithmTypeList.size() > 0) {
            algorithmDtoList = this.baseMapper.queryAlgorithmTreeList();
        } else {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ALG_TYPE_NOT_EXIST.getMsg());
        }
        for (AlgorithmType type : algorithmTypeList) {
            Map<String, Object> param = new HashMap<>(SysConstant.INT_2);
            param.put("algorithmId", type.getAlgorithmType());
            param.put("algorithmName", type.getAlgorithmTypeName());

            List<Map<String, Object>> childList = new ArrayList<>(SysConstant.INT_3);
            Iterator<AlgorithmDto> it = algorithmDtoList.iterator();
            AlgorithmDto algorithmDto;
            while (it.hasNext()) {
                algorithmDto = it.next();
                if (algorithmDto.getAlgorithmType() == type.getAlgorithmType().byteValue()) {
                    Map<String, Object> param1 = new HashMap<>(SysConstant.INT_3);
                    param1.put("algorithmId", algorithmDto.getAlgorithmId());
                    param1.put("algorithmName", algorithmDto.getAlgorithmName());
                    param1.put("algorithmDto", algorithmDto);
                    childList.add(param1);
                    it.remove();
                }
            }
            param.put("child", childList);
            treeList.add(param);
        }
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
