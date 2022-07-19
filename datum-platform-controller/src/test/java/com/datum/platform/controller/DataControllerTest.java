package com.datum.platform.controller;

import com.datum.platform.common.enums.DataOrderByEnum;
import com.datum.platform.mapper.enums.MetaDataFileTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "dev")
@AutoConfigureMockMvc
public class DataControllerTest extends BaseControllerTest {

    @Test
    public void getDataStats() throws Exception {
        System.out.println("result = "  + commonGet("/data/getDataStats", emptyParameters));
    }

    @Test
    public void getDataListByOrg() throws Exception {
        pageParameters.add("identityId", "did:pid:lat1eqpf8vxz7m64j25mkmwk39t3xf8zeltxrr2nql");
        System.out.println("result = "  + commonGet("/data/getDataListByOrg", pageParameters));
    }

    @Test
    public void getDataList() throws Exception {
        pageParameters.add("orderBy", DataOrderByEnum.PUBLISHED.name());
        pageParameters.add("fileType", MetaDataFileTypeEnum.CSV.name());
        System.out.println("result = "  + commonGet("/data/getDataList", pageParameters));
    }

    @Test
    public void getDataListByUser() throws Exception {
        pageParameters.add("identityId", "did:pid:lat1eqpf8vxz7m64j25mkmwk39t3xf8zeltxrr2nql");
        System.out.println("result = "  + commonGetWithToken("/data/getDataListByUser", pageParameters));
    }

    @Test
    public void getUserAuthDataList() throws Exception {
        System.out.println("result = "  + commonGetWithToken("/data/getUserAuthDataList", pageParameters));
    }

    @Test
    public void getDataDetails() throws Exception {
        emptyParameters.add("metaDataId", "metadata:0x68410d1ce7f6befd78aa174b2e87ef502301a3fe4730a3cdff84f20b2c477290");
        System.out.println("result = "  + commonGet("/data/getDataDetails", emptyParameters));
    }

    @Test
    public void getNoAttributeCredential() throws Exception {
        emptyParameters.add("metaDataId", "metadata:0x014e3274e01a1fde20f34b95f685cdf8edf234fa68c9f16244ffcb77c82b208e");
        System.out.println("result = "  + commonGet("/data/getNoAttributeCredential", emptyParameters));
    }

    @Test
    public void getAttributeCredentialList() throws Exception {
        emptyParameters.add("metaDataId", "metadata:0x68410d1ce7f6befd78aa174b2e87ef502301a3fe4730a3cdff84f20b2c477290");
        System.out.println("result = "  + commonGet("/data/getAttributeCredentialList", emptyParameters));
    }

    @Test
    public void getUserDatumNetworkLatInfo() throws Exception {
        System.out.println("result = "  + commonGetWithToken("/data/getUserDatumNetworkLatInfo", emptyParameters));
    }

    @Test
    public void getUserNoAttributeCredential() throws Exception {
        emptyParameters.add("metaDataId", "metadata:0x014e3274e01a1fde20f34b95f685cdf8edf234fa68c9f16244ffcb77c82b208e");
        System.out.println("result = "  + commonGetWithToken("/data/getUserNoAttributeCredential", emptyParameters));
    }

    @Test
    public void getUserAttributeCredentialList() throws Exception {
        emptyParameters.add("metaDataId", "metadata:0x68410d1ce7f6befd78aa174b2e87ef502301a3fe4730a3cdff84f20b2c477290");
        System.out.println("result = "  + commonGetWithToken("/data/getUserAttributeCredentialList", emptyParameters));
    }

    @Test
    public void getUserModelList() throws Exception {
        emptyParameters.add("identityId", "did:pid:lat166gjn98vr5lvl3x2lsltsxjspq77arrp2szcfw");
        emptyParameters.add("algorithmId", String.valueOf(2022L));
        System.out.println("result = "  + commonGetWithToken("/data/getUserModelList", emptyParameters));
    }
}
