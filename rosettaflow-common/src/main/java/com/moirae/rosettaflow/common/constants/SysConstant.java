package com.moirae.rosettaflow.common.constants;

/**
 * @author admin
 * @date 2021/7/20
 */
public class SysConstant {

    /**
     * 数字常量
     */
    public static final int INT_0 = 0;
    public static final int INT_1 = 1;
    public static final int INT_2 = 2;
    public static final int INT_3 = 3;
    public static final int INT_4 = 4;

    /**
     * 换算单位
     */
    public static final int INT_60 = 60;
    public static final int INT_1024 = 1024;
    public static final int INT_1000 = 1000;
    public static final int INT_3600 = 3600;

    /**
     * 角色数据
     */
    public static final byte[] ROLE_BYTE_ARR = {1, 2, 3};

    /**
     * 批量处理方式
     */
    public static final String INSERT = "INSERT";
    public static final String UPDATE = "UPDATE";

    /**
     * 启动工作流前是否需要整体保存：0-否，1-是
     */
    public static final String STR_0 = "0";
    public static final String STR_1 = "1";

    /**
     * 请求头token key值
     */
    public static final String HEADER_TOKEN_KEY = "Access-Token";

    /**
     * 请求头 国际化 language key值
     */
    public static final String HEADER_LANGUAGE_KEY = "Accept-Language";

    /**
     * redis数据库 key值 用户前缀
     */
    public static final String REDIS_USER_PREFIX_KEY = "User:";

    /**
     * redis数据库 用户NONCE前缀 Nonce:{address}:{nonce}
     */
    public static final String REDIS_USER_NONCE_KEY = "Nonce:{}:{}";

    /**
     * redis数据库 用户项目权限 Nonce:{usrId}:{projectId} value:role
     */
    public static final String REDIS_USER_PROJECT_ROLE_KEY = "ROLE:USER-{}:PROJECT-{}";

    /**
     * redis数据库 key值 Token前缀
     */
    public static final String REDIS_TOKEN_PREFIX_KEY = "Token:";

    /**
     * redis数据库 key值
     */
    public static final String REDIS_SYNC_USER_METADATA_PREFIX_KEY = "sync_user_metadata";

    /**
     * 用户非互踢模式时，token已登录用的用户数前缀
     */
    public static final String REDIS_TOKEN_BIND_PREFIX_KEY = "token-bind:";

    /**
     * 系统默认日期格式
     */
    public static final String DEFAULT_DATE_PATTERN = "yyyy/MM/dd";

    /**
     * 系统默认时间格式
     */
    public static final String DEFAULT_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时区默认东八区北京时间
     */
    public static final String DEFAULT_TIMEZONE = "GMT+8";

    public static final String CLASSPATH = "classpath:";

    /**
     * 国际化中文
     */
    public static final String ZH_CN = "zh";
    /**
     * 国际化英文
     */
    public static final String EN_US = "en";
    /**
     * 不需要用户登录可以访问的接口
     */
    public static final String[] LOGIN_URIS = {
            "user/login",
            "user/getLoginNonce",
            "alg/getAlgTree",
            "alg/getAlgTreeDetails",
            "org/getOrgStats",
            "org/getOrgList",
            "org/getOrgDetails",
            "data/getDataStats",
            "data/getDataListByOrg",
            "data/getDataList",
            "data/getDataDetails",

            "swagger",
            "error",
            "api-docs",
            "data/pageList",
            "data/detail",
            "data/columnList",
            "data/listDataFileByIdentityId",
            "data/getDataFile",
            "data/listMetaDataColumn",
            "algorithm/list",
            "algorithm/details",
            "projectTemplate/list",
            "testGrpc",
            "redis",
            "quartz",
            "task/getOrgTaskListByIdentityId",
            "task/getTaskByMetaDataId",
            "task/getTaskDetails",
            "task/getTaskEventList",
            "home/dataTrend",
            "home/globalStats"
    };

    /**
     * 作业添加队列
     */
    public static final String JOB_ADD_QUEUE = "job_add_queue";
    /**
     * 作业修改队列
     */
    public static final String JOB_EDIT_QUEUE = "job_edit_queue";
    /**
     * 作业暂停队列
     */
    public static final String JOB_PAUSE_QUEUE = "job_pause_queue";

    /**
     * 待处理工作流前缀
     */
    public static final String REDIS_WORKFLOW_PREFIX_KEY = "workflowDtoTask:";

    /**
     * 待处理作业节点前缀
     */
    public static final String REDIS_SUB_JOB_PREFIX_KEY = "subJobDtoTask:";

}
