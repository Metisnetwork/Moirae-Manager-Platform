package com.moirae.rosettaflow.service.impl;

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
import com.moirae.rosettaflow.mapper.domain.Algorithm;
import com.moirae.rosettaflow.mapper.domain.AlgorithmType;
import com.moirae.rosettaflow.service.IAlgorithmService;
import com.moirae.rosettaflow.service.IAlgorithmTypeService;
import lombok.extern.slf4j.Slf4j;
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
    IAlgorithmTypeService algorithmTypeService;

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
    public List<Map<String, Object>> queryAlgorithmTreeList(String language) {
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
            // 处理国际化语言
            param.put("algorithmName", SysConstant.EN_US.equals(language) ? type.getAlgorithmTypeNameEn() : type.getAlgorithmTypeName());

            List<Map<String, Object>> childList = new ArrayList<>(SysConstant.INT_3);
            Iterator<AlgorithmDto> it = algorithmDtoList.iterator();
            AlgorithmDto algorithmDto;
            while (it.hasNext()) {
                algorithmDto = it.next();
                // 处理国际化语言
                if (SysConstant.EN_US.equals(language)) {
                    algorithmDto.setAlgorithmName(algorithmDto.getAlgorithmNameEn());
                    algorithmDto.setAlgorithmDesc(algorithmDto.getAlgorithmDescEn());
                }
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
}
