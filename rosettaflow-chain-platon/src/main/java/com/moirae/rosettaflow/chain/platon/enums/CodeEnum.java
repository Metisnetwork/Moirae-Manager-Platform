package com.moirae.rosettaflow.chain.platon.enums;

public enum CodeEnum {
    // 通用异常
    RET_SUCCESS(0,"success"),
    INNER_ERROR(1,"system error"),
    NO_NODE_AVAILABLE(2,"no nodes available"),
    PARAM_ERROR(3,"parameter format error【%s】"),
    APP_CONFIG_ERRORS(4,"application configuration error【%s】"),
    BALANCE_NOT_SUFFICIENT(5,"insufficient account balance！ balance【%s】 fee【%s】"),
    PASSWORD_ERROR(6,"wrong password"),

    // rpc调用时异常
    CALL_RPC_ERROR(2000,"call node rpc exception"),
    CALL_RPC_NET_ERROR(2001,"call node rpc network exception"),
    CALL_RPC_BIZ_ERROR(2002,"call node rpc business exception【%s】"),
    CALL_RPC_READ_TIMEOUT(2003,"call node rpc read timeout"),

    // 交易异常
    TX_KNOWN_TX(3000,"known transaction"),
    TX_NONCE_TOO_LOW(3001,"nonce too low"),
    TX_INSUFFICIENT_FUND(3002,"insufficient balance"),
    TX_EXCEEDS_BLOCK_GAS_LIMIT(3003,"exceeds block gas limit"),
    ;

    private int code;
    private String name;

    CodeEnum(int code, String name){
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

}
