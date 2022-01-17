package com.moirae.rosettaflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.InputModelEnum;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public Algorithm queryAlgorithmStepDetails(Long algorithmId) {
        return this.baseMapper.queryAlgorithmStepDetails(algorithmId);
    }

    @Override
    public void isValid(List<Long> algorithmIdList) {
        List<Algorithm> algorithmList = getAlgorithmByIdList(algorithmIdList);
        Map<Long, Algorithm> algorithmMap = algorithmList.stream().collect(Collectors.toMap(Algorithm::getId, item -> item));

        // 必须是同一个算法 algorithm_code
        String fristAlgorithmCode = null;
        int preStep = 0;
        for (Long algorithmId : algorithmIdList) {
            // 算法是否存在
            if(!algorithmMap.containsKey(algorithmId)){
                log.error("不存在的算法, id = {}", algorithmId);
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ALG_REPEAT_ERROR.getMsg());
            }
            Algorithm algorithm = algorithmMap.get(algorithmId);
            // 算法类型校验
            if(StringUtils.isNoneBlank(fristAlgorithmCode)){
                if(!StringUtils.equals(fristAlgorithmCode, algorithm.getAlgorithmCode())){
                    log.error("不相同的算法, idList = {}", algorithmIdList);
                    throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ALG_REPEAT_ERROR.getMsg());
                }
            }else{
                fristAlgorithmCode = algorithm.getAlgorithmCode();
            }

            // 算法步骤
            if(algorithm.getAlgorithmStep() < preStep){
                log.error("相同的算法顺序错误, idList = {}", algorithmIdList);
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ALG_REPEAT_ERROR.getMsg());
            }else{
                preStep = algorithm.getAlgorithmStep();
            }
        }
    }

    @Override
    public Algorithm getAlgorithmByIdCode(String algorithmCode, int inputModel) {
        LambdaQueryWrapper<Algorithm> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Algorithm::getAlgorithmCode, algorithmCode);
        queryWrapper.eq(Algorithm::getInputModel, inputModel);
        return this.getOne(queryWrapper);
    }

    private List<Algorithm> getAlgorithmByIdList(List<Long> algorithmIdList) {
        LambdaQueryWrapper<Algorithm> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Algorithm::getId, algorithmIdList);
        queryWrapper.eq(Algorithm::getStatus, StatusEnum.VALID.getValue());
        return this.list(queryWrapper);
    }
}
