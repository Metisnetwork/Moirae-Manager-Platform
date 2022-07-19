package com.datum.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.datum.platform.mapper.domain.MetaData;
import com.datum.platform.mapper.domain.MetaDataCertificate;
import com.datum.platform.mapper.domain.Model;
import com.datum.platform.req.data.*;
import com.datum.platform.req.model.GetUserModelListReq;
import com.datum.platform.req.org.OrgIdPageReq;
import com.datum.platform.service.DataService;
import com.datum.platform.service.dto.data.*;
import com.datum.platform.utils.ConvertUtils;
import com.datum.platform.vo.PageVo;
import com.datum.platform.vo.ResponseVo;
import com.datum.platform.vo.data.*;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@ApiSupport(order = 400)
@RequestMapping(value = "data", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataController {

    @Resource
    private DataService dataService;

    @GetMapping("getDataStats")
    @ApiOperation(value = "查询数据统计", notes = "查询数据统计" )
    @ApiOperationSupport(order = 1)
    public ResponseVo<DataStatsVo> getDataStats() {
        int dataCount = dataService.statisticsOfGlobal().getTotalCount();
        DataStatsVo dataStatsVo = new DataStatsVo();
        dataStatsVo.setDataCount(dataCount);
        return ResponseVo.createSuccess(dataStatsVo);
    }

    @GetMapping("getDataListByOrg")
    @ApiOperation(value = "查询数据列表通过组织id", notes = "查询数据列表通过组织id")
    @ApiOperationSupport(order = 2)
    public ResponseVo<PageVo<DataVo>> getDataListByOrg(@Valid OrgIdPageReq req) {
        IPage<MetaData> page = dataService.listMetaDataByOrg(req.getCurrent(), req.getSize(), req.getIdentityId());
        List<DataVo> itemList = BeanUtil.copyToList(page.getRecords(), DataVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getDataList")
    @ApiOperation(value = "查询数据列表", notes = "查询数据列表")
    @ApiOperationSupport(order = 3)
    public ResponseVo<PageVo<DataVo>> getDataList(@Valid GetDataListReq req) {
        IPage<MetaData> page = dataService.listMetaDataByCondition(req.getCurrent(), req.getSize(), req.getKeyword(), req.getIndustry(), req.getFileType(), req.getMinSize(), req.getMaxSize(), req.getOrderBy());
        List<DataVo> itemList = BeanUtil.copyToList(page.getRecords(), DataVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getDataListByUser")
    @ApiOperation(value = "查询用户的数据列表", notes = "查询用户的数据列表")
    @ApiOperationSupport(order = 4)
    public ResponseVo<PageVo<DataVo>> getDataListByUser(@Valid GetDataListByIdentityIdReq req) {
        IPage<MetaData> page = dataService.getUserDataList(req.getCurrent(), req.getSize(), req.getIdentityId(), req.getKeyword());
        List<DataVo> itemList = BeanUtil.copyToList(page.getRecords(), DataVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getUserAuthDataList")
    @ApiOperation(value = "查询用户的授权数据列表", notes = "查询用户的授权数据列表")
    @ApiOperationSupport(order = 5)
    public ResponseVo<PageVo<UserAuthDataVo>> getUserAuthDataList(@Valid GetUserAuthDataListReq req) {
        IPage<MetaData> page = dataService.getUserAuthDataList(req.getCurrent(), req.getSize(), req.getKeyword());
        List<UserAuthDataVo> itemList = BeanUtil.copyToList(page.getRecords(), UserAuthDataVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getDataDetails")
    @ApiOperation(value = "查询数据详情", notes = "查询数据详情")
    @ApiOperationSupport(order = 6)
    public ResponseVo<DataDetailsVo> getDataDetails(@Valid GetDataDetailsReq req) {
        MetaData data = dataService.getMetaDataById(req.getMetaDataId(), true);
        return ResponseVo.createSuccess(BeanUtil.copyProperties(data, DataDetailsVo.class));
    }

    @GetMapping("getNoAttributeCredential")
    @ApiOperation(value = "查询数据关联的无属性凭证", notes = "查询数据关联的无属性凭证")
    @ApiOperationSupport(order = 7)
    public ResponseVo<NoAttributesCredentialDto> getNoAttributeCredential(@Valid GetDataDetailsReq req) {
        MetaDataCertificate certificate = dataService.getNoAttributeCredentialByMetaDataId(req.getMetaDataId());
        if(certificate != null){
            return ResponseVo.createSuccess(BeanUtil.copyProperties(certificate, NoAttributesCredentialDto.class));
        }else{
            return ResponseVo.createSuccess();
        }
    }

    @GetMapping("getAttributeCredentialList")
    @ApiOperation(value = "查询数据关联的有属性凭证列表", notes = "查询数据关联的有属性凭证列表")
    @ApiOperationSupport(order = 8)
    public ResponseVo<PageVo<HaveAttributesCredentialDto>> getAttributeCredentialList(@Valid GetAttributeCredentialListReq req) {
        IPage<MetaDataCertificate> page = dataService.pageHaveAttributesCertificateByMetaDataId(req.getCurrent(), req.getSize(), req.getMetaDataId());
        List<HaveAttributesCredentialDto> itemList = BeanUtil.copyToList(page.getRecords(), HaveAttributesCredentialDto.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getUserDatumNetworkLatInfo")
    @ApiOperation(value = "查询DatumNetworkLat代币信息", notes = "查询DatumNetworkLat代币信息")
    @ApiOperationSupport(order = 9)
    public ResponseVo<UserWLatCredentialDto> getUserDatumNetworkLatInfo() {
        UserWLatCredentialDto resp = dataService.getUserWLatCredential();
        return ResponseVo.createSuccess(resp);
    }

    @GetMapping("getUserNoAttributeCredential")
    @ApiOperation(value = "查询用户数据关联的无属性凭证", notes = "查询用户数据关联的无属性凭证")
    @ApiOperationSupport(order = 10)
    public ResponseVo<UserNoAttributesCredentialDto> getUserNoAttributeCredential(@Valid GetDataDetailsReq req) {
        MetaDataCertificate certificate = dataService.getNoAttributeCredentialByMetaDataIdAndUser(req.getMetaDataId());
        if(certificate != null){
            return ResponseVo.createSuccess(BeanUtil.copyProperties(certificate, UserNoAttributesCredentialDto.class));
        }else{
            return ResponseVo.createSuccess();
        }
    }

    @GetMapping("getUserAttributeCredentialList")
    @ApiOperation(value = "查询用户数据关联的有属性凭证列表", notes = "查询用户数据关联的有属性凭证列表")
    @ApiOperationSupport(order = 11)
    public ResponseVo<PageVo<UserHaveAttributesCredentialDto>> getUserAttributeCredentialList(@Valid GetAttributeCredentialListReq req) {
        IPage<MetaDataCertificate> page = dataService.pageHaveAttributesCertificateByMetaDataIdAndUser(req.getCurrent(), req.getSize(), req.getMetaDataId());
        List<UserHaveAttributesCredentialDto> itemList = BeanUtil.copyToList(page.getRecords(), UserHaveAttributesCredentialDto.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getUserModelList")
    @ApiOperation(value = "查询用户的模型列表", notes = "查询用户的模型列表")
    @ApiOperationSupport(order = 12)
    public ResponseVo<List<ModelVo>> getUserModelList(@Valid GetUserModelListReq req) {
        List<Model> list =  dataService.listModelByUser(req.getAlgorithmId(), req.getIdentityId());
        return ResponseVo.createSuccess(BeanUtil.copyToList(list, ModelVo.class));
    }
}
