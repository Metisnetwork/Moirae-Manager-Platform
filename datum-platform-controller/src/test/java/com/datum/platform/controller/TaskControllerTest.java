package com.datum.platform.controller;

import com.datum.platform.common.enums.TaskStatusEnum;
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
public class TaskControllerTest extends BaseControllerTest {

    @Test
    public void getTaskStats() throws Exception {
        System.out.println("result = "  + commonGet("/task/getTaskStats", emptyParameters));
    }

    @Test
    public void getTaskListByOrg() throws Exception {
        pageParameters.add("identityId", "did:pid:lat1eqpf8vxz7m64j25mkmwk39t3xf8zeltxrr2nql");
        System.out.println("result = "  + commonGet("/task/getTaskListByOrg", pageParameters));
    }

    @Test
    public void getTaskListByData() throws Exception {
        pageParameters.add("metaDataId", "metadata:0x68410d1ce7f6befd78aa174b2e87ef502301a3fe4730a3cdff84f20b2c477290");
        System.out.println("result = "  + commonGet("/task/getTaskListByData", pageParameters));
    }

    @Test
    public void getTaskList() throws Exception {
        pageParameters.add("taskStatus", TaskStatusEnum.ALL.name());
        System.out.println("result = "  + commonGet("/task/getTaskList", pageParameters));
    }

    @Test
    public void getTaskDetails() throws Exception {
        emptyParameters.add("taskId", "task:0x01925d3b02bda779711434c5ba56be06facd9dbcb55904738eee032d48f31b0e");
        System.out.println("result = "  + commonGet("/task/getTaskDetails", emptyParameters));
    }
}
