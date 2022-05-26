package com.datum.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.datum.platform.mapper.domain.MetaData;
import com.datum.platform.mapper.domain.Model;
import com.datum.platform.req.data.GetDataDetailsReq;
import com.datum.platform.req.data.GetDataListByIdentityIdReq;
import com.datum.platform.req.data.GetDataListReq;
import com.datum.platform.req.model.GetUserModelListReq;
import com.datum.platform.req.org.OrgIdPageReq;
import com.datum.platform.service.DataService;
import com.datum.platform.service.dto.data.DatumNetworkLatInfoDto;
import com.datum.platform.utils.ConvertUtils;
import com.datum.platform.vo.PageVo;
import com.datum.platform.vo.ResponseVo;
import com.datum.platform.vo.data.*;
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
@RequestMapping(value = "data", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataController {

    @Resource
    private DataService dataService;

    @GetMapping("getDataStats")
    @ApiOperation(value = "查询数据统计", notes = "查询数据统计")
    public ResponseVo<DataStatsVo> getDataStats() {
        int dataCount = dataService.statisticsOfGlobal().getTotalCount();
        DataStatsVo dataStatsVo = new DataStatsVo();
        dataStatsVo.setDataCount(dataCount);
        return ResponseVo.createSuccess(dataStatsVo);
    }

    @GetMapping("getDataListByOrg")
    @ApiOperation(value = "查询数据列表通过组织id", notes = "查询数据列表通过组织id")
    public ResponseVo<PageVo<DataVo>> getDataListByOrg(@Valid OrgIdPageReq req) {
        IPage<MetaData> page = dataService.listMetaDataByOrg(req.getCurrent(), req.getSize(), req.getIdentityId());
        List<DataVo> itemList = BeanUtil.copyToList(page.getRecords(), DataVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getDataList")
    @ApiOperation(value = "查询数据列表", notes = "查询数据列表")
    public ResponseVo<PageVo<DataVo>> getDataList(@Valid GetDataListReq req) {
        IPage<MetaData> page = dataService.listMetaDataByCondition(req.getCurrent(), req.getSize(), req.getKeyword(), req.getIndustry(), req.getFileType(), req.getMinSize(), req.getMaxSize(), req.getOrderBy());
        List<DataVo> itemList = BeanUtil.copyToList(page.getRecords(), DataVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getDataDetails")
    @ApiOperation(value = "查询数据详情", notes = "查询数据详情")
    public ResponseVo<DataDetailsVo> getDataDetails(@Valid GetDataDetailsReq req) {
        MetaData data = dataService.getMetaDataById(req.getMetaDataId(), true);
        return ResponseVo.createSuccess(BeanUtil.copyProperties(data, DataDetailsVo.class));
    }

    @GetMapping("getUserDataList")
    @ApiOperation(value = "查询用户的数据列表", notes = "查询用户的数据列表(存在余额的)")
    public ResponseVo<PageVo<UserDataVo>> getUserDataList(@Valid GetDataListByIdentityIdReq req) {
        IPage<MetaData> page = dataService.getUserDataList(req.getCurrent(), req.getSize(), req.getIdentityId(), req.getKeyword());
        List<UserDataVo> itemList = BeanUtil.copyToList(page.getRecords(), UserDataVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getUserDatumNetworkLatInfo")
    @ApiOperation(value = "查询DatumNetworkLat代币信息", notes = "查询DatumNetworkLat代币信息")
    public ResponseVo<DatumNetworkLatInfoDto> getUserDatumNetworkLatInfo() {
        DatumNetworkLatInfoDto resp = dataService.getUserDatumNetworkLatInfo();
        return ResponseVo.createSuccess(resp);
    }

    @GetMapping("getUserModelList")
    @ApiOperation(value = "查询当前用户的模型列表", notes = "查询当前用户的模型列表")
    public ResponseVo<List<ModelVo>> getUserModelList(@Valid GetUserModelListReq req) {
        List<Model> list =  dataService.listModelByUser(req.getAlgorithmId(), req.getIdentityId());
        return ResponseVo.createSuccess(BeanUtil.copyToList(list, ModelVo.class));
    }
}
