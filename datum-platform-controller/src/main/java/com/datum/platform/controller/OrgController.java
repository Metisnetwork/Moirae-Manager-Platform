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
import com.datum.platform.vo.org.DataSelectOrgVo;
import com.datum.platform.vo.org.OrgStatsVo;
import com.datum.platform.vo.org.OrgVo;
import com.datum.platform.vo.org.UserOrgVo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
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
@ApiSupport(order = 300)
@RequestMapping(value = "org", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrgController {

    @Resource
    private OrgService orgService;

    @GetMapping("getOrgStats")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "查询组织统计", notes = "查询组织统计")
    public ResponseVo<OrgStatsVo> getOrgStats() {
        int orgCount = orgService.getOrgStats();
        OrgStatsVo orgStatsVo = new OrgStatsVo();
        orgStatsVo.setOrgCount(orgCount);
        return ResponseVo.createSuccess(orgStatsVo);
    }

    @GetMapping("getOrgList")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "查询组织列表", notes = "查询组织列表")
    public ResponseVo<PageVo<OrgVo>> getOrgList(@Valid GetOrgListReq req) {
        IPage<Org> page = orgService.getOrgList(req.getCurrent(), req.getSize(), req.getKeyword(), req.getOrderBy());
        List<OrgVo> itemList = BeanUtil.copyToList(page.getRecords(), OrgVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getOrgDetails")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "查询组织详情", notes = "查询组织详情")
    public ResponseVo<OrgVo> getOrgDetails(@Valid OrgIdReq req) {
        Org org = orgService.getOrgDetails(req.getIdentityId());
        return ResponseVo.createSuccess(BeanUtil.copyProperties(org, OrgVo.class));
    }

    @GetMapping("getUserOrgList")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "查询用户可用的组织列表", notes = "查询用户可用的组织列表")
    public ResponseVo<List<UserOrgVo>> getUserOrgList() {
        List<Org> orgList = orgService.getUserOrgList();
        return ResponseVo.createSuccess(BeanUtil.copyToList(orgList, UserOrgVo.class));
    }

    @GetMapping("getBaseOrgList")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "工作流数据输入时, 查询用户存在数据的组织列表", notes = "工作流数据输入时, 查询用户存在数据的组织列表")
    public ResponseVo<List<DataSelectOrgVo>> getBaseOrgList() {
        List<Org> orgList = orgService.getBaseOrgList();
        return ResponseVo.createSuccess(BeanUtil.copyToList(orgList, DataSelectOrgVo.class));
    }

    @GetMapping("getPowerOrgList")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "工作流数据输入时, 查询存在算力的组织列表", notes = "工作流数据输入时, 查询存在算力的组织列表")
    public ResponseVo<List<DataSelectOrgVo>> getPowerOrgList() {
        List<Org> orgList = orgService.getPowerOrgList();
        return ResponseVo.createSuccess(BeanUtil.copyToList(orgList, DataSelectOrgVo.class));
    }

    @PostMapping("joinOrg")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "用户加入组织", notes = "用户加入组织")
    public ResponseVo<?> joinOrg(@RequestBody @Valid JoinOrgReq req) {
        orgService.addOrganizationByUser(req.getIdentityIp(), req.getIdentityPort());
        return ResponseVo.createSuccess();
    }

    @PostMapping("quitOrg")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "用户退出组织", notes = "用户退出组织")
    public ResponseVo<?> quitOrg(@RequestBody @Valid OrgIdReq req) {
        orgService.deleteOrganizationByUser(req.getIdentityId());
        return ResponseVo.createSuccess();
    }
}
