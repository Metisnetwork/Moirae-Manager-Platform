package com.datum.platform.chain.platon.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class VoteConfigDto {
    /**
     * 投票开始
     */
    private BigInteger beginVote;
    /**
     * 投票周期
     */
    private BigInteger vote;
    /**
     * 退出周期
     */
    private BigInteger quit;
}
