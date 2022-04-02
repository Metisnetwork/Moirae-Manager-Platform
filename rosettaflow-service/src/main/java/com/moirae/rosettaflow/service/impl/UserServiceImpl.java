package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moirae.rosettaflow.common.constants.SysConfig;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.common.utils.WalletSignUtils;
import com.moirae.rosettaflow.dto.SignMessageDto;
import com.moirae.rosettaflow.dto.UserDto;
import com.moirae.rosettaflow.manager.UserManager;
import com.moirae.rosettaflow.mapper.domain.User;
import com.moirae.rosettaflow.service.TokenService;
import com.moirae.rosettaflow.service.OrgService;
import com.moirae.rosettaflow.service.UserService;
import com.moirae.rosettaflow.service.utils.CommonUtils;
import com.moirae.rosettaflow.service.utils.UserContext;
import com.zengtengpeng.operation.RedissonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TokenService tokenService;
    @Resource
    private OrgService organizationService;
    @Resource
    private UserManager userManager;

    @Resource
    private RedissonObject redissonObject;
    @Resource
    private SysConfig sysConfig;

    @Override
    public User getByAddress(String address) {
        return userManager.getValidById(address);
    }

    @Override
    public UserDto loginBySign(String hexAddress, String hrpAddress,String authenticateSignMessage, String authenticateSign) {
        // 检查nonce
        checkNonceValidity(authenticateSignMessage, hexAddress);

        // 登录校验
        verifySign(hrpAddress, authenticateSignMessage, authenticateSign);

        // 查询用户信息
        boolean isSave = false;
        boolean isUpdate = false;

        User user = this.getByAddress(hexAddress);
        if (user == null) {
            user = new User();
            user.setUserName(hrpAddress);
            user.setAddress(hexAddress);
            isSave = true;
        }

        // 设置用户组织
        if(StringUtils.isBlank(user.getOrgIdentityId())){
            List<String> organizationList = organizationService.getIdentityIdListByUser(hexAddress);
            if (CollectionUtil.isNotEmpty(organizationList)) {
                user.setOrgIdentityId(organizationList.get(new Random().nextInt(organizationList.size())));
                isUpdate = true;
            }
        }

        // 保存用户信息
        if(isSave){
            userManager.save(user);
        } else if(isUpdate){
            userManager.updateById(user);
        }

        // 设置用户会话
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setToken(tokenService.setToken(userDto));

        return  userDto;
    }

    @Override
    public void logout() {
        try {
            UserDto userDto = UserContext.getCurrentUser();
            tokenService.removeToken(userDto.getToken());
        } catch (BusinessException e) {
            log.info("User not login not need to logout");
        }
    }

    @Override
    public void updateNickName(String address, String nickName) {
        User user = userManager.getValidByUserName(nickName);
        if (!Objects.isNull(user)) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_NAME_EXISTED.getMsg());
        }

        try {
            user = userManager.getValidById(address);
            user.setUserName(nickName);
            userManager.updateById(user);
        } catch (Exception e) {
            log.error("updateNickName--修改用户昵称失败, 错误信息:{}", e.getMessage());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.MODIFY_USER_NAME_FAILED.getMsg());
        }
    }

    @Override
    public List<User> queryAllUserNickName() {
        return userManager.getValidList();
    }

    @Override
    public String getLoginNonce(String address) {
        String nonce = CommonUtils.generateUuid();
        redissonObject.setValue(StrUtil.format(SysConstant.REDIS_USER_NONCE_KEY, address, nonce), nonce, sysConfig.getNonceTimeOut());
        return nonce;
    }

    private void checkNonceValidity(String signMessage, String address) {

        SignMessageDto signMessageDto;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            signMessageDto = objectMapper.readValue(signMessage, SignMessageDto.class);
        } catch (Exception e) {
            log.error(ErrorMsg.PARAM_ERROR.getMsg(), e);
            throw new BusinessException(RespCodeEnum.PARAM_ERROR, ErrorMsg.PARAM_ERROR.getMsg());
        }
        if (Objects.isNull(signMessageDto.getMessage()) || StrUtil.isEmpty(signMessageDto.getMessage().getKey())) {
            log.error(ErrorMsg.PARAM_ERROR.getMsg());
            throw new BusinessException(RespCodeEnum.PARAM_ERROR, ErrorMsg.PARAM_ERROR.getMsg());
        }
        String nonce = signMessageDto.getMessage().getKey();
        if (StrUtil.isEmpty(nonce)) {
            log.error("Nonce is empty!");
            throw new BusinessException(RespCodeEnum.PARAM_ERROR, ErrorMsg.PARAM_ERROR.getMsg());
        }

        String redisKey = StrUtil.format(SysConstant.REDIS_USER_NONCE_KEY, address, nonce);

        if (!redissonObject.delete(redisKey)) {
            log.error("Delete old user nonce fail!");
            throw new BusinessException(RespCodeEnum.NONCE_INVALID, ErrorMsg.USER_NONCE_INVALID.getMsg());
        }
    }

    private void verifySign(String hrpAddress, String authenticateSignMessage, String authenticateSign) {
        boolean flg;
        try {
            String signMessage = StrUtil.replace(authenticateSignMessage, "\\\"", "\"");
            flg = WalletSignUtils.verifyTypedDataV4(signMessage, authenticateSign, hrpAddress);
        } catch (Exception e) {
            log.error("User login signature error,error msg:{}", e.getMessage(), e);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_SIGN_ERROR.getMsg());
        }
        if (!flg) {
            log.error("User login signature error");
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_SIGN_ERROR.getMsg());
        }
    }

}
