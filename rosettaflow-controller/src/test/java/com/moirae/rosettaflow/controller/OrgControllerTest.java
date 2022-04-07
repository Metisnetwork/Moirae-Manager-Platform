package com.moirae.rosettaflow.controller;

import com.moirae.rosettaflow.common.enums.OrgOrderByEnum;
import io.swagger.annotations.ApiModelProperty;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "dev")
@AutoConfigureMockMvc
public class OrgControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getOrgStats() throws Exception {
        mvc.perform(get("/org/getOrgStats")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getOrgList() throws Exception {
        Long current = 1L;
        Long size = 10L;
        // 搜索关键字(身份标识、组织名称关键字)
        String keyword = "";
        String orderBy = OrgOrderByEnum.BANDWIDTH.getUserValue();

        mvc.perform(get("/org/getOrgList")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("current", String.valueOf(current))
                        .param("size", String.valueOf(size))
                        .param("keyword", keyword)
                        .param("orderBy", orderBy)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
