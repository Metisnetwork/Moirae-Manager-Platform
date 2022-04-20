package com.moirae.rosettaflow.chain.platon;

import com.moirae.rosettaflow.chain.platon.contract.evm.DataTokenFactory;
import com.platon.bech32.Bech32;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class DataTokenFactoryTest extends BaseContractTest {

    private String templateAddress = "lat1hhfwhezjwdlfx7tv7ss6a7g94r2sgpvpy53cmd";
    private String factoryAddress = "lat1cma0rlan2e3lfvvmycukc5aeus6cz78zdu2fmc";

    private List<String> dataIdList = Arrays.asList("metadata:0xeae5caf8a8fc93139febf6025929627e1960010eae08c5b4b60c37d545702795",
            "metadata:0x3862767893eafc91029689c9eaf6b68fb209b1930e735c25e523d0215e1bda9f",
            "metadata:0xf27ac94efd0be30748c8232356f153e3727b896b1834ad094771aba59dfdc0fb",
            "metadata:0xeee12d13c32e8babbd873b3d052c9f78f6786ec162db3591d35319c061cffd4a",
            "metadata:0x9ad82fc6ebc9b7c39d2491fcc2ad46556a3d507b7f9a9d4665c173b8da066605",
            "metadata:0x1d9f31d585404d1ad7d926526cd6a0cc630f0db49f583a3c3d9d10b9a80ead61",
            "metadata:0xe2d03ead9d8089147350ecd352b32c84e5e295049c42ae7cce6ae0d4c36f3aa2",
            "metadata:0xfe6dfc49ee867cea2649f2b0a5d19c03fb990be0c32d53ebc434177182a7c354",
            "metadata:0x65b1ae7e819b443413f46dd22c80b3f7bf24f36cf18512c033eee3096a847044",
            "metadata:0x02190e3811e595f70cf7ca360765530462887ff7a8d09ed429c089eeef28930c",
            "metadata:0x4c20858f152b13d36773c588ec9424e2001a4886732005e6edbd301825397bb6",
            "metadata:0xae78afc3d2d24ce861f47f64a95f42607d8d2ba15fb2c0762ce63e3a93151c86",
            "metadata:0x492a94c069ae59a34dedfc1154f4f45bfd24611a328aa7a48c44a3fa8cee6df4",
            "metadata:0xd494b8e243983e34eaf2c44618bd04667a74be62b6ffb448939a343c791736c9",
            "metadata:0xf5c18d1c257ae1fd8447c9b16bb06f24519cfe1d6b82595b65d6157d23e57f9e",
            "metadata:0x31c55ef787b99c55d1290139b4b78584d5ad10709865336aa7675d93171a5a7e",
            "metadata:0xb00c07cc759b27142032da5063c78d145a0ef016c48a78a1b269f91aad3d710c",
            "metadata:0xc5ba11f6aa3335bd8dd228c234f7378e7aae38977d57670de5ab83df1730ae3b",
            "metadata:0x22763921a546cedf1c6833388566c57382fd45a290d673aa90277f7a6f4e022e",
            "metadata:0x1abcfa434c6b18b14d5123436de4441321d13e1a721a0138cef237a9a7d09a06",
            "metadata:0x06949dc1c73f793c2f461e211ac073f9a34626b03befdf9db2bd95a9a6659aff",
            "metadata:0x80a9f6690ce083a4ea0a719a51242603b832b79806f3b59b481a27db395ffd4a",
            "metadata:0x4bd2c7cd173643fe0c0635ca554d3fa22b6d0f9878aef9e3b60510ff104c9cc3",
            "metadata:0xae6ffd54d7da8b1ba1f515d14a402d3a97364f7194d4c500b784589e8c8b0561",
            "metadata:0xf9b071ca62c601832a96fb34e3faa3c521ed7531493e9e404d912bfd4e9a7186",
            "metadata:0x2ad9571d4777005db0c48682d93e0d9e11e99ca09267019d05def0ea6f6e6562");

    private String sqlTemp = "UPDATE `dc_meta_data` SET `token_address` = '${A}' WHERE `meta_data_id` = '${B}'; \r\n";

    @Test
    public void creatToken() {
        int index = 1;
        StringBuilder result = new StringBuilder();
        DataTokenFactory dataTokenFactory = load();
        for (String dataId: dataIdList) {
            try {
                result.append(StringUtils.replace(StringUtils.replace(sqlTemp, "${A}", creatToken(dataTokenFactory, dataId, index++ )), "${B}", dataId));
            } catch (Exception e){
               e.printStackTrace();
            }
        }
        System.out.println(result);
    }

    private String creatToken(DataTokenFactory dataTokenFactory, String dataId, Integer index) throws Exception {
        TransactionReceipt transactionReceipt = dataTokenFactory.createToken("DT-N-" + index,"DT-S-" + index, new BigInteger("500000000000000000000000000"), new BigInteger("100000000000000000000000000"), dataId).send();
        List<DataTokenFactory.TokenCreatedEventResponse> eventEvents = dataTokenFactory.getTokenCreatedEvents(transactionReceipt);
        return Bech32.addressDecodeHex(eventEvents.get(0).newTokenAddress);
    }

    @Test
    public void deploy() throws Exception{
        DataTokenFactory contract = DataTokenFactory.deploy(web3j, credentials, gasProvider, templateAddress).send();
        System.out.println(contract.getContractAddress());
        System.out.println(Bech32.addressDecodeHex(contract.getContractAddress()));
        // lat1cma0rlan2e3lfvvmycukc5aeus6cz78zdu2fmc
        // 0xc6faf1ffb35663f4b19b26396c53b9e4358178e2
    }

    private DataTokenFactory load(){
        return DataTokenFactory.load(factoryAddress, web3j, credentials, gasProvider);
    }
}
