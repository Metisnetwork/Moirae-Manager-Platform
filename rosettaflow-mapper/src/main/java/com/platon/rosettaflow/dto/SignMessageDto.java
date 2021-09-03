package com.platon.rosettaflow.dto;


import lombok.Data;

/**
 * @author juzix
 */
@Data
public class SignMessageDto {

    public Domain domain;
    public Message message;
    public String primaryType;
    public Types types;

    @Data
    public static class  Domain {
        String name;
    }

    @Data
    public static class Message {
        String key;
        String desc;
    }

    @Data
    public static class Types {
    }


}



