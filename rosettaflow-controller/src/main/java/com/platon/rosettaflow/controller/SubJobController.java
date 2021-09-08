package com.platon.rosettaflow.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platon.rosettaflow.common.enums.JobActionStatusEnum;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.SubJobDto;

import com.platon.rosettaflow.req.job.ActionJobReq;
import com.platon.rosettaflow.req.job.ListSubJobReq;
import com.platon.rosettaflow.service.ISubJobService;
import com.platon.rosettaflow.vo.PageVo;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.job.SubJobVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping(value = "subjob", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubJobController {

    @Resource
    private ISubJobService subJobService;


    @PostMapping("list")
    @ApiOperation(value = "查询子作业分页列表", notes = "查询子作业分页列表")
    public ResponseVo<PageVo<SubJobVo>> listSubJob(@RequestBody @Valid ListSubJobReq listSubJobReq) {
        IPage<SubJobDto> jobDtoIpage = subJobService.sublist(listSubJobReq.getCurrent(), listSubJobReq.getSize(), listSubJobReq.getSubJobId(), listSubJobReq.getJobId());
        return convertToSubJobVo(jobDtoIpage);
    }


    @PostMapping("action")
    @ApiOperation(value = "操作作业", notes = "操作作业")
    public ResponseVo<?> actionJob(@RequestBody @Valid ActionJobReq actionJobReq) {
        if(actionJobReq.getActionType() == JobActionStatusEnum.PAUSE.getValue()){
            subJobService.pause(actionJobReq.getId());
        } else {
            subJobService.reStart(actionJobReq.getId());
        }
        return ResponseVo.createSuccess();
    }

    private ResponseVo<PageVo<SubJobVo>> convertToSubJobVo(IPage<SubJobDto> pageDto) {
        List<SubJobVo> items = new ArrayList<>();
        pageDto.getRecords().forEach(dto -> {
            SubJobVo vo = new SubJobVo();
            BeanCopierUtils.copy(dto, vo);
            items.add(vo);
        });
        PageVo<SubJobVo> pageVo = new PageVo<>();
        pageVo.setCurrent(pageDto.getCurrent());
        pageVo.setItems(items);
        pageVo.setSize(pageDto.getSize());
        pageVo.setTotal(pageDto.getTotal());
        return ResponseVo.createSuccess(pageVo);
    }



}
