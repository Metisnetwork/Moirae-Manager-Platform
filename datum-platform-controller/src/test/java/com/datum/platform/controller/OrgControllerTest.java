package com.datum.platform.controller;

import com.alibaba.fastjson.JSONObject;
import com.datum.platform.common.enums.OrgOrderByEnum;
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
public class OrgControllerTest extends BaseControllerTest {

    @Test
    public void getOrgStats() throws Exception {
        System.out.println("result = "  + commonGet("/org/getOrgStats", emptyParameters));
    }

    @Test
    public void getOrgList() throws Exception {
        pageParameters.add("keyword", "");
        pageParameters.add("orderBy", OrgOrderByEnum.NAME.name());
        System.out.println("result = "  + commonGet("/org/getOrgList", emptyParameters));
    }

    @Test
    public void getOrgDetails() throws Exception {
        emptyParameters.add("identityId", "did:pid:lat1eqpf8vxz7m64j25mkmwk39t3xf8zeltxrr2nql");
        System.out.println("result = "  + commonGet("/org/getOrgDetails", emptyParameters));
    }

    @Test
    public void getBaseOrgList() throws Exception {
        System.out.println("result = "  + commonGetWithToken("/org/getBaseOrgList", emptyParameters));
    }

    @Test
    public void getUserOrgList() throws Exception {
        System.out.println("result = "  + commonGetWithToken("/org/getUserOrgList", emptyParameters));
    }

    @Test
    public void joinOrg() throws Exception {
        JSONObject req = new JSONObject();
        req.put("identityIp", "192.168.10.156");
        req.put("identityPort", 10033);
        System.out.println("result = "  + commonPostWithToken("/org/joinOrg", req.toJSONString()));
    }

    @Test
    public void quitOrg() throws Exception {
        JSONObject req = new JSONObject();
        req.put("identityId", "identity:403931f2e18c4be2915e229b9065a208");
        System.out.println("result = "  + commonPostWithToken("/org/quitOrg", req.toJSONString()));
    }
}
