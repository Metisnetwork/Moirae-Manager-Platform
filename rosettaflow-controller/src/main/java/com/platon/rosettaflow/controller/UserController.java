package com.platon.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.dto.UserDto;
import com.platon.rosettaflow.req.user.LoginInReq;
import com.platon.rosettaflow.req.user.LoginOutReq;
import com.platon.rosettaflow.req.user.UpdateNickReq;
import com.platon.rosettaflow.service.IUserService;
import com.platon.rosettaflow.utils.WalletSignUtils;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.user.UserNicknameVo;
import com.platon.rosettaflow.vo.user.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    public ResponseVo<UserVo> login(@RequestBody @Valid LoginInReq loginInReq) {
        boolean flg;
//        try {
//            flg = WalletSignUtils.verifyTypedDataV4(loginInReq.getSignMessage(), loginInReq.getSign(), loginInReq.getAddress());
//        } catch (IOException e) {
//            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_SIGN_ERROR.getMsg());
//        }
//        if (!flg) {
//            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_SIGN_ERROR.getMsg());
//        }
        UserDto userDto = userService.generatorToken(loginInReq.getAddress(), loginInReq.getUserType());
        return ResponseVo.createSuccess(BeanUtil.copyProperties(userDto, UserVo.class));
    }

    @PostMapping("logout")
    @ApiOperation(value = "用户登出", notes = "用户登出")
    public ResponseVo<UserVo> logout(@RequestBody @Valid LoginOutReq loginOutReq) {
        userService.logout(loginOutReq.getAddress());
        return ResponseVo.create(RespCodeEnum.SUCCESS);
    }

    @PostMapping("updateNickName")
    @ApiOperation(value = "修改昵称", notes = "修改昵称")
    public ResponseVo<UserVo> updateNickName(@RequestBody @Valid UpdateNickReq updateNickReq) {
        userService.updateNickName(updateNickReq.getAddress(), updateNickReq.getNickName());
        return ResponseVo.create(RespCodeEnum.SUCCESS);
    }

    @GetMapping("queryAllUserNickname")
    @ApiOperation(value = "查询所有用户昵称", notes = "查询所有用户昵称")
    public ResponseVo<List<UserNicknameVo>> queryAllUserNickname() {
        List<Map<String, Object>> list = userService.queryAllUserNickname();
        return ResponseVo.createSuccess(BeanUtil.copyToList(list, UserNicknameVo.class));
    }

}
