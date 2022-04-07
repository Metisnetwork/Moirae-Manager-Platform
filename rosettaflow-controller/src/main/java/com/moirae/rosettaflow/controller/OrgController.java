package com.moirae.rosettaflow.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.mapper.domain.Org;
import com.moirae.rosettaflow.req.org.GetOrgListReq;
import com.moirae.rosettaflow.req.org.JoinOrgReq;
import com.moirae.rosettaflow.req.org.OrgIdReq;
import com.moirae.rosettaflow.service.OrgService;
import com.moirae.rosettaflow.utils.ConvertUtils;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.org.OrgStatsVo;
import com.moirae.rosettaflow.vo.org.OrgVo;
import com.moirae.rosettaflow.vo.org.UserOrgVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 组织相关
 * @date 2021/12/15
 */
@Slf4j
@RestController
@Api(tags = "组织管理关接口")
@RequestMapping(value = "org", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrgController {

    @Resource
    private OrgService orgService;

    @GetMapping("getOrgStats")
    @ApiOperation(value = "查询组织统计", notes = "查询组织统计")
    public ResponseVo<OrgStatsVo> getOrgStats() {
        int orgCount = orgService.getOrgStats();
        OrgStatsVo orgStatsVo = new OrgStatsVo();
        orgStatsVo.setOrgCount(orgCount);
        return ResponseVo.createSuccess(orgStatsVo);
    }

    @GetMapping("（开发中）getOrgList")
    @ApiOperation(value = "查询组织列表", notes = "查询组织列表")
    public ResponseVo<PageVo<OrgVo>> getOrgList(@Valid GetOrgListReq req) {
        IPage<Org> page = orgService.getOrgList(req.getCurrent(), req.getSize(), req.getKeyword(), req.getOrderBy());
        List<OrgVo> itemList = BeanUtil.copyToList(page.getRecords(), OrgVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("（开发中）getOrgDetails")
    @ApiOperation(value = "查询组织详情", notes = "查询组织详情")
    public ResponseVo<OrgVo> getOrgDetails(@RequestParam OrgIdReq req) {
        Org org = orgService.getOrgDetails(req.getIdentityId());
        return ResponseVo.createSuccess(BeanUtil.copyProperties(org, OrgVo.class));
    }

    @GetMapping("（开发中）getUserOrgList")
    @ApiOperation(value = "查询用户可用的组织列表", notes = "查询用户可用的组织列表")
    public ResponseVo<PageVo<UserOrgVo>> getUserOrgList() {
        List<UserOrgVo> orgTaskVoList = new ArrayList<>();
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(null, orgTaskVoList));
    }

    @PostMapping("（开发中）joinOrg")
    @ApiOperation(value = "用户加入组织", notes = "用户加入组织")
    public ResponseVo<?> joinOrg(@RequestBody @Valid JoinOrgReq req) {
        orgService.addOrganizationByUser(req.getIdentityIp(), req.getIdentityPort());
        return ResponseVo.createSuccess();
    }

    @PostMapping("（开发中）quitOrg")
    @ApiOperation(value = "用户退出组织", notes = "用户退出组织")
    public ResponseVo<?> quitOrg(@RequestBody @Valid OrgIdReq req) {
        orgService.deleteOrganizationByUser(req.getIdentityId());
        return ResponseVo.createSuccess();
    }
}
