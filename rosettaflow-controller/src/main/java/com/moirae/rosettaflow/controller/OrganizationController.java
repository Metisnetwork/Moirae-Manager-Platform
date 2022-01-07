package com.moirae.rosettaflow.controller;


import cn.hutool.core.bean.BeanUtil;
import com.moirae.rosettaflow.mapper.domain.Organization;
import com.moirae.rosettaflow.req.organization.DelIpPortBindReq;
import com.moirae.rosettaflow.req.organization.IpPortBindReq;
import com.moirae.rosettaflow.service.IOrganizationService;
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
    private IOrganizationService organizationService;

    @GetMapping("list")
    @ApiOperation(value = "查询用户绑定的组织列表", notes = "查询用户绑定的组织列表")
    public ResponseVo<List<OrganizationVo>> list() {
        List<Organization> organizationList = organizationService.getAllByUserSession();
        List<OrganizationVo> organizationVoList = BeanUtil.copyToList(organizationList, OrganizationVo.class);
        return ResponseVo.createSuccess(organizationVoList);
    }

    @PostMapping("addIpPortBind")
    @ApiOperation(value = "增加用户绑定的组织", notes = "增加用户绑定的组织")
    public ResponseVo<?> addIpPortBind(@RequestBody @Valid IpPortBindReq ipPortBindReq) {
        organizationService.addUserOrganization(ipPortBindReq.getIdentityIp(), ipPortBindReq.getIdentityPort());
        return ResponseVo.createSuccess();
    }

    @PostMapping("delIpPortBind")
    @ApiOperation(value = "删除用户绑定的组织", notes = "删除用户绑定的组织")
    public ResponseVo<?> delIpPortBind(@RequestBody @Valid DelIpPortBindReq delIpPortBindReq) {
        organizationService.deleteUserOrganization(delIpPortBindReq.getIdentityId());
        return ResponseVo.createSuccess();
    }
}
