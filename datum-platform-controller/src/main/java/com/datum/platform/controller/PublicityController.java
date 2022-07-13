package com.datum.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.datum.platform.mapper.domain.OrgExpand;
import com.datum.platform.mapper.domain.OrgVc;
import com.datum.platform.mapper.domain.Proposal;
import com.datum.platform.req.CommonPageReq;
import com.datum.platform.req.IdReq;
import com.datum.platform.req.publicity.GetLatestOrgVcListReq;
import com.datum.platform.service.OrgService;
import com.datum.platform.service.PublicityService;
import com.datum.platform.utils.ConvertUtils;
import com.datum.platform.vo.PageVo;
import com.datum.platform.vo.ResponseVo;
import com.datum.platform.vo.publicity.*;
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

@Slf4j
@RestController
@Api(tags = "公示相关接口")
@ApiSupport(order = 700)
@RequestMapping(value = "publicity", produces = MediaType.APPLICATION_JSON_VALUE)
public class PublicityController {
    @Resource
    private PublicityService publicityService;
    @Resource
    private OrgService orgService;

    @GetMapping("getPublicityStats")
    @ApiOperation(value = "查询公示统计", notes = "查询公示统计")
    @ApiOperationSupport(order = 1)
    public ResponseVo<PublicityStatsVo> getPublicityStats() {
        int orgVcCount = orgService.countOfOrgVc();
        PublicityStatsVo publicityStatsVo = new PublicityStatsVo();
        publicityStatsVo.setOrgVcCount(orgVcCount);
        return ResponseVo.createSuccess(publicityStatsVo);
    }

    @GetMapping("getLatestOrgVcList")
    @ApiOperation(value = "获得最新的已认证组织列表", notes = "获得最新的已认证组织列表")
    @ApiOperationSupport(order = 2)
    public ResponseVo<List<OrgVcVo>> getLatestOrgVcList(@Valid GetLatestOrgVcListReq req) {
        List<OrgVc> orgList = orgService.getLatestOrgVcList(req.getSize());
        return ResponseVo.createSuccess(BeanUtil.copyToList(orgList, OrgVcVo.class));
    }

    @GetMapping("getProposalList")
    @ApiOperation(value = "查询委员会提案列表", notes = "查询委员会提案列表")
    @ApiOperationSupport(order = 3)
    public ResponseVo<PageVo<ProposalVo>> getProposalList(@Valid CommonPageReq req) {
        IPage<Proposal> page = publicityService.listProposal(req.getCurrent(), req.getSize());
        List<ProposalVo> itemList = BeanUtil.copyToList(page.getRecords(), ProposalVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getOrgVcList")
    @ApiOperation(value = "获得已认证组织列表", notes = "获得已认证组织列表")
    @ApiOperationSupport(order = 4)
    public ResponseVo<PageVo<OrgVcVo>> getOrgVcList(@Valid CommonPageReq req) {
        IPage<OrgVc> page = orgService.listOrgVcList(req.getCurrent(), req.getSize());
        List<OrgVcVo> itemList = BeanUtil.copyToList(page.getRecords(), OrgVcVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getAuthorityList")
    @ApiOperation(value = "查询委员会列表", notes = "查询委员会列表")
    @ApiOperationSupport(order = 5)
    public ResponseVo<PageVo<AuthorityVo>> getAuthorityList(@Valid CommonPageReq req) {
        IPage<OrgExpand> page = orgService.listAuthority(req.getCurrent(), req.getSize());
        List<AuthorityVo> itemList = BeanUtil.copyToList(page.getRecords(), AuthorityVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getProposalDetails")
    @ApiOperation(value = "查询委员会提案详情", notes = "查询委员会提案详情")
    @ApiOperationSupport(order = 6)
    public ResponseVo<ProposalDetailsVo> getProposalDetails(@Valid IdReq req) {
        Proposal proposal = publicityService.getProposalDetails(req.getId());
        return ResponseVo.createSuccess(BeanUtil.copyProperties(proposal, ProposalDetailsVo.class));
    }
}
