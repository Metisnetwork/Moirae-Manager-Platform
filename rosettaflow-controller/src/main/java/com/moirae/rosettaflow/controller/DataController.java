package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.AuthTypeEnum;
import com.moirae.rosettaflow.common.utils.BeanCopierUtils;
import com.moirae.rosettaflow.dto.MetaDataDetailsDto;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.dto.UserMetaDataDto;
import com.moirae.rosettaflow.req.data.MetaDataAuthReq;
import com.moirae.rosettaflow.req.data.MetaDataAuthTablesReq;
import com.moirae.rosettaflow.req.data.MetaDataDetailReq;
import com.moirae.rosettaflow.req.data.MetaDataReq;
import com.moirae.rosettaflow.service.IMetaDataDetailsService;
import com.moirae.rosettaflow.service.IMetaDataService;
import com.moirae.rosettaflow.service.IUserMetaDataService;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.data.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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

    @GetMapping(value = "detail/{metaDataId}")
    @ApiOperation(value = "获取元数据详情", notes = "获取元数据详情")
    public ResponseVo<MetaDataDetailVo> detail(@ApiParam(value = "元数据表metaDataId", required = true) @PathVariable String metaDataId) {
        MetaDataDto metaDataDto = metaDataService.detail(metaDataId);
        return ResponseVo.createSuccess(BeanUtil.copyProperties(metaDataDto, MetaDataDetailVo.class));
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
        pageDto.getRecords().forEach(u -> items.add(BeanUtil.copyProperties(u, MetaDataVo.class)));

        PageVo<MetaDataVo> pageVo = new PageVo<>();
        BeanUtil.copyProperties(pageDto, pageVo);
        pageVo.setItems(items);
        return ResponseVo.createSuccess(pageVo);
    }

    private ResponseVo<PageVo<UserMetaDataVo>> convertUserMetaDataToResponseVo(IPage<UserMetaDataDto> pageDto) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(SysConstant.DEFAULT_TIME_PATTERN);
        List<UserMetaDataVo> items = new ArrayList<>();
        pageDto.getRecords().forEach(userMetaDataDto -> {
            UserMetaDataVo userMetaDataVo = new UserMetaDataVo();
            BeanCopierUtils.copy(userMetaDataDto, userMetaDataVo);
            String authTime = "";
            if(!Objects.isNull(userMetaDataDto.getAuthBeginTime()) && !Objects.isNull(userMetaDataDto.getAuthEndTime())){
                authTime = dateFormat.format(userMetaDataDto.getAuthBeginTime()) + "~" + dateFormat.format(userMetaDataDto.getAuthEndTime());
            }
            userMetaDataVo.setAuthValueStr(userMetaDataDto.getAuthType() == AuthTypeEnum.NUMBER.getValue() ? String.valueOf(userMetaDataDto.getAuthValue()) :  authTime);
            items.add(userMetaDataVo);
        });

        PageVo<UserMetaDataVo> pageVo = new PageVo<>();
        BeanUtil.copyProperties(pageDto, pageVo);
        pageVo.setItems(items);
        return ResponseVo.createSuccess(pageVo);
    }

    private ResponseVo<PageVo<MetaDataColumnsVo>> convertToResponseVo(IPage<MetaDataDetailsDto> pageDto) {
        List<MetaDataColumnsVo> items = new ArrayList<>();
        pageDto.getRecords().forEach(u -> items.add(BeanUtil.copyProperties(u, MetaDataColumnsVo.class)));

        PageVo<MetaDataColumnsVo> pageVo = new PageVo<>();
        BeanUtil.copyProperties(pageDto, pageVo);
        pageVo.setItems(items);
        return ResponseVo.createSuccess(pageVo);
    }

}
