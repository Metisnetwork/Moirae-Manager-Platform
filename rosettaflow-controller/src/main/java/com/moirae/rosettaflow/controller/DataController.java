package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.*;
import com.moirae.rosettaflow.common.utils.BeanCopierUtils;
import com.moirae.rosettaflow.dto.*;
import com.moirae.rosettaflow.mapper.domain.MetaDataColumn;
import com.moirae.rosettaflow.req.data.*;
import com.moirae.rosettaflow.service.DataService;
import com.moirae.rosettaflow.service.IMetaDataOldService;
import com.moirae.rosettaflow.service.IUserMetaDataService;
import com.moirae.rosettaflow.utils.ConvertUtils;
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
    private IMetaDataOldService metaDataService;
    @Resource
    private IUserMetaDataService userMetaDataService;
    @Resource
    private DataService dataService;

    @GetMapping("listMetaDataColumn")
    @ApiOperation(value = "根据元数据ID,查询元数据的列定义", notes = "根据元数据ID,查询元数据的列定义")
    public ResponseVo<PageVo<MetaDataColumnsVo>> listMetaDataColumn(@Valid MetaDataColumnByIdReq metaDataColumnByIdReq) {
        IPage<MetaDataColumn> page = dataService.listMetaDataColumn(metaDataColumnByIdReq.getCurrent(), metaDataColumnByIdReq.getSize(), metaDataColumnByIdReq.getMetaDataId());
        List<MetaDataColumnsVo> organizationVoList = BeanUtil.copyToList(page.getRecords(), MetaDataColumnsVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, organizationVoList));
    }

    @GetMapping("getDataFile")
    @ApiOperation(value = "根据元数据ID,查询数据文件信息(带参与任务数)", notes = "根据元数据ID,查询数据文件信息(带参与任务数)")
    public ResponseVo<MetaDataDetailsVo> getDataFile(@Valid MetaDataByIdReq metaDataByIdReq) {
        MetaDataDto metaDataDto = dataService.getDataFile(metaDataByIdReq.getMetaDataId());
        return ResponseVo.createSuccess(BeanUtil.toBean(metaDataDto, MetaDataDetailsVo.class));
    }

    @GetMapping("listDataFileByIdentityId")
    @ApiOperation(value = "查询某个组织的数据文件列表(带参与任务数)", notes = "查询某个组织的数据文件列表(带参与任务数)")
    public ResponseVo<PageVo<MetaDataVo>> listMetadataByIdentityId(@Valid MetaDataByIdentityIdReq metaDataByIdentityIdReq) {
        IPage<MetaDataDto> page = dataService.listDataFileByIdentityId(metaDataByIdentityIdReq.getCurrent(), metaDataByIdentityIdReq.getSize(), metaDataByIdentityIdReq.getIdentityId());
        List<MetaDataVo> orgMetaDataVoList = BeanUtil.copyToList(page.getRecords(), MetaDataVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, orgMetaDataVoList));
    }

    @GetMapping("pageList")
    @ApiOperation(value = "获取元数据列表", notes = "获取元数据列表")
    public ResponseVo<PageVo<MetaDataOldVo>> list(@Valid MetaDataReq metaDataReq) {
        IPage<MetaDataDtoOld> servicePage = metaDataService.list(metaDataReq.getCurrent(), metaDataReq.getSize(), metaDataReq.getDataName());
        return this.convertToMetaDataVo(servicePage);
    }

    @GetMapping(value = "detail")
    @ApiOperation(value = "获取元数据详情", notes = "获取元数据详情")
    public ResponseVo<MetaDataDetailVo> detail(@Validated MetaDataDetailDescribeReq detailReq) {
        MetaDataDtoOld metaDataDto = metaDataService.detail(detailReq.getMetaDataPkId(), detailReq.getUserMetaDataId());
        return ResponseVo.createSuccess(BeanUtil.copyProperties(metaDataDto, MetaDataDetailVo.class));
    }

    @GetMapping("listByOwner")
    @ApiOperation(value = "我的资源列表", notes = "我的资源列表")
    public ResponseVo<PageVo<UserMetaDataVo>> listByOwner(@Valid MetaDataReq metaDataReq) {
        IPage<UserMetaDataDto> servicePage = userMetaDataService.list(metaDataReq.getCurrent(), metaDataReq.getSize(), metaDataReq.getDataName());
        return convertUserMetaDataToResponseVo(servicePage);
    }

    //TODO waitTest
    @GetMapping("columnList")
    @ApiOperation(value = "获取元数据列分页列表", notes = "获取元数据列分页列表")
    public ResponseVo<PageVo<MetaDataColumnsVo>> columnList(@Valid MetaDataDetailReq metaDataDetailReq) {
        IPage<MetaDataColumn> page = dataService.listMetaDataColumn(metaDataDetailReq.getCurrent(), metaDataDetailReq.getSize(), metaDataDetailReq.getMetaDataId());
        List<MetaDataColumnsVo> organizationVoList = BeanUtil.copyToList(page.getRecords(), MetaDataColumnsVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, organizationVoList));
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
        List<MetaDataDtoOld> dtoList = userMetaDataService.getAllAuthTables(metaDataAuthTablesReq.getIdentityId());
        return ResponseVo.createSuccess(BeanUtil.copyToList(dtoList, MetaDataTablesVo.class));
    }

    //TODO
    @GetMapping("getAllAuthColumns/{metaDataId}")
    @ApiOperation(value = "查询工作流输入字段", notes = "查询工作流输入字段")
    public ResponseVo<List<MetaDataColumnsChooseVo>> getAllAuthColumns(@ApiParam(value = "元数据表ID", required = true) @PathVariable String metaDataId) {
        List<MetaDataColumn> dtoList = dataService.listMetaDataColumnAll(metaDataId);
        return ResponseVo.createSuccess(BeanUtil.copyToList(dtoList, MetaDataColumnsChooseVo.class));
    }

    private ResponseVo<PageVo<MetaDataOldVo>> convertToMetaDataVo(IPage<MetaDataDtoOld> pageDto) {
        List<MetaDataOldVo> items = new ArrayList<>();
        pageDto.getRecords().forEach(metaDataDto -> {
            MetaDataOldVo metaDataVo = BeanUtil.copyProperties(metaDataDto, MetaDataOldVo.class);
            metaDataVo.setAuthStatus(metaDataService.dealAuthStatus(metaDataDto.getAuthStatus(), metaDataDto.getAuthMetadataState()));
            items.add(metaDataVo);
        });
        PageVo<MetaDataOldVo> pageVo = new PageVo<>();
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
            userMetaDataVo.setActionShow(getActionShow(userMetaDataVo.getAuthStatus(), userMetaDataDto.getDataStatus()));
            items.add(userMetaDataVo);
        });

        PageVo<UserMetaDataVo> pageVo = new PageVo<>();
        BeanUtil.copyProperties(pageDto, pageVo);
        pageVo.setItems(items);
        return ResponseVo.createSuccess(pageVo);
    }

    /**
     *
     *  0-查看详情, 1-重新申请, 2-撤销申请
     * @param authStatus
     * @param dataStatus
     * @return
     */
    private Integer getActionShow(Byte authStatus, Byte dataStatus) {
        if(authStatus == UserMetaDataAuditEnum.AUDIT_REFUSED.getValue() && dataStatus == MetaDataStateEnum.MetaDataState_Released.getValue() ){
            return 1;
        }
        if(authStatus == UserMetaDataAuditEnum.AUDIT_PENDING.getValue()){
            return 2;
        }
        return 0;
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
