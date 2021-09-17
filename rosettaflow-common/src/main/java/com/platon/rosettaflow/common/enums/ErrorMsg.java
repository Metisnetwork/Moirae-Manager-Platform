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
     * 通用
     */
    PARAM_ERROR("请求参数错误", "Request param error"),

    /**
     * 用户
     */
    USER_NONCE_INVALID("nonce无效", "Nonce invalid"),
    USER_SIGN_ERROR("签名错误", "Sign error"),
    USER_UN_LOGIN("用户未登录", "User un login"),
    USER_ADDRESS_ERROR("用户地址有误", "User address error"),
    USER_NAME_EXISTED("用户名称已存在", "User name already exist"),

    /**
     * 项目
     */
    ADD_PROJ_ERROR("新增项目错误", "Add project error"),
    PROJECT_NAME_EXISTED("项目名称已存在", "Project name already exist"),
    PROJECT_NOT_EXIST("项目不存在", "Project not exist"),
    MEMBER_ROLE_EXISTED("成员角色已存在", "Member role already exist"),
    UPDATE_PROJ_ERROR("修改项目错误", "Update project error"),
    QUERY_PROJ_LIST_ERROR("查询项目列表错误", "Query project list error"),
    QUERY_PROJ_DETAILS_ERROR("查询项目详情错误", "Query project details error"),
    ADD_PROJECT_TEMPLATE_ERROR("新增项目模板错误", "Add project template error"),
    PROJECT_TEMPLATE_NAME_EXISTED("项目模板名称已存在", "Project template name already exist"),

    /**
     * 算法
     */
    ADD_ALG_ERROR("新增算法错误", "Add algorithm error"),
    UPDATE_ALG_ERROR("修改算法错误", "Update algorithm error"),
    QUERY_ALG_DETAILS_ERROR("查询算法详情错误", "Query algorithm details error"),
    ALG_NOT_EXIST("算法不存在", "Algorithm not exist"),
    ALG_CODE_NOT_EXIST("算法代码不存在", "Algorithm code not exist"),

    /**
     * 数据
     */
    APPLY_METADATA_USAGE_TYPE_ERROR("元数据使用方式输入格式错误", "Apply metadata usage type error"),
    METADATA_NOT_EXIST("元数据不存在", "Meta data not exist"),
    METADATA_AUTH_TIMES_ERROR("元数据授权申请按次时，使用次数必须大于零", "Metadata authorization by times, the times must be greater than zero"),
    METADATA_AUTH_TIME_ERROR("元数据授权申请时间错误", "Metadata authorization apply time error"),

    /**
     * 工作流
     */
    WORKFLOW_NOT_EXIST("工作流不存在", "Workflow not exist"),
    WORKFLOW_ORIGIN_NOT_EXIST("原工作流不存在", "Origin workflow not exist"),
    WORKFLOW_EXIST("工作流已存在", "Workflow already exist"),
    WORKFLOW_RUNNING_EXIST("工作流正在运行中", "Workflow is running"),
    WORKFLOW_NOT_RUNNING("工作流已结束不能终止", "Workflow has ended and cannot be terminated"),
    WORKFLOW_TERMINATE_NET_PROCESS_ERROR("工作流终止失败", "Workflow terminated fail"),
    WORKFLOW_END_NODE_OVERFLOW("截止节点不能大于工作流最大节点数", "EndNode can not more than workflow nodeNumber"),
    WORKFLOW_NODE_NOT_EXIST("工作流节点不存在", "Workflow node not exist"),
    WORKFLOW_NODE_NOT_CACHE("工作流节点未缓存", "Workflow node not cache"),
    WORKFLOW_NODE_COUNT_CHECK("只支持运行一种算法", "Only one algorithm is supported to run"),
    WORKFLOW_NODE_CODE_NOT_EXIST("工作流节点代码不存在", "Workflow node code not exist"),

    /**
     * 作业
     */
    JOB_ADD_ERROR("新增作业失败", "Add job error"),
    JOB_EDIT_ERROR("作业更新失败", "Job modify error"),
    JOB_NOT_EXIST("原作业不存在", "Job not exist"),
    JOB_TIME_ERROR("作业时间错误", "Job time error"),
    JOB_RUNNING("作业正在执行中不能修改", "Job is running can not modify"),
    JOB_TIME_REPEAT_INTERVAL_ERROR("作业执行重复时，作业时间及执行间隔时间错误", "Job is repeated,the job time or repeat interval error");

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
