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
public class SysControllerTest extends BaseControllerTest {

    @Test
    public void getPlatONChainConfig() throws Exception {
        System.out.println("result = "  + commonGet("/sys/getPlatONChainConfig", emptyParameters));
    }
}
