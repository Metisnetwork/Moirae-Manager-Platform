package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.JobDto;
import com.moirae.rosettaflow.mapper.domain.Job;
import org.apache.ibatis.annotations.Param;

/**
 * t_job
 * @author admin
 */
public interface JobMapper extends BaseMapper<Job> {

    /**
     *  查询作业列表
     * @param jobName : 作业名称
     * @param projectId : 项目id
     * @param page :作业page
     * @return : 作业列表
     */
    IPage<JobDto> queryJobList(@Param("jobName") String jobName, @Param("projectId") Long projectId, IPage<JobDto> page);

    /**
     *  修改作业根据作业id
     * @param job : 作业信息
     * @return :  作业修改成功与否
     */
    int updateJobById(Job job);
}