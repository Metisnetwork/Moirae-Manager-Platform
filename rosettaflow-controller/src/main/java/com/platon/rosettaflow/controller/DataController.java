package com.platon.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.MetaDataDetailsDto;
import com.platon.rosettaflow.dto.MetaDataDto;
import com.platon.rosettaflow.dto.UserMetaDataDto;
import com.platon.rosettaflow.req.data.MetaDataAuthReq;
import com.platon.rosettaflow.req.data.MetaDataAuthTablesReq;
import com.platon.rosettaflow.req.data.MetaDataDetailReq;
import com.platon.rosettaflow.req.data.MetaDataReq;
import com.platon.rosettaflow.service.IMetaDataDetailsService;
import com.platon.rosettaflow.service.IMetaDataService;
import com.platon.rosettaflow.service.IUserMetaDataService;
import com.platon.rosettaflow.vo.PageVo;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.data.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/25
 * @description 数据管理
 */
@Slf4j
@RestController
@Api(tags = "数据管理关接口")
@RequestMapping(value = "data", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataController {

    @Resource
    private IMetaDataService metaDataService;

    @Resource
    private IUserMetaDataService userMetaDataService;

    @Resource
    private IMetaDataDetailsService metaDataDetailsService;

    @GetMapping("pageList")
    @ApiOperation(value = "获取元数据列表", notes = "获取元数据列表")
    public ResponseVo<PageVo<MetaDataVo>> list(@Valid MetaDataReq metaDataReq) {
        IPage<MetaDataDto> servicePage = metaDataService.list(metaDataReq.getCurrent(), metaDataReq.getSize(), metaDataReq.getDataName());
        return convertToMetaDataVo(servicePage);
    }

    @GetMapping(value = "detail/{id}")
    @ApiOperation(value = "获取元数据详情", notes = "获取元数据详情")
    public ResponseVo<MetaDataDetailVo> detail(@ApiParam(value = "元数据表ID", required = true) @PathVariable Long id) {
        MetaDataDto metaDataDto = metaDataService.detail(id);
        MetaDataDetailVo vo = new MetaDataDetailVo();
        BeanCopierUtils.copy(metaDataDto, vo);
        return ResponseVo.createSuccess(vo);
    }

    @GetMapping("listByOwner")
    @ApiOperation(value = "我的元数据列表", notes = "我的元数据列表")
    public ResponseVo<PageVo<UserMetaDataVo>> listByOwner(@Valid MetaDataReq metaDataReq) {
        IPage<UserMetaDataDto> servicePage = userMetaDataService.list(metaDataReq.getCurrent(), metaDataReq.getSize(), metaDataReq.getDataName());
        return convertUserMetaDataToResponseVo(servicePage);
    }

    @GetMapping("columnList")
    @ApiOperation(value = "获取元数据列分页列表", notes = "获取元数据列分页列表")
    public ResponseVo<PageVo<MetaDataColumnsVo>> columnList(@Valid MetaDataDetailReq metaDataDetailReq) {
        IPage<MetaDataDetailsDto> servicePage = metaDataDetailsService.findByMetaDataId(metaDataDetailReq.getMetaDataId(), metaDataDetailReq.getCurrent(), metaDataDetailReq.getSize());
        return convertToResponseVo(servicePage);
    }

    @PostMapping("auth")
    @ApiOperation(value = "元数据申请授权", notes = "元数据申请授权")
    public ResponseVo<?> auth(@RequestBody @Valid MetaDataAuthReq metaDataAuthReq) {
        UserMetaDataDto userMetaDataDto = new UserMetaDataDto();
        BeanCopierUtils.copy(metaDataAuthReq, userMetaDataDto);
        userMetaDataService.auth(userMetaDataDto);
        return ResponseVo.createSuccess();
    }

    @GetMapping("getAllAuthOrganization")
    @ApiOperation(value = "查询工作流输入组织", notes = "查询工作流输入组织")
    public ResponseVo<List<UserMetaDataOrgVo>> getAllAuthOrganization() {
        List<UserMetaDataDto> dtoList = userMetaDataService.getAllAuthOrganization();
        return ResponseVo.createSuccess(BeanUtil.copyToList(dtoList, UserMetaDataOrgVo.class));
    }

    @GetMapping("getAllAuthTables")
    @ApiOperation(value = "查询工作流输入表", notes = "查询工作流输入表")
    public ResponseVo<List<MetaDataTablesVo>> getAllAuthTables(@Valid MetaDataAuthTablesReq metaDataAuthTablesReq) {
        List<MetaDataDto> dtoList = userMetaDataService.getAllAuthTables(metaDataAuthTablesReq.getIdentityId());
        return ResponseVo.createSuccess(BeanUtil.copyToList(dtoList, MetaDataTablesVo.class));
    }

    @GetMapping("getAllAuthColumns/{metaDataId}")
    @ApiOperation(value = "查询工作流输入字段", notes = "查询工作流输入字段")
    public ResponseVo<List<MetaDataColumnsAuthVo>> getAllAuthColumns(@ApiParam(value = "元数据表ID", required = true) @PathVariable String metaDataId) {
        List<MetaDataDetailsDto> dtoList = metaDataDetailsService.getAllAuthColumns(metaDataId);
        return ResponseVo.createSuccess(BeanUtil.copyToList(dtoList, MetaDataColumnsAuthVo.class));
    }

    private ResponseVo<PageVo<MetaDataVo>> convertToMetaDataVo(IPage<MetaDataDto> pageDto) {
        List<MetaDataVo> items = new ArrayList<>();
        pageDto.getRecords().forEach(dto -> {
            MetaDataVo vo = new MetaDataVo();
            BeanCopierUtils.copy(dto, vo);
            items.add(vo);
        });

        PageVo<MetaDataVo> pageVo = new PageVo<>();
        pageVo.setCurrent(pageDto.getCurrent());
        pageVo.setItems(items);
        pageVo.setSize(pageDto.getSize());
        pageVo.setTotal(pageDto.getTotal());
        return ResponseVo.createSuccess(pageVo);
    }

    private ResponseVo<PageVo<UserMetaDataVo>> convertUserMetaDataToResponseVo(IPage<UserMetaDataDto> pageDto) {
        List<UserMetaDataVo> items = new ArrayList<>();
        pageDto.getRecords().forEach(u -> {
            UserMetaDataVo vo = new UserMetaDataVo();
            BeanCopierUtils.copy(u, vo);
            items.add(vo);
        });

        PageVo<UserMetaDataVo> pageVo = new PageVo<>();
        pageVo.setCurrent(pageDto.getCurrent());
        pageVo.setItems(items);
        pageVo.setSize(pageDto.getSize());
        pageVo.setTotal(pageDto.getTotal());
        return ResponseVo.createSuccess(pageVo);
    }

    private ResponseVo<PageVo<MetaDataColumnsVo>> convertToResponseVo(IPage<MetaDataDetailsDto> pageDto) {
        List<MetaDataColumnsVo> items = new ArrayList<>();
        pageDto.getRecords().forEach(dto -> {
            MetaDataColumnsVo vo = new MetaDataColumnsVo();
            BeanCopierUtils.copy(dto, vo);
            items.add(vo);
        });

        PageVo<MetaDataColumnsVo> pageVo = new PageVo<>();
        pageVo.setCurrent(pageDto.getCurrent());
        pageVo.setItems(items);
        pageVo.setSize(pageDto.getSize());
        pageVo.setTotal(pageDto.getTotal());
        return ResponseVo.createSuccess(pageVo);
    }

}
