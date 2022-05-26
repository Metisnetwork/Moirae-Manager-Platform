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
public class AlgControllerTest extends BaseControllerTest {

    @Test
    public void getAlgTree() throws Exception {
        System.out.println("result = "  + commonGet("/alg/getAlgTree", emptyParameters));
    }

    @Test
    public void getAlgTreeDetails() throws Exception {
        System.out.println("result = "  + commonGet("/alg/getAlgTreeDetails", emptyParameters));
    }
}
