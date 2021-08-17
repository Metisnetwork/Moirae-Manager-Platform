package com.platon.rosettaflow.common.enums;

import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.common.utils.LanguageContext;

/**
 * @author admin
 * @date 2021/8/10
 * @description 错误码描述
 */
public enum ErrorMsg {

    /**
     * 用户签名错误
     */
    USER_SIGN_ERROR("签名错误", "Sign error"),
    /**
     * 截止节点不能大于工作流最大节点数
     */
    WORKFLOW_END_NODE_OVERFLOW("截止节点不能大于工作流最大节点数", "EndNode can not more than workflow nodeNumber"),
    /**
     * 工作流不存在
     */
    WORKFLOW_NOT_EXIST("工作流不存在", "workflow not exist"),
    /**
     * 工作流节点不存在
     */
    WORKFLOW_NODE_NOT_EXIST("工作流节点不存在", "workflow node not exist");

    private final String zh;
    private final String en;

    ErrorMsg(String zh, String en) {
        this.zh = zh;
        this.en = en;
    }

    public String getMsg() {
        String lang = LanguageContext.get();
        if (lang == null || lang.equals(SysConstant.ZH_CN)) {
            return this.zh;
        }
        return this.en;
    }
}
