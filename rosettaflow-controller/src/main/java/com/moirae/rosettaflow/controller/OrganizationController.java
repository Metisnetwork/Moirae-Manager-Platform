package com.moirae.rosettaflow.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.UserOrgMaintainDto;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.req.organization.DelIpPortBindReq;
import com.moirae.rosettaflow.req.organization.IpPortBindReq;
import com.moirae.rosettaflow.req.organization.ListUserOrgBindReq;
import com.moirae.rosettaflow.req.workflow.ListWorkflowReq;
import com.moirae.rosettaflow.service.IUserOrgMaintainService;
import com.moirae.rosettaflow.utils.ConvertUtils;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.organization.UserOrgMaintainVo;
import com.moirae.rosettaflow.vo.workflow.WorkflowVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
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
    private IUserOrgMaintainService userOrgMaintainService;

    @GetMapping("list")
    @ApiOperation(value = "查询当前用户绑定的组织列表", notes = "查询当前用户绑定的组织列表")
    public ResponseVo<PageVo<UserOrgMaintainVo>> list(@Validated ListUserOrgBindReq listReq) {
        IPage<UserOrgMaintainDto> page = userOrgMaintainService.queryUserOrgMaintainPageList(listReq.getOrgName(),
                listReq.getCurrent(), listReq.getSize());
        List<UserOrgMaintainVo> items = BeanUtil.copyToList(page.getRecords(), UserOrgMaintainVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, items));
    }

    @PostMapping("addIpPortBind")
    @ApiOperation(value = "配置组织与ip及port绑定关系", notes = "配置组织与ip及port绑定关系")
    public ResponseVo<?> addIpPortBind(@RequestBody @Valid IpPortBindReq ipPortBindReq) {
        userOrgMaintainService.ipPortBind(ipPortBindReq.getIdentityIp(), ipPortBindReq.getIdentityPort());
        return ResponseVo.createSuccess();
    }

    @PostMapping("delIpPortBind")
    @ApiOperation(value = "删除用户ip及port绑定关系", notes = "删除用户ip及port绑定关系")
    public ResponseVo<?> delIpPortBind(@RequestBody @Valid DelIpPortBindReq delIpPortBindReq) {
        userOrgMaintainService.delIpPortBind(delIpPortBindReq.getId());
        return ResponseVo.createSuccess();
    }

}
