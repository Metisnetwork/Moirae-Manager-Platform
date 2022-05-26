package com.datum.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import com.datum.platform.common.constants.SysConstant;
import com.datum.platform.common.enums.ErrorMsg;
import com.datum.platform.common.enums.RespCodeEnum;
import com.datum.platform.dto.UserDto;
import com.datum.platform.req.user.LoginInReq;
import com.datum.platform.req.user.UpdateUserNameReq;
import com.datum.platform.service.UserService;
import com.datum.platform.service.dto.user.NonceDto;
import com.datum.platform.service.dto.user.UserAddressDto;
import com.datum.platform.vo.ResponseVo;
import com.datum.platform.vo.user.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
    private UserService userService;

    @GetMapping("getLoginNonce")
    @ApiOperation(value = "获取登录Nonce", notes = "获取登录Nonce")
    public ResponseVo<NonceDto> getLoginNonce(UserAddressDto req) {
        return ResponseVo.createSuccess(userService.getLoginNonce(req));
    }

    @PostMapping("login")
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public ResponseVo<UserVo> login(@RequestBody @Validated LoginInReq loginInReq) {
        UserDto userDto = userService.loginBySign(loginInReq.getAddress(), loginInReq.getHrpAddress(), loginInReq.getSignMessage(), loginInReq.getSign());
        UserVo userVo = BeanUtil.copyProperties(userDto, UserVo.class);
        return ResponseVo.createSuccess(userVo);
    }

    @PostMapping("logout")
    @ApiOperation(value = "用户登出", notes = "用户登出")
    public ResponseVo<?> logout(HttpServletResponse httpServletResponse) {
        userService.logout();
        httpServletResponse.setHeader(SysConstant.HEADER_TOKEN_KEY, "");
        return ResponseVo.createSuccess();
    }

    @PostMapping("updateUserName")
    @ApiOperation(value = "修改昵称", notes = "修改昵称")
    public ResponseVo<?> updateUserName(@RequestBody @Valid UpdateUserNameReq req) {
        userService.updateUserName(req.getUserName());
        return ResponseVo.create(RespCodeEnum.SUCCESS, ErrorMsg.SUCCESS.getMsg());
    }
}
