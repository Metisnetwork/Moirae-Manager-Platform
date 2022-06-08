package com.datum.platform.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.datum.platform.mapper.domain.Org;
import com.datum.platform.req.org.GetOrgListReq;
import com.datum.platform.req.org.JoinOrgReq;
import com.datum.platform.req.org.OrgIdReq;
import com.datum.platform.service.OrgService;
import com.datum.platform.utils.ConvertUtils;
import com.datum.platform.vo.PageVo;
import com.datum.platform.vo.ResponseVo;
import com.datum.platform.vo.org.BaseOrgVo;
import com.datum.platform.vo.org.OrgStatsVo;
import com.datum.platform.vo.org.OrgVo;
import com.datum.platform.vo.org.UserOrgVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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

    @GetMapping("getOrgList")
    @ApiOperation(value = "查询组织列表", notes = "查询组织列表")
    public ResponseVo<PageVo<OrgVo>> getOrgList(@Valid GetOrgListReq req) {
        IPage<Org> page = orgService.getOrgList(req.getCurrent(), req.getSize(), req.getKeyword(), req.getOrderBy());
        List<OrgVo> itemList = BeanUtil.copyToList(page.getRecords(), OrgVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getOrgDetails")
    @ApiOperation(value = "查询组织详情", notes = "查询组织详情")
    public ResponseVo<OrgVo> getOrgDetails(@Valid OrgIdReq req) {
        Org org = orgService.getOrgDetails(req.getIdentityId());
        return ResponseVo.createSuccess(BeanUtil.copyProperties(org, OrgVo.class));
    }

    @GetMapping("getUserOrgList")
    @ApiOperation(value = "查询用户可用的组织列表", notes = "查询用户可用的组织列表")
    public ResponseVo<List<UserOrgVo>> getUserOrgList() {
        List<Org> orgList = orgService.getUserOrgList();
        return ResponseVo.createSuccess(BeanUtil.copyToList(orgList, UserOrgVo.class));
    }

    @GetMapping("getBaseOrgList")
    @ApiOperation(value = "查询基本组织列表-用户有可用数据的", notes = "查询基本组织列表-用户有可用数据的")
    public ResponseVo<List<BaseOrgVo>> getBaseOrgList() {
        List<Org> orgList = orgService.getBaseOrgList();
        return ResponseVo.createSuccess(BeanUtil.copyToList(orgList, BaseOrgVo.class));
    }

    @PostMapping("joinOrg")
    @ApiOperation(value = "用户加入组织", notes = "用户加入组织")
    public ResponseVo<?> joinOrg(@RequestBody @Valid JoinOrgReq req) {
        orgService.addOrganizationByUser(req.getIdentityIp(), req.getIdentityPort());
        return ResponseVo.createSuccess();
    }

    @PostMapping("quitOrg")
    @ApiOperation(value = "用户退出组织", notes = "用户退出组织")
    public ResponseVo<?> quitOrg(@RequestBody @Valid OrgIdReq req) {
        orgService.deleteOrganizationByUser(req.getIdentityId());
        return ResponseVo.createSuccess();
    }
}
