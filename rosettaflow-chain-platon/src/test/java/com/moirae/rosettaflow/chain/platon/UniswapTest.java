package com.moirae.rosettaflow.chain.platon;

import com.moirae.rosettaflow.chain.platon.contract.evm.IUniswapV2Factory;
import com.moirae.rosettaflow.chain.platon.contract.evm.IUniswapV2Pair;
import com.moirae.rosettaflow.chain.platon.contract.evm.IUniswapV2Router02;
import com.moirae.rosettaflow.chain.platon.utils.AddressUtils;
import com.platon.bech32.Bech32;
import com.platon.tuples.generated.Tuple3;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class UniswapTest extends BaseContractTest {

    private String address = AddressUtils.hexToBech32("0x4378daf745e9053f6048c4f78c803f3bc8829703");


    /**
     * WLAT at: 0xa0C63FAC4e5425F4721FF3258c3FA5B381152F73
     *
     * DipoleFactory at: 0xA44D7cdf71f53f2bcB4cb31618fbe532BD9A2d5c
     * DipoleRouter at: 0x26D637E206Cc39942628421e7B0D6Fb41dB0bC06
     *
     * lat1aa3hvtgr9sqym7qehfhs98rk0vps4zgmnsu8dt
     * 0xef63762d032c004df819ba6f029c767b030a891b
     */
    @Test
    public void check() throws Exception{
//        IUniswapV2Router02 router = loadRouter();
//        IUniswapV2Factory factory = loadFactory(router.factory().send());
//        IUniswapV2Pair pair = loadPair(factory.getPair(router.WETH().send(), "lat1aa3hvtgr9sqym7qehfhs98rk0vps4zgmnsu8dt").send());
//        //Tuple3{value1=20000000000000000000, value2=20000000000000000000, value3=4255551100}
//        System.out.println(pair.getReserves().send());

        IUniswapV2Router02 router = loadRouter();

        IUniswapV2Factory factory = loadFactory(router.factory().send());
        IUniswapV2Pair pair = loadPair(factory.getPair(router.WETH().send(), Bech32.addressEncode(hrp,"0x355b39ad02068e7e0189b5df2df1818ad72dc64b")).send());
        //Tuple3{value1=20000000000000000000, value2=20000000000000000000, value3=4255551100}

        Tuple3<BigInteger, BigInteger, BigInteger> tuple3 = pair.getReserves().send();

        System.out.println("0x355b39ad02068e7e0189b5df2df1818ad72dc64b".compareTo("0xa0c63fac4e5425f4721ff3258c3fa5b381152f73"));
        System.out.println("0xad716b2d1adb6d8a508326a7c2e328db8b154da0".compareTo("0xa0c63fac4e5425f4721ff3258c3fa5b381152f73"));
        System.out.println("0xe19cfd8f9173155c26149818abd5decaa6f705f3".compareTo("0xa0c63fac4e5425f4721ff3258c3fa5b381152f73"));
        System.out.println("0xe88695d3a3ba03ee6bb2130ffd7869a8e368a0b4".compareTo("0xa0c63fac4e5425f4721ff3258c3fa5b381152f73"));

//        System.out.println(getPrice(tuple3.getValue1(), tuple3.getValue2()));
//        System.out.println(pair.price0CumulativeLast().send());
//        System.out.println(pair.price1CumulativeLast().send());

//        System.out.println(router.getAmountIn(pair.getReserves().send().getValue1(), pair.getReserves().send().getValue2(), new BigInteger("10000000000000000")).send());
    }

    private String getPrice(BigInteger wEth, BigInteger token) {
        BigDecimal reserve0 = new BigDecimal(wEth);
        BigDecimal reserve1 = new BigDecimal(token);
        System.out.println("reserve0 = " + reserve0);
        System.out.println("reserve1 = " + reserve1);
        return reserve0.divide(reserve1, 18,  RoundingMode.HALF_DOWN).toString();
    }


    @Test
    public void add2Dex() throws Exception{
        IUniswapV2Router02 router = loadRouter();

        String tokenList = "0x282875fd9579367c44a8b2666d65f75f8c589fdb\n" +
                "0xfc23df226ca45f9a2c527d8313e27395964060fa\n" +
                "0xb74c8525dfb15598687c80be6615fb243c2eeb25\n" +
                "0x148d92fc65b04c60840793be11b9f1b558e6eb9e\n" +
                "0xbb54ec09ec1433fa48716effc94bbe9a950aecef\n" +
                "0x90d308f5e6ab2d087983ac4d5c8ced533ed7681a\n" +
                "0xdd4f9e77c206c64343140e25f5bf56dec6d81750\n" +
                "0xfc4239a8dfe02c0d6db4a2b0c9ce8d11fd80b73c\n" +
                "0x33f919a0f6312bfd225f797a4808683958660e3a\n" +
//                "0xab94a2338ca24d05e2cc6318ecbd083eabbe473c\n" +
//                "0x4ab735a85751534ce7d2bf733bc083232573fb06\n" +
//                "0xe1b66c3ef7ca24e9b37b0ec38868fdb67881696f\n" +
//                "0x1981e3ab9dd60eae70ae83d3d1b236c2662eae8a\n" +
//                "0xb9ef5fd080839d3eb04809c0f69db709dd9b5f69\n" +
//                "0xfa0bea7347b9b98e068528da5826cf45a9c6075e\n" +
//                "0xcacc4ca37e0cd5d162455a753cf07bcdbc26281b\n" +
//                "0x1e19040bae09c01d06e82ebd83c801308959daed\n" +
//                "0xc91ef95ac29cfafce0b0b714051e5add99b64c87\n" +
//                "0x8e801527c44929da704b7cd3406142f5391fe1f7\n" +
//                "0x2fa1f9217fead87874ff63715c29af10d78f741e\n" +
//                "0x4be1ae0984ddb224c863c1e20fec05c0b86c7d3d\n" +
//                "0xa4f375aa5b51de1510709c8fdfbe20cc4aa8790c\n" +
//                "0xb861eea15a711c19574dde3c582278e4e06a6482\n" +
//                "0x2d0602fd33ff61e24a886060c13a3c619aa6b61e\n" +
                "0x15950be327f74885f64ec3c5f0367ea643a4a6c9";

        String[] tokenArray = tokenList.split("\n");

        for (String token: tokenArray) {
            router.addLiquidityETH(Bech32.addressEncode(hrp,token),
                    new BigInteger("10000000000000000000"),
                    new BigInteger("10000000000000000000"),
                    new BigInteger("10000000000000000000"),
                    credentials.getAddress(),  BigInteger.valueOf(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of("+8")).toEpochMilli()),
                    new BigInteger("10000000000000000000")).send();
        }
    }


    private IUniswapV2Router02 loadRouter(){
        return IUniswapV2Router02.load(address, web3j, credentials, gasProvider);
    }

    private IUniswapV2Factory loadFactory(String address){
        return IUniswapV2Factory.load(address, web3j, credentials, gasProvider);
    }

    private IUniswapV2Pair loadPair(String address){
        System.err.println(Bech32.addressDecodeHex(address));
        return IUniswapV2Pair.load(address, web3j, credentials, gasProvider);
    }

}
