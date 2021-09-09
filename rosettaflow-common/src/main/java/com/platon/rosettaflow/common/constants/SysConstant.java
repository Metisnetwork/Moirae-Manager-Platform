package com.platon.rosettaflow.common.constants;

/**
 * @author admin
 * @date 2021/7/20
 */
public class SysConstant {

    /**
     * 数字常量
     */
    public static final int INT_1 = 1;
    public static final int INT_2 = 2;
    public static final int INT_3 = 3;
    public static final int INT_4 = 3;

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
    public static final String ZH_CN = "zh-CN";
    /**
     * 国际化英文
     */
    public static final String EN_US = "en-US";
    /**
     * 不需要用户登录可以访问的接口
     */
    public static final String[] LOGIN_URIS = {"user/login",
            "user/getLoginNonce",
            "swagger",
            "error",
            "api-docs",
            "data/list",
            "data/detail",
            "data/columnList"
    };

    /**
     * 作业添加队列
     */
    public static final String JOB_ADD_QUEUE = "JOB_ADD_LIST";
    /**
     * 作业修改队列
     */
    public static final String JOB_EDIT_QUEUE = "JOB_ADD_LIST";
    /**
     * 作业暂停队列
     */
    public static final String JOB_PAUSE_QUEUE = "JOB_PAUSE_QUEUE";

}
