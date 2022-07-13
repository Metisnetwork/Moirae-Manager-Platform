package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.datum.platform.chain.platon.contract.DataTokenTemplateContract;
import com.datum.platform.chain.platon.contract.ERC721TemplateContract;
import com.datum.platform.chain.platon.contract.IUniswapV2FactoryContract;
import com.datum.platform.common.exception.BusinessException;
import com.datum.platform.mapper.domain.Token;
import com.datum.platform.mapper.enums.TokenTypeEnum;
import com.datum.platform.service.DataService;
import com.platon.contracts.ppos.dto.common.ErrorCode;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class SyncTokenTask {

    @Resource
    private DataService dataService;
    @Resource
    private DataTokenTemplateContract dataTokenTemplateDao;
    @Resource
    private IUniswapV2FactoryContract uniswapV2FactoryDao;
    @Resource
    private ERC721TemplateContract erc721TemplateContract;

//    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncTokenTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            Token token = dataService.getTokenById(uniswapV2FactoryDao.WETH());
            if(token == null){
                token = new Token();
                token.setType(TokenTypeEnum.ERC20);
                token.setAddress(uniswapV2FactoryDao.WETH());
                dataService.saveToken(token);
            }

            List<Token> tokenList = dataService.listTokenByNeedSyncedInfo(1000);

            tokenList.forEach(item -> {
                try {
                    sync(item);
                } catch (Exception e){
                    log.error("DataToken信息同步, 明细失败：{}",item.getAddress(), e);
                }
            });
        } catch (Exception e) {
            log.error("DataToken信息同步失败原因：{}", e.getMessage(), e);
        }
        log.info("DataToken信息同步，总耗时:{}ms", DateUtil.current() - begin);
    }

    private void sync(Token token){
        switch (token.getType()){
            case ERC20:
                token.setSymbol(dataTokenTemplateDao.symbol(token.getAddress()));
                token.setName(dataTokenTemplateDao.name(token.getAddress()));
                BigInteger decimals = dataTokenTemplateDao.decimals(token.getAddress());
                if(decimals.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0){
                    token.setDecimal(decimals.longValue());
                }
                break;
            case ERC721:
                token.setSymbol(erc721TemplateContract.symbol(token.getAddress()));
                token.setName(erc721TemplateContract.name(token.getAddress()));
                break;
            default:
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "未知类型");
        }

        dataService.updateTokenById(token);
    }
}
