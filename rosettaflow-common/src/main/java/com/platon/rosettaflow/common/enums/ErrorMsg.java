package com.platon.rosettaflow.common.enums;

import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.common.utils.LanguageContext;

/**
 * @author  hudenian
 * @date
 * @description
 */
public enum ErrorMsg {

    /**
     * 用户签名错误
     */
    USER_SIGN_ERROR("签名错误", "Sign error"),

    APPLY_METADATA_USAGE_TYPE_ERROR("元数据使用方式输入格式错误", "Apply metadata usage type error"),
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
    WORKFLOW_NODE_NOT_EXIST("工作流节点不存在", "workflow node not exist"),
    /**
     * 工作流节点代码不存在
     */
    WORKFLOW_NODE_CODE_NOT_EXIST("工作流节点代码不存在", "workflow node code not exist");

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
