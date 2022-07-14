package com.datum.platform.chain.platon;

import com.datum.platform.chain.platon.contract.evm.ERC721Factory;
import com.datum.platform.chain.platon.utils.AddressUtils;
import com.platon.bech32.Bech32;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ERC721FactoryTest extends BaseContractTest {

    private String factoryAddress = "lat1n6demns656sxpetnjdkcl9e97uzykzgtyg7c4l";

    @Test
    public void creatToken() throws Exception {
        ERC721Factory dataTokenFactory = load();

        List<String> metadataIdList = new ArrayList<>();
        metadataIdList.add("metadata:0x014e3274e01a1fde20f34b95f685cdf8edf234fa68c9f16244ffcb77c82b208e");
        metadataIdList.add("metadata:0x096b4bc3a33ab90d555162039bc0cb81cbbe7db051d6f22d5ee2b1dc5018c05e");
        metadataIdList.add("metadata:0x11590a7d8ca86204609f06b3d3f7bf9ec02abe42d2a87069443e73dd1a766c3b");
        metadataIdList.add("metadata:0x2c651e59b1f7d1138bce76405aceba19743d57dd61be9d9c4adec5ac183a754b");
        metadataIdList.add("metadata:0x311a144bd25d4480b9cb28c3b3a3d2896e384df4a6ecd1ed1c247a32d6431b54");
        metadataIdList.add("metadata:0x33db13be150feb0a18dc84bfc3fba8f7a17d2f6d33f1af10cfebec4889e7e401");
        metadataIdList.add("metadata:0x48f963c37fd5ce924788ce96ae3c214dd8be79d4975fc58bfe89e3732d5432f4");
        metadataIdList.add("metadata:0x4fa1ae327b1d1c99bb8aff71c53d0f70aabbb09994f0bfb771642949ee8193fd");
        metadataIdList.add("metadata:0x57f519fa7c2f09e9b329ca10028e72c7d0878e3187dbbeb7ad7f79af92fd4a89");
        metadataIdList.add("metadata:0x68410d1ce7f6befd78aa174b2e87ef502301a3fe4730a3cdff84f20b2c477290");
        metadataIdList.add("metadata:0x71747832b155ecfad1a75cf2c9b60210c62910f76926ca9bc0b9540400f7a68f");
        metadataIdList.add("metadata:0x9947e9d4044eea4cd69fdb0b03f9785bbb381d18ed45905a935648741e1821c7");
        metadataIdList.add("metadata:0xa6c007ff49c36c0ce224b455a44fce24cd2d7ff022ef0a42a84bb7c081d81c4c");
        metadataIdList.add("metadata:0xac821b778f8df53fff30d3549ea48c6dde7f373bbe5473ae9d9d7f68023b372e");
        metadataIdList.add("metadata:0xb2d99d4fc215b8658aa145d59f855fc5f737799bc265b5f443600fda8edf122b");
        metadataIdList.add("metadata:0xd5f120b242ccbde61b60ce9270784601c816b2b7147d3376e720a7a97f0c8195");
        metadataIdList.add("metadata:0xdeca802a572c2083378f5283818786a92cc7cd92d71e74f74c7397fe74602398");
        metadataIdList.add("metadata:0xdf5d201df00c809a15d59cb98877c3045fa9fbd1498d0ba0077d68b00431c3bd");
        metadataIdList.add("metadata:0xe14b0a011476198cad99ad51ae448c9f6df5fa2fbb6ab79e052aff23fda1ddfe");

        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < metadataIdList.size(); i++) {
            TransactionReceipt transactionReceipt = dataTokenFactory.deployERC721Contract("Ncd721-"+i, "Scd721-"+i, metadataIdList.get(i), BigInteger.valueOf(3)).send();
            List<ERC721Factory.NFTContractCreatedEventResponse> eventList = dataTokenFactory.getNFTContractCreatedEvents(transactionReceipt);
            eventList.forEach(item -> {
                resultList.add(item.newTokenAddress);
            });
        }

        for (int i = 0; i < metadataIdList.size(); i++) {
            System.out.println(metadataIdList.get(i) + " " + resultList.get(i));
        }
    }

    @Test
    public void test() throws Exception {
        List<String> addressList = new ArrayList<>();
        addressList.add("lat1d5td2hm2cq9kekel0grht0m3uv7vnk75j8tgrx");
        addressList.add("lat1ua3eqaqfr9c7sxcaxl77l72ujxtjck3f8z4vq2");
        addressList.add("lat1f0ynjyt6ezzal2ke5najuwpkkm9uec38r8e5kz");
        addressList.add("lat1savrxa9pwpucfh93etk9rpwdfryevek0jdddya");
        addressList.add("lat1vde5vfwre2wt3fsccpev2jmkjqkyuhzca6nuft");
        addressList.add("lat1077qxpvusy7hj3xt33tqagsy7rhs34ldxfyn4f");
        addressList.add("lat1pss3dh4s3vqd6qym3xzr6f7zdtrmfscjtayf08");
        addressList.add("lat1405c3cgz0nvrlz4zvgj4mt0e4s9qs8kwv4957v");
        addressList.add("lat10580sp3vwq24h9e400wdvc835dqge030ycelzj");
        addressList.add("lat1z6hnkejz23ycm0mc2yfv2sed7tml29pydzjytp");
        addressList.add("lat1gw6dcnmsl9ad49vedlwjjvtrvu2d65fkugkrnp");
        addressList.add("lat1r9pgsayjdg4np7magwpk5dpwz6crtqyme3zk7c");
        addressList.add("lat1924y4az9f97pl8g97v8lnj466jk8wu46d9np4f");
        addressList.add("lat1j5c3rhakmdkmfktxfx44w6ttrsspyvl7gy5vwe");
        addressList.add("lat1nmmr6r4wg50ualjrfksh0mrlp2vswd64rasg7r");
        addressList.add("lat1r4my4sev470kj47nnp0pdypt68nx9axu7w07h7");
        addressList.add("lat1dp9cpa4eu9wvfhmtk79qfpf47063zna76q9prr");
        addressList.add("lat1eu9e9yg5t7wz883vp57dun5mxmc5tps8h5ylu0");
        addressList.add("lat1vfasjhj5w2dm4cpl8fsscspy05v7yw8qurk4hl");

        for (String address : addressList) {
            System.out.println(Bech32.addressDecodeHex(address));
        }
    }



    private ERC721Factory load(){
        return ERC721Factory.load(factoryAddress, web3j, credentials, gasProvider);
    }
}
