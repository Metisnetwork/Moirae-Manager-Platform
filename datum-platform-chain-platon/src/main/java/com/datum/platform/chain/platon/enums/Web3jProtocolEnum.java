package com.datum.platform.chain.platon.enums;

import lombok.Getter;

public enum Web3jProtocolEnum {
    WS("ws://"),
    WSS("wss://"),
    HTTP("http://"),
    HTTPS("https://");
    @Getter
    private String head;
    Web3jProtocolEnum(String head){
        this.head = head;
    }
}
