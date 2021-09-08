package com.platon.rosettaflow.common.enums;

import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.common.utils.LanguageContext;

/**
 * @author hudenian
 * @date
 * @description
 */
public enum ErrorMsg {

    /**
     * 请求参数错误
     */
    PARAM_ERROR("请求参数错误", "Request param error"),

    /**
     * 用户签名错误
     */
    USER_NONCE_INVALID("nonce无效", "Nonce invalid"),

    USER_SIGN_ERROR("签名错误", "Sign error"),

    USER_UN_LOGIN("用户未登录", "User un login"),

    /**
     * 用户缓存信息丢失
     */
    USER_CACHE_LOST_ERROR("查询算法列表错误", "user cache lost error"),

    /**
     * 新增算法错误
     */
    ADD_ALG_ERROR("新增算法错误", "add algorithm error"),

    /**
     * 修改算法错误
     */
    UPDATE_ALG_ERROR("修改算法错误", "update algorithm error"),

    /**
     * 查询算法列表错误
     */
    QUERY_ALG_LIST_ERROR("查询算法列表错误", "query algorithm list error"),

    /**
     * 查询算法详情错误
     */
    QUERY_ALG_DETAILS_ERROR("查询算法详情错误", "query algorithm details error"),

    /**
     * 新增项目错误
     */
    ADD_PROJ_ERROR("新增项目错误", "add project error"),

    /**
     * 项目名称已存在
     */
    PROJECT_NAME_EXISTED("项目名称已存在", "project name already exist"),

    /**
     * 项目成员角色已存在
     */
    MEMBER_ROLE_EXISTED("成员角色已存在", "member role already exist"),

    /**
     * 修改项目错误
     */
    UPDATE_PROJ_ERROR("修改项目错误", "update project error"),

    /**
     * 查询项目列表错误
     */
    QUERY_PROJ_LIST_ERROR("查询项目列表错误", "query project list error"),

    /**
     * 查询项目详情错误
     */
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
    WORKFLOW_ORIGIN_NOT_EXIST("原工作流不存在", "Origin workflow not exist"),
    WORKFLOW_EXIST("工作流已存在", "workflow already exist"),
    /**
     * 工作流节点不存在
     */
    WORKFLOW_NODE_NOT_EXIST("工作流节点不存在", "workflow node not exist"),
    WORKFLOW_NODE_EXIST("工作流节点已存在", "workflow node exist"),
    /**
     * 工作流节点代码不存在
     */
    WORKFLOW_NODE_CODE_NOT_EXIST("工作流节点代码不存在", "workflow node code not exist"),

    JOB_ADD_ERROR("新增作业失败", "Add job error"),

    JOB_EDIT_ERROR("作业更新失败", "Job modify error"),

    JOB_NOT_EXIST("原作业不存在", "Job not exist"),

    JOB_TIME_ERROR("作业时间错误", "Job time error"),

    JOB_TIME_REPEATINTERVAL_ERROR("作业执行重复时，作业时间及执行间隔时间错误", "job is repeated,the job time or repeat interval error"),

    METADATA_NOT_EXIST("元数据不存在", "Meta data not exist"),

    METADATA_AUTH_TIMES_ERROR("元数据授权申请按次时，使用次数必须大于零", "Metadata authorization by times, the times must be greater than zero"),

    METADATA_AUTH_TIME_ERROR("元数据授权申请时间错误", "Metadata authorization apply time error"),

    ALGORITHM_AUTH_NOT_EXIST("用户算法授权信息不存在或者授权已失效", "Algorithm auth not exist or invalidation");

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
