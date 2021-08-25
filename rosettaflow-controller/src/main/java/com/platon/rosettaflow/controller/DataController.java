package com.platon.rosettaflow.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.MetaDataDto;
import com.platon.rosettaflow.req.data.MetaDataReq;
import com.platon.rosettaflow.service.IMetaDataService;
import com.platon.rosettaflow.vo.PageVo;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.data.MetaDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("list")
    @ApiOperation(value = "获取元数据列表", notes = "获取元数据列表")
    public ResponseVo<PageVo<MetaDataVo>> list(@RequestBody @Valid MetaDataReq metaDataReq) {
        IPage<MetaDataDto> servicePage = metaDataService.list(metaDataReq.getCurrent(), metaDataReq.getSize());
        return convertToResponseVo(servicePage);
    }

    private ResponseVo<PageVo<MetaDataVo>> convertToResponseVo(IPage<MetaDataDto> pageDto) {
        List<MetaDataVo> items = new ArrayList<>();
        pageDto.getRecords().forEach(metaDataDto -> {
            MetaDataVo metaDataVo = new MetaDataVo();
            BeanCopierUtils.copy(metaDataDto, metaDataVo);
            items.add(metaDataVo);
        });

        PageVo<MetaDataVo> pageVo = new PageVo<>();
        pageVo.setCurrent(pageDto.getCurrent());
        pageVo.setItems(items);
        pageVo.setSize(pageDto.getSize());
        pageVo.setTotal(pageDto.getTotal());
        return ResponseVo.createSuccess(pageVo);
    }
}
