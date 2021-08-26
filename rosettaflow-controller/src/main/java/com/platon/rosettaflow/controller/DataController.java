package com.platon.rosettaflow.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.MetaDataDto;
import com.platon.rosettaflow.dto.UserMetaDataDto;
import com.platon.rosettaflow.req.data.MetaDataReq;
import com.platon.rosettaflow.service.IMetaDataService;
import com.platon.rosettaflow.service.IUserMetaDataService;
import com.platon.rosettaflow.vo.PageVo;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.data.MetaDataColumnsVo;
import com.platon.rosettaflow.vo.data.MetaDataDetailVo;
import com.platon.rosettaflow.vo.data.MetaDataVo;
import com.platon.rosettaflow.vo.data.UserMetaDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Resource
    private IUserMetaDataService userMetaDataService;

    @GetMapping("list")
    @ApiOperation(value = "获取元数据列表", notes = "获取元数据列表")
    public ResponseVo<PageVo<MetaDataVo>> list(@Valid MetaDataReq metaDataReq) {
        IPage<MetaDataDto> servicePage = metaDataService.list(metaDataReq.getCurrent(), metaDataReq.getSize());
        return convertToResponseVo(servicePage);
    }

    @GetMapping(value = "/detail/{id}")
    @ApiOperation(value = "获取元数据详情", notes = "获取元数据详情")
    public ResponseVo<MetaDataDetailVo> detail(@ApiParam(value = "项目ID", required = true) @PathVariable Long id) {
        MetaDataDto metaDataDto = metaDataService.detail(id);
        return convertToResponseVo(metaDataDto);
    }

    @GetMapping("listByOwner")
    @ApiOperation(value = "我的元数据列表", notes = "我的元数据列表")
    public ResponseVo<PageVo<UserMetaDataVo>> listByOwner(@Valid MetaDataReq metaDataReq) {
        IPage<UserMetaDataDto> servicePage = userMetaDataService.list(metaDataReq.getCurrent(), metaDataReq.getSize());
        return convertUserMetaDataToResponseVo(servicePage);
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

    private ResponseVo<MetaDataDetailVo> convertToResponseVo(MetaDataDto dto) {
        MetaDataDetailVo metaDataDetailVo = new MetaDataDetailVo();
        BeanCopierUtils.copy(dto, metaDataDetailVo);
        List<MetaDataColumnsVo> columnsVoList = new ArrayList<>();
        dto.getMetaDataDetailsDtoList().forEach(c -> {
            MetaDataColumnsVo cVo = new MetaDataColumnsVo();
            BeanCopierUtils.copy(c, cVo);
            columnsVoList.add(cVo);

        });
        metaDataDetailVo.setMetaDataColumnsVoList(columnsVoList);
        return ResponseVo.createSuccess(metaDataDetailVo);
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

}
