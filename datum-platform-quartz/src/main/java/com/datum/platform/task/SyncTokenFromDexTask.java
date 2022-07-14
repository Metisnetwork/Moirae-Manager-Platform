package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.datum.platform.chain.platon.contract.IUniswapV2FactoryContract;
import com.datum.platform.chain.platon.contract.IUniswapV2PairContract;
import com.datum.platform.mapper.domain.Token;
import com.datum.platform.mapper.enums.TokenTypeEnum;
import com.datum.platform.service.DataService;
import com.platon.tuples.generated.Tuple3;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class SyncTokenFromDexTask {

    @Resource
    private DataService dataService;
    @Resource
    private IUniswapV2FactoryContract uniswapV2FactoryDao;
    @Resource
    private IUniswapV2PairContract uniswapV2PairDao;

    @Scheduled(fixedDelay = 60 * 1000)
    @Lock(keys = "SyncTokenFromDexTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            List<String> tokenList = dataService.listERC20TokenAddress();
            tokenList.forEach(item -> {
                try {
                    if(!item.equals(uniswapV2FactoryDao.WETH())){
                        sync(item);
                    }
                } catch (Exception e){
                    log.error("DataToken的dex信息同步, 明细失败：{}",item, e);
                }
            });
        } catch (Exception e) {
            log.error("DataToken的dex信息同步失败原因：{}", e.getMessage(), e);
        }
        log.info("DataToken的dex信息同步，总耗时:{}ms", DateUtil.current() - begin);
    }

    private void sync(String tokenAddress){
        String pairAddress = uniswapV2FactoryDao.getPair(tokenAddress);
        if("0x0000000000000000000000000000000000000000".equals(pairAddress) || "0x0".equals(pairAddress)){
            return;
        }

        Tuple3<BigInteger, BigInteger, BigInteger> tuple3 = uniswapV2PairDao.getReserves(pairAddress);
        if(tuple3.getValue1().compareTo(BigInteger.ZERO) == 0 || tuple3.getValue2().compareTo(BigInteger.ZERO) == 0){
            return;
        }
        Token token = new Token();
        token.setAddress(tokenAddress);
        token.setType(TokenTypeEnum.ERC20);
        token.setIsAddLiquidity(true);
        token.setPrice(getPrice(tokenAddress.compareTo(uniswapV2FactoryDao.WETH()) < 0 ? tuple3.getValue2() : tuple3.getValue1(),
                tokenAddress.compareTo(uniswapV2FactoryDao.WETH()) < 0 ? tuple3.getValue1() : tuple3.getValue2()));
        dataService.updateTokenById(token);
    }

    private String getPrice(BigInteger wEth, BigInteger token) {
        BigDecimal one = BigDecimal.ONE;
        BigDecimal reserve0 = new BigDecimal(wEth);
        BigDecimal reserve1 = new BigDecimal(token);
        BigDecimal inputAmount = new BigDecimal("1000000000000000000");
        BigDecimal feesD = new BigDecimal("10000");
        BigDecimal feesN = new BigDecimal("9975");
        BigDecimal numerator = reserve0.multiply(inputAmount).multiply(feesD);
        BigDecimal denominator = reserve1.subtract(inputAmount).multiply(feesN);
        BigDecimal result = numerator.divide(denominator, 10,  RoundingMode.HALF_DOWN).add(one).divide(inputAmount, 10,  RoundingMode.HALF_DOWN);
        if(result.signum() == 1){
            return result.toPlainString();
        }else {
            return BigDecimal.ZERO.toPlainString();
        }
    }
}
