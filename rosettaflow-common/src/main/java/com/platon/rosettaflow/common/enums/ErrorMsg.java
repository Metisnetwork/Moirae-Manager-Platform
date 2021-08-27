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

    USER_UN_LOGIN("用户未登录", "User un login"),

    /** 新增算法错误 */
    ADD_ALG_ERROR("新增算法错误", "add algorithm error"),

    /** 修改算法错误 */
    UPDATE_ALG_ERROR("修改算法错误", "update algorithm error"),

    /** 查询算法列表错误 */
    QUERY_ALG_LIST_ERROR("查询算法列表错误", "query algorithm list error"),

    /** 查询算法详情错误 */
    QUERY_ALG_DETAILS_ERROR("查询算法详情错误", "query algorithm details error"),

    /** 新增项目错误 */
    ADD_PROJ_ERROR("新增项目错误", "add project error"),

    /** 修改项目错误 */
    UPDATE_PROJ_ERROR("修改项目错误", "update project error"),

    /** 查询项目列表错误 */
    QUERY_PROJ_LIST_ERROR("查询项目列表错误", "query project list error"),

    /** 查询项目详情错误 */
    QUERY_PROJ_DETAILS_ERROR("查询项目详情错误", "query project details error"),

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
