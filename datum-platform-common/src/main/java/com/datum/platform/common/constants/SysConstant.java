package com.datum.platform.common.constants;

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
     * redis数据库 key值 Token前缀
     */
    public static final String REDIS_TOKEN_PREFIX_KEY = "Token:";

    /**
     * 用户非互踢模式时，token已登录用的用户数前缀
     */
    public static final String REDIS_TOKEN_BIND_PREFIX_KEY = "token-bind:";

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
            "data/getNoAttributeCredential",
            "data/getAttributeCredentialList",
            "task/getTaskStats",
            "task/getTaskListByOrg",
            "task/getTaskListByData",
            "task/getTaskList",
            "task/getTaskDetails",
            "workflow/wizard/getCalculationProcessList",
            "workflow/downloadResultFile",
            "sys/getPlatONChainConfig",
            "home/queryNavigation",
            "home/getLatestTaskList",
            "home/getGlobalStats",
            "home/getTaskTrend",
            "home/getOrgPowerTop",
            "home/getDataUsedTop",
            "publicity/getProposalList",
            "publicity/getOrgVcList",
            "publicity/getAuthorityList",
            "publicity/getProposalDetails",
            "swagger",
            "error",
            "api-docs"
    };
}
