package com.datum.platform.controller;

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
        System.out.println("result = "  + commonGetWithToken("/data/getDataStats", emptyParameters));
    }

    @Test
    public void getDataListByOrg() throws Exception {
        pageParameters.add("identityId", "identity:3ddb63047d214ddd8187438a82841250");
        System.out.println("result = "  + commonGetWithToken("/data/getDataListByOrg", pageParameters));
    }

    @Test
    public void getUserDataList() throws Exception {
        pageParameters.add("identityId", "identity:17c9cc15b6a14f858a96a633c3486f3d");
        System.out.println("result = "  + commonGetWithToken("/data/getUserDataList", pageParameters));
    }

    @Test
    public void getUserDatumNetworkLatInfo() throws Exception {
        System.out.println("result = "  + commonGetWithToken("/data/getUserDatumNetworkLatInfo", emptyParameters));
    }

    @Test
    public void getUserModelList() throws Exception {
        emptyParameters.add("identityId", "identity:3ddb63047d214ddd8187438a82841250");
        emptyParameters.add("algorithmId", String.valueOf(2021L));
        System.out.println("result = "  + commonGetWithToken("/data/getUserModelList", emptyParameters));
    }
}
