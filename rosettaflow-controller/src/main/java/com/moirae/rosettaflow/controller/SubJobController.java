package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.common.enums.JobActionStatusEnum;
import com.moirae.rosettaflow.dto.SubJobDto;
import com.moirae.rosettaflow.req.job.ActionJobReq;
import com.moirae.rosettaflow.req.job.DeleteBatchSubJobReq;
import com.moirae.rosettaflow.req.job.ListSubJobReq;
import com.moirae.rosettaflow.service.ISubJobService;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.job.SubJobVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author juzix
 * @description 子作业管理
 */
@Slf4j
@RestController
@Api(tags = "子作业管理关接口")
@RequestMapping(value = "subJob", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubJobController {

    @Resource
    private ISubJobService subJobService;

    @PostMapping("list")
    @ApiOperation(value = "查询子作业分页列表", notes = "查询子作业分页列表")
    public ResponseVo<PageVo<SubJobVo>> listSubJob(@RequestBody @Valid ListSubJobReq listSubJobReq) {
        IPage<SubJobDto> jobDtoPage = subJobService.sublist(listSubJobReq.getCurrent(), listSubJobReq.getSize(), listSubJobReq.getSubJobId(), listSubJobReq.getJobId());
        return convertToSubJobVo(jobDtoPage);
    }

    @PostMapping("action")
    @ApiOperation(value = "操作子作业", notes = "操作子作业")
    public ResponseVo<?> actionJob(@RequestBody @Valid ActionJobReq actionSubJobReq) {
        if (actionSubJobReq.getActionType() == JobActionStatusEnum.PAUSE.getValue()) {
            subJobService.pause(actionSubJobReq.getId());
        } else {
            subJobService.reStart(actionSubJobReq.getId());
        }
        return ResponseVo.createSuccess();
    }

    @PostMapping("deleteBatch")
    @ApiOperation(value = "批量删除子作业", notes = "批量删除子作业")
    public ResponseVo<?> actionJob(@RequestBody @Valid DeleteBatchSubJobReq idsReq) {
        subJobService.deleteBatchSubJob(idsReq.getSubJobIds());
        return ResponseVo.createSuccess();
    }

    private ResponseVo<PageVo<SubJobVo>> convertToSubJobVo(IPage<SubJobDto> pageDto) {
        List<SubJobVo> items = new ArrayList<>();
        pageDto.getRecords().forEach(dto -> items.add(BeanUtil.copyProperties(dto, SubJobVo.class)));

        PageVo<SubJobVo> pageVo = new PageVo<>();
        pageVo.setCurrent(pageDto.getCurrent());
        pageVo.setItems(items);
        pageVo.setSize(pageDto.getSize());
        pageVo.setTotal(pageDto.getTotal());
        return ResponseVo.createSuccess(pageVo);
    }

}
