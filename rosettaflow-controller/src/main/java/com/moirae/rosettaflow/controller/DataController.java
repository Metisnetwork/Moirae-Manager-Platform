package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.mapper.domain.MetaDataColumn;
import com.moirae.rosettaflow.mapper.enums.MetaDataAuthTypeEnum;
import com.moirae.rosettaflow.req.data.*;
import com.moirae.rosettaflow.service.DataService;
import com.moirae.rosettaflow.utils.ConvertUtils;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.data.*;
import com.moirae.rosettaflow.vo.org.OrgUserChooseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    private DataService dataService;

    @GetMapping("listMetaDataColumn")
    @ApiOperation(value = "根据元数据ID,查询元数据的列定义", notes = "根据元数据ID,查询元数据的列定义")
    public ResponseVo<PageVo<MetaDataColumnsVo>> listMetaDataColumn(@Valid MetaDataColumnByIdReq req) {
        IPage<MetaDataColumn> page = dataService.listMetaDataColumn(req.getCurrent(), req.getSize(), req.getMetaDataId());
        List<MetaDataColumnsVo> organizationVoList = BeanUtil.copyToList(page.getRecords(), MetaDataColumnsVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, organizationVoList));
    }

    @GetMapping("getDataFile")
    @ApiOperation(value = "根据元数据ID,查询数据文件信息(带参与任务数)", notes = "根据元数据ID,查询数据文件信息(带参与任务数)")
    public ResponseVo<MetaDataDetailsVo> getDataFile(@Valid MetaDataByIdReq req) {
        MetaDataDto metaDataDto = dataService.getDataFile(req.getMetaDataId());
        return ResponseVo.createSuccess(BeanUtil.toBean(metaDataDto, MetaDataDetailsVo.class));
    }

    @GetMapping("listDataFileByIdentityId")
    @ApiOperation(value = "查询某个组织的数据文件列表(带参与任务数)", notes = "查询某个组织的数据文件列表(带参与任务数)")
    public ResponseVo<PageVo<MetaDataVo>> listMetadataByIdentityId(@Valid MetaDataByIdentityIdReq req) {
        IPage<MetaDataDto> page = dataService.listDataFileByIdentityId(req.getCurrent(), req.getSize(), req.getIdentityId());
        List<MetaDataVo> orgMetaDataVoList = BeanUtil.copyToList(page.getRecords(), MetaDataVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, orgMetaDataVoList));
    }

    @GetMapping("pageList")
    @ApiOperation(value = "获取元数据列表", notes = "获取元数据列表")
    public ResponseVo<PageVo<MetaDataOfMarketplaceVo>> list(@Valid MetaDataReq req) {
        IPage<MetaDataDto> page = dataService.list(req.getCurrent(), req.getSize(), req.getDataName());
        List<MetaDataOfMarketplaceVo> metaDataOfMarketplaceVoList = BeanUtil.copyToList(page.getRecords(), MetaDataOfMarketplaceVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, metaDataOfMarketplaceVoList));
    }

    @GetMapping(value = "detail")
    @ApiOperation(value = "获取元数据详情", notes = "获取元数据详情")
    public ResponseVo<MetaDataAuthDetailsVo> detail(@Validated MetaDataDetailDescribeReq detailReq) {
        MetaDataDto metaDataDto = dataService.getMetaDataAuthDetails(detailReq.getUserMetaDataId());
        return ResponseVo.createSuccess(BeanUtil.copyProperties(metaDataDto, MetaDataAuthDetailsVo.class));
    }

    @GetMapping("listByOwner")
    @ApiOperation(value = "我的资源列表", notes = "我的资源列表")
    public ResponseVo<PageVo<MetaDataAuthVo>> listByOwner(@Valid MetaDataReq req) {
        IPage<MetaDataDto> page = dataService.listMetaDataAuth(req.getCurrent(), req.getSize(), req.getDataName());
        List<MetaDataAuthVo> itemList = BeanUtil.copyToList(page.getRecords(), MetaDataAuthVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

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
        dataService.apply(metaDataAuthReq.getMetaDataId(), MetaDataAuthTypeEnum.find(metaDataAuthReq.getAuthType().intValue()), metaDataAuthReq.getAuthBeginTime(), metaDataAuthReq.getAuthEndTime(), metaDataAuthReq.getAuthValue(), metaDataAuthReq.getSign());
        return ResponseVo.createSuccess();
    }

    @PostMapping("revoke")
    @ApiOperation(value = "元数据撤销授权", notes = "元数据撤销授权")
    public ResponseVo<?> revoke(@RequestBody @Valid MetaDataRevokeReq req) {
        dataService.revoke(req.getMetadataAuthId(), req.getSign());
        return ResponseVo.createSuccess();
    }

    @GetMapping("getAllAuthOrganization")
    @ApiOperation(value = "查询工作流输入组织", notes = "查询工作流输入组织")
    public ResponseVo<List<OrgUserChooseVo>> getAllAuthOrganization() {
        List<MetaDataDto> metaDataDtoList = dataService.getOrgChooseListByMetaDataAuth();
        return ResponseVo.createSuccess(BeanUtil.copyToList(metaDataDtoList, OrgUserChooseVo.class));
    }

    @GetMapping("getAllAuthTables")
    @ApiOperation(value = "查询工作流输入表", notes = "查询工作流输入表")
    public ResponseVo<List<MetaDataAuthChooseVo>> getAllAuthTables(@Valid MetaDataAuthTablesReq metaDataAuthTablesReq) {
        List<MetaDataDto> dtoList = dataService.getMetaDataByChoose(metaDataAuthTablesReq.getIdentityId());
        return ResponseVo.createSuccess(BeanUtil.copyToList(dtoList, MetaDataAuthChooseVo.class));
    }

    @GetMapping("getAllAuthColumns/{metaDataId}")
    @ApiOperation(value = "查询工作流输入字段", notes = "查询工作流输入字段")
    public ResponseVo<List<MetaDataColumnsChooseVo>> getAllAuthColumns(@ApiParam(value = "元数据表ID", required = true) @PathVariable String metaDataId) {
        List<MetaDataColumn> dtoList = dataService.listMetaDataColumnAll(metaDataId);
        return ResponseVo.createSuccess(BeanUtil.copyToList(dtoList, MetaDataColumnsChooseVo.class));
    }
}
