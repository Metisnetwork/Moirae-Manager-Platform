package com.moirae.rosettaflow.controller;

import com.alibaba.fastjson.JSONObject;
import com.moirae.rosettaflow.common.enums.OrgOrderByEnum;
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
        System.out.println("result = "  + commonGetWithToken("/org/getOrgStats", emptyParameters));
    }

    @Test
    public void getOrgList() throws Exception {
        pageParameters.add("keyword", "");
        pageParameters.add("orderBy", OrgOrderByEnum.NAME.name());
        System.out.println("result = "  + commonGetWithToken("/org/getOrgList", emptyParameters));
    }

    @Test
    public void getOrgDetails() throws Exception {
        emptyParameters.add("identityId", "identity:07c0119cb39e47f497ff581efd48e342");
        System.out.println("result = "  + commonGetWithToken("/org/getOrgDetails", emptyParameters));
    }

    @Test
    public void getUserOrgList() throws Exception {
        System.out.println("result = "  + commonGetWithToken("/org/getUserOrgList", emptyParameters));
    }

    @Test
    public void joinOrg() throws Exception {
        JSONObject req = new JSONObject();
        req.put("identityIp", "192.168.10.154");
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
