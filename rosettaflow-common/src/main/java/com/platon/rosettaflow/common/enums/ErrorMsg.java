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
    USER_SIGN_ERROR("签名错误", "Signature error"),
    USER_UN_LOGIN("用户未登录", "User not logged in"),
    USER_ADDRESS_ERROR("用户地址有误", "Wrong user address"),
    MODIFY_USER_NAME_FAILED("昵称修改失败", "Nickname modification failed"),
    USER_NAME_EXISTED("昵称已存在", "Nickname already exists"),

    /**
     * 项目
     */
    ADD_PROJ_ERROR("新增项目错误", "New project error"),
    PROJECT_NAME_EXISTED("项目名称已存在", "Project name already exists"),
    PROJECT_NOT_EXIST("项目不存在", "Project does not exist"),
    MEMBER_ROLE_EXISTED("成员角色已存在", "Member role already exists"),
    UPDATE_PROJ_ERROR("修改项目错误", "Modify project error"),
    QUERY_PROJ_LIST_ERROR("查询项目列表错误", "Query project list error"),
    QUERY_PROJ_DETAILS_ERROR("查询项目详情错误", "Query project details error"),
    ADD_PROJECT_TEMPLATE_ERROR("新增项目模板错误", "Add project template error"),
    PROJECT_TEMPLATE_NAME_EXISTED("项目模板名称已存在", "Project template name already exists"),
    ALGORITHM_CODE_NOT_NOT_EXISTS("项目模板对应的工作流节点代码不存在", "Project template algorithm code not exists"),
    USER_ADMIN_PERMISSION_ERROR("您不是当前项目管理员，无操作权限！", "You are not this project administrator, No operation authority"),
    USER_NOT_PERMISSION_ERROR("您是项目查看者，暂无编辑权限！", "You are a project viewer and have no editing rights"),
    USER_ADMIN_must__ERROR("不能删除最后一个管理员", "Cannot delete the last administrator"),

    /**
     * 算法
     */
    ADD_ALG_ERROR("新增算法错误", "New algorithm error"),
    UPDATE_ALG_ERROR("修改算法错误", "Modify algorithm error"),
    QUERY_ALG_DETAILS_ERROR("查询算法详情错误", "Query algorithm details error"),
    ALG_NOT_EXIST("算法不存在", "Algorithm does not exist"),
    ALG_CODE_NOT_EXIST("算法代码不存在", "Algorithm code does not exist"),
    ALGORITHM_AUTH_NOT_EXIST("用户算法授权信息不存在或者授权已失效", "Algorithm auth not exist or invalidation"),

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
    WORKFLOW_NOT_EXIST("工作流不存在", "Workflow does not exist"),
    WORKFLOW_ORIGIN_NOT_EXIST("原工作流不存在", "Origin workflow does not exist"),
    WORKFLOW_COPY_ERROR("复制工作流失败", "Failed to copy workflow"),
    WORKFLOW_EXIST("工作流已存在", "Workflow already exists"),
    WORKFLOW_RUNNING_EXIST("工作流正在运行中", "Workflow is running"),
    WORKFLOW_NOT_RUNNING("工作流已结束不能终止", "Workflow has ended and cannot be terminated"),
    WORKFLOW_TERMINATE_NET_PROCESS_ERROR("工作流终止失败", "Workflow terminated failed"),
    WORKFLOW_END_NODE_OVERFLOW("截止节点不能大于工作流最大节点数", "EndNode can not more than workflow nodeNumber"),
    WORKFLOW_NODE_NOT_EXIST("工作流节点不存在", "Workflow node does not exist"),
    WORKFLOW_NODE_NOT_CACHE("工作流节点未缓存", "Workflow node not cached"),
    WORKFLOW_NODE_COUNT_CHECK("只支持运行一种算法", "Only one algorithm is supported"),
    WORKFLOW_NODE_SENDER_NOT_EXIST("工作流节点需要一个发起方", "Workflow node need one sender"),
    WORKFLOW_NODE_NOT_INPUT_EXIST("工作流节点输入未配置", "Workflow node input is not configured"),
    WORKFLOW_NODE_NOT_OUTPUT_EXIST("工作流节点输出未配置", "Workflow node output is not configured"),
    WORKFLOW_NODE_CODE_NOT_EXIST("工作流节点代码未配置", "Workflow node code is not configured"),
    WORKFLOW_NODE_NOT_RESOURCE_EXIST("工作流节点环境未配置", "Workflow node environment is not configured"),
    WORKFLOW_PRE_TASK_RESULT_NOT_EXIST("工作流前一个节点运行节点获取失败", "Workflow pre task result not exist"),

    /**
     * 作业
     */
    JOB_ADD_ERROR("新增作业失败", "Add job error"),
    JOB_EDIT_ERROR("作业更新失败", "Job modify error"),
    JOB_NOT_EXIST("原作业不存在", "Job not exist"),
    JOB_ID_NOT_EXIST("作业id不存在", "Job id not exist"),
    JOB_TIME_ERROR("作业时间错误", "Job time error"),
    JOB_RUNNING_OR_FINISH("作业正在执行或执行完成不能修改", "Job is running or finish can not modify"),
    JOB_TIME_REPEAT_INTERVAL_ERROR("作业执行重复时，作业时间及执行间隔时间错误", "Job is repeated,the job time or repeat interval error"),
    JOB_TIME_NO_REPEAT_INTERVAL_ERROR("作业执行不重复时，作业时间及执行间隔时间错误", "job is not repeated,the job time or repeat interval error"),
    JOB_NOT_RUNNING("作业并未执行中不能修改", "Job is not running can not modify"),
    JOB_NOT_STOP("作业并未停止中不能修改", "Job is not stop can not modify"),
    JOB_NOT_DELETE("作业运行中不能删除", "Job is running can not delete"),
    JOB_RUNNING_CACHE_CLEAR_ERROR("作业执行缓存清理错误", "Job run cache clear error"),

    SUB_JOB_NOT_EXIST("子作业不存在", "Sub job not exist"),
    SUB_JOB_NOT_RUNNING("子作业非运行状态中不能暂停", "Sub job is not running can not stop"),
    SUB_JOB_NOT_STOP("子作业非暂停状态中不能重启", "Sub job is not stop can not start"),
    SUB_JOB_TERMINATE_NET_PROCESS_ERROR("子作业终止失败", "sub job terminated failed"),
    SUB_JOB_RESTART_FAILED_ERROR("子作业重启失败", "sub job restart failed"),
    SUB_JOB_NOT_DELETE("子作业运行中不能删除", "Sub job is running can not delete"),
    SUB_JOB_ID_NOT_EXIST("子作业id不存在", "Sub job not exist"),
    SUB_JOB_NODE_NOT_DELETE("子作业节点运行中不能删除", "Sub job node is running can not delete"),
    SUB_JOB_NODE_UPDATE_FAIL("子作业节点更新失败", "Sub job node update fail"),
    SUB_JOB_NODE_PUBLISH_FAIL("子作业节点发布失败", "Sub job node publish fail"),


    /**
     * 机构
     */
    ORGANIZATION_NOT_EXIST("机构不存在", "Organization does not exist");

    private final String zh;
    private final String en;

    ErrorMsg(String zh, String en) {
        this.zh = zh;
        this.en = en;
    }

    public String getMsg() {
        if (LanguageContext.get().equals(SysConstant.EN_US)) {
            return this.en;
        }
        return this.zh;
    }
}
