package com.datum.platform.chain.platon.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AuthorityDto {
    String address;
    String serviceUrl;
    Date joinTime;
}
