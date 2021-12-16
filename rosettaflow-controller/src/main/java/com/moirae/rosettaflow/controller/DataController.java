package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.AuthStatusShowEnum;
import com.moirae.rosettaflow.common.enums.AuthTypeEnum;
import com.moirae.rosettaflow.common.enums.UserMetaDataAuditEnum;
import com.moirae.rosettaflow.common.enums.UserMetaDataAuthorithStateEnum;
import com.moirae.rosettaflow.common.utils.BeanCopierUtils;
import com.moirae.rosettaflow.dto.MetaDataDetailsDto;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.dto.UserMetaDataDto;
import com.moirae.rosettaflow.req.data.*;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

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
        return this.convertToMetaDataVo(servicePage);
    }

    @GetMapping(value = "detail")
    @ApiOperation(value = "获取元数据详情", notes = "获取元数据详情")
    public ResponseVo<MetaDataDetailVo> detail(@Validated MetaDataDetailDescribeReq detailReq) {
        MetaDataDto metaDataDto = metaDataService.detail(detailReq.getMetaDataPkId(), detailReq.getUserMetaDataId());
        return ResponseVo.createSuccess(BeanUtil.copyProperties(metaDataDto, MetaDataDetailVo.class));
    }

    @GetMapping("listByOwner")
    @ApiOperation(value = "我的资源列表", notes = "我的资源列表")
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

    @PostMapping("revoke")
    @ApiOperation(value = "元数据撤销授权", notes = "元数据撤销授权")
    public ResponseVo<?> revoke(@RequestBody @Valid MetaDataRevokeReq metaDataRevokeReq) {
        UserMetaDataDto userMetaDataDto = new UserMetaDataDto();
        BeanCopierUtils.copy(metaDataRevokeReq, userMetaDataDto);
        userMetaDataService.revoke(userMetaDataDto);
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
        pageDto.getRecords().forEach(metaDataDto -> {
            MetaDataVo metaDataVo = BeanUtil.copyProperties(metaDataDto, MetaDataVo.class);
            metaDataVo.setAuthStatus(metaDataService.dealAuthStatus(metaDataDto.getAuthStatus(), metaDataDto.getAuthMetadataState()));
            items.add(metaDataVo);
        });
        PageVo<MetaDataVo> pageVo = new PageVo<>();
        BeanUtil.copyProperties(pageDto, pageVo);
        pageVo.setItems(items);
        return ResponseVo.createSuccess(pageVo);
    }

    private ResponseVo<PageVo<UserMetaDataVo>> convertUserMetaDataToResponseVo(IPage<UserMetaDataDto> pageDto) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(SysConstant.DEFAULT_TIME_PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone(SysConstant.DEFAULT_TIMEZONE));
        List<UserMetaDataVo> items = new ArrayList<>();
        pageDto.getRecords().forEach(userMetaDataDto -> {
            UserMetaDataVo userMetaDataVo = new UserMetaDataVo();
            BeanCopierUtils.copy(userMetaDataDto, userMetaDataVo);
            //授权值
            String authTime = "";
            if(!Objects.isNull(userMetaDataDto.getAuthBeginTime()) && !Objects.isNull(userMetaDataDto.getAuthEndTime())){
                authTime = dateFormat.format(userMetaDataDto.getAuthBeginTime()) + "~" + dateFormat.format(userMetaDataDto.getAuthEndTime());
            }
            userMetaDataVo.setAuthValueStr(userMetaDataDto.getAuthType() == AuthTypeEnum.NUMBER.getValue() ? String.valueOf(userMetaDataDto.getAuthValue()) :  authTime);
            //状态
            userMetaDataVo.setAuthStatusShow(getAuthStatusShow(userMetaDataDto));
            userMetaDataVo.setAuthStatus(metaDataService.dealAuthStatus(userMetaDataDto.getAuthStatus(), userMetaDataDto.getAuthMetadataState()));
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


    /**
     * 获取转换后数据授权状态(前端展示使用)
     * @param userMetaDataDto 用户元数据
     * @return  授权状态
     */
    private Byte getAuthStatusShow (UserMetaDataDto userMetaDataDto) {

        Byte authStatus = userMetaDataDto.getAuthStatus();
        Byte authMetadataState = userMetaDataDto.getAuthMetadataState();
        if (authStatus == UserMetaDataAuditEnum.AUDIT_PENDING.getValue() && authMetadataState == UserMetaDataAuthorithStateEnum.RELEASED.getValue()) {
            return AuthStatusShowEnum.APPLY.getValue();
        } else if (authStatus == UserMetaDataAuditEnum.AUDIT_PASSED.getValue() && authMetadataState == UserMetaDataAuthorithStateEnum.RELEASED.getValue()) {
            return AuthStatusShowEnum.AUTHORIZED.getValue();
        } else if (authStatus == UserMetaDataAuditEnum.AUDIT_REFUSED.getValue() && authMetadataState == UserMetaDataAuthorithStateEnum.INVALID.getValue()) {
            return AuthStatusShowEnum.REFUSE.getValue();
        } else if (authStatus == UserMetaDataAuditEnum.AUDIT_PASSED.getValue() && authMetadataState == UserMetaDataAuthorithStateEnum.INVALID.getValue()) {
            return AuthStatusShowEnum.INVALID.getValue();
        } else if (authMetadataState == UserMetaDataAuthorithStateEnum.INVALID.getValue() || authMetadataState == UserMetaDataAuthorithStateEnum.REVOKED.getValue()) {
            return AuthStatusShowEnum.INVALID.getValue();
        } else {
            return AuthStatusShowEnum.UNKNOWN.getValue();
        }
    }
}
