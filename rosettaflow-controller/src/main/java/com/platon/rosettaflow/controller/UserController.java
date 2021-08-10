package com.platon.rosettaflow.controller;

import com.platon.rosettaflow.dto.MemberRoleDto;
import com.platon.rosettaflow.mapper.domain.MemberRole;
import com.platon.rosettaflow.req.user.RegisterReq;
import com.platon.rosettaflow.service.IMemberRole;
import com.platon.rosettaflow.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author admin
 * @date 2021/7/20
 */
@Slf4j
@RestController
@Api(tags = "用户管理关接口")
@RequestMapping(value = "user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Resource
    private IMemberRole memberRole;

    @PostMapping("register")
    @ApiOperation(value = "用户注册", notes = "用户注册")
    public ResponseVo<List<MemberRoleDto>> register(@RequestBody @Valid RegisterReq registerReq) {
        log.info("用户注册");
        List<MemberRoleDto> memberRoleDtoList = memberRole.getAll();
        return ResponseVo.createSuccess(memberRoleDtoList);
    }
}
