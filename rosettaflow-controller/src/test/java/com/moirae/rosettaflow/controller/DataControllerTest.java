package com.moirae.rosettaflow.controller;

import com.alibaba.fastjson.JSONObject;
import com.moirae.rosettaflow.common.enums.DataOrderByEnum;
import com.moirae.rosettaflow.common.enums.OrgOrderByEnum;
import com.moirae.rosettaflow.mapper.enums.MetaDataFileTypeEnum;
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

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "dev")
@AutoConfigureMockMvc
public class DataControllerTest {

    @Autowired
    private MockMvc mvc;

    private String accessToken = "16488952050141F9C69601BB84C1CAFBB6F7F140F9CD6";

    @Test
    public void getDataStats() throws Exception {
        mvc.perform(get("/data/getDataStats")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getDataListByOrg() throws Exception {
        Long current = 1L;
        Long size = 10L;
        String identityId = "identity:3ddb63047d214ddd8187438a82841250";

        mvc.perform(get("/data/getDataListByOrg")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("current", String.valueOf(current))
                        .param("size", String.valueOf(size))
                        .param("identityId", identityId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getDataList() throws Exception {
        Long current = 1L;
        Long size = 10L;
        //搜索关键字(凭证名称、元数据id)
        String keyword = "";
        String orderBy = DataOrderByEnum.PUBLISHED.name();
        String industry = "";
        String fileType = MetaDataFileTypeEnum.CSV.name();
        Long minSize = null;
        Long maxSize = null;

        mvc.perform(get("/data/getOrgList")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("current", String.valueOf(current))
                        .param("size", String.valueOf(size))
                        .param("keyword", keyword)
                        .param("orderBy", orderBy)
                        .param("industry", industry)
                        .param("fileType", fileType)
                        .param("minSize", null)
                        .param("maxSize", null)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
