package com.platon.rosettaflow.controller;

import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.dto.UserDto;
import com.platon.rosettaflow.req.user.LoginReq;
import com.platon.rosettaflow.service.IUserService;
import com.platon.rosettaflow.utils.ConvertUtils;
import com.platon.rosettaflow.utils.WalletSignUtils;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.user.UserVo;
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

/**
 * @author admin
 * @date 2021/8/17
 */
@Slf4j
@RestController
@Api(tags = "用户管理关接口")
@RequestMapping(value = "user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("login")
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public ResponseVo<UserVo> register(@RequestBody @Valid LoginReq registerReq) {
        if (!WalletSignUtils.verifySign(registerReq.getAddress(), registerReq.getSign(), registerReq.getAddress())) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_SIGN_ERROR.getMsg());
        }
        UserDto userDto = userService.generatorToken(registerReq.getAddress());
        return ResponseVo.createSuccess(ConvertUtils.convert2Vo(userDto));
    }
}
