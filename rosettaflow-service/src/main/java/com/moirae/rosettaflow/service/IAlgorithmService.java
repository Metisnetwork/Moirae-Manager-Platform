package com.moirae.rosettaflow.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.AlgorithmDto;
import com.moirae.rosettaflow.mapper.domain.Algorithm;
import com.moirae.rosettaflow.mapper.domain.WorkflowNode;

import java.util.List;
import java.util.Map;

/**
 * @author houz
 */
public interface IAlgorithmService extends IService<Algorithm> {

    /**
     * 新增算法
     * @param algorithmDto 算法请求参数
     */
    void addAlgorithm(AlgorithmDto algorithmDto);

    /**
     * 修改算法
     * @param algorithmDto 算法请求参数
     */
    void updateAlgorithm(AlgorithmDto algorithmDto);

    /**
     * 查询算法列表
     * @param current 当前页数
     * @param size 每页数量
     * @param algorithmName 算法名称
     * @return AlgorithmDto
     */
    IPage<AlgorithmDto> queryAlgorithmList(Long current, Long size, String algorithmName);

    /**
     * 获取算法，通过算法id
     * @param id 算法id
     * @return Algorithm
     */
    Algorithm getAlgorithmById(Long id);

    /**
     * 查询算法详情
     * @param id 算法id
     * @return AlgorithmDto
     */
    AlgorithmDto queryAlgorithmDetails(Long id);

    /**
     * 查询算法树结构
     * @return 数据
     */
    List<Map<String, Object>> queryAlgorithmTreeList();

    /**
     * 复制保存算法
     * @param oldNode 源算法信息
     * @return 新算法id
     */
    Long copySaveAlgorithm(WorkflowNode oldNode);

    /**
     * 清空算法表
     */
    void truncate();
}