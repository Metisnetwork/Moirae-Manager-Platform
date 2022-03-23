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
    private DataService dataService;

    @GetMapping("getDataStats")
    @ApiOperation(value = "查询数据统计", notes = "查询数据统计")
    public ResponseVo<DataStatsVo> getDataStats() {
        return ResponseVo.createSuccess(new DataStatsVo());
    }

    @GetMapping("getDataList")
    @ApiOperation(value = "查询数据列表", notes = "查询数据列表")
    public ResponseVo<PageVo<DataVo>> getDataList(@Valid GetDataListReq req) {
        List<DataVo> orgTaskVoList = new ArrayList<>();
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(null, orgTaskVoList));
    }

    @GetMapping("getDataDetails")
    @ApiOperation(value = "查询数据详情", notes = "查询数据详情")
    public ResponseVo<DataDetailsVo> getDataDetails(@Valid GetDataDetailsReq req) {
        return ResponseVo.createSuccess(new DataDetailsVo());
    }

    @GetMapping("getUserDataList")
    @ApiOperation(value = "查询用户的数据列表", notes = "查询用户的数据列表(存在余额的)")
    public ResponseVo<PageVo<UserDataVo>> getUserDataList(@Valid GetDataListReq req) {
        List<UserDataVo> orgTaskVoList = new ArrayList<>();
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(null, orgTaskVoList));
    }
}
