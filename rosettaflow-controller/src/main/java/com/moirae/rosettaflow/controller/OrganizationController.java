package com.moirae.rosettaflow.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.req.KeyWorkPageReq;
import com.moirae.rosettaflow.req.organization.DelIpPortBindReq;
import com.moirae.rosettaflow.req.organization.IpPortBindReq;
import com.moirae.rosettaflow.service.OrganizationService;
import com.moirae.rosettaflow.utils.ConvertUtils;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.organization.OrganizationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/12/15
 */
@Slf4j
@RestController
@Api(tags = "组织管理关接口")
@RequestMapping(value = "org", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationController {

    @Resource
    private OrganizationService organizationService;

    @GetMapping("listOrgInfoByActivity")
    @ApiOperation(value = "查询组织列表（按活跃度）", notes = "查询组织列表（按活跃度）")
    public ResponseVo<PageVo<OrganizationVo>> listOrgInfoByActivity(@Valid KeyWorkPageReq keyWorkPageReq) {
        IPage<OrganizationDto> page = organizationService.listOrgInfoByNameOrderByActivityDesc(keyWorkPageReq.getCurrent(), keyWorkPageReq.getSize(), keyWorkPageReq.getKeyword());
        List<OrganizationVo> organizationVoList = BeanUtil.copyToList(page.getRecords(), OrganizationVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, organizationVoList));
    }

    @GetMapping("listOrgInfoByTotalData")
    @ApiOperation(value = "查询组织列表（按数据总数）", notes = "查询组织列表（按数据总数）")
    public ResponseVo<PageVo<OrganizationVo>> listOrgInfoByTotalData(@Valid KeyWorkPageReq keyWorkPageReq) {
        IPage<OrganizationDto> page = organizationService.listOrgInfoByNameOrderByTotalDataDesc(keyWorkPageReq.getCurrent(), keyWorkPageReq.getSize(), keyWorkPageReq.getKeyword());
        List<OrganizationVo> organizationVoList = BeanUtil.copyToList(page.getRecords(), OrganizationVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, organizationVoList));
    }

    @GetMapping("listOrgInfoByName")
    @ApiOperation(value = "查询组织列表（按名称）", notes = "查询组织列表（按名称）")
    public ResponseVo<PageVo<OrganizationVo>> listOrgInfoByName(@Valid KeyWorkPageReq keyWorkPageReq) {
        IPage<OrganizationDto> page = organizationService.listOrgInfoByNameOrderByNameAsc(keyWorkPageReq.getCurrent(), keyWorkPageReq.getSize(), keyWorkPageReq.getKeyword());
        List<OrganizationVo> organizationVoList = BeanUtil.copyToList(page.getRecords(), OrganizationVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, organizationVoList));
    }

    @GetMapping("list")
    @ApiOperation(value = "查询用户绑定的组织列表", notes = "查询用户绑定的组织列表")
    public ResponseVo<List<OrganizationVo>> list() {
        List<OrganizationDto> organizationList = organizationService.getOrganizationListByUser();
        List<OrganizationVo> organizationVoList = BeanUtil.copyToList(organizationList, OrganizationVo.class);
        return ResponseVo.createSuccess(organizationVoList);
    }

    @PostMapping("addIpPortBind")
    @ApiOperation(value = "增加用户绑定的组织", notes = "增加用户绑定的组织")
    public ResponseVo<?> addIpPortBind(@RequestBody @Valid IpPortBindReq ipPortBindReq) {
        organizationService.addOrganizationByUser(ipPortBindReq.getIdentityIp(), ipPortBindReq.getIdentityPort());
        return ResponseVo.createSuccess();
    }

    @PostMapping("delIpPortBind")
    @ApiOperation(value = "删除用户绑定的组织", notes = "删除用户绑定的组织")
    public ResponseVo<?> delIpPortBind(@RequestBody @Valid DelIpPortBindReq delIpPortBindReq) {
        organizationService.deleteOrganizationByUser(delIpPortBindReq.getIdentityId());
        return ResponseVo.createSuccess();
    }
}
