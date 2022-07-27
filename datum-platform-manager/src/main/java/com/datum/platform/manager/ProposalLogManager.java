package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.ProposalLog;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 提案日志 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
public interface ProposalLogManager extends IService<ProposalLog> {

    ProposalLog getLatestOne();

    boolean contain(BigInteger blockNumber, String transactionHash, BigInteger logIndex);

    List<ProposalLog> listByPage(Long latestSynced, Long size);
}
