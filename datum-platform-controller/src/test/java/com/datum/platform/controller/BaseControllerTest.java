package com.datum.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.web3j.crypto.Credentials;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class BaseControllerTest {

    @Autowired
    protected MockMvc mvc;

    protected Credentials user = Credentials.create("68efa6466edaed4918f0b6c3b1b9667d37cad591482d672e8abcb4c5d1720f89");
    protected String accessToken = "16581332841314EFD41B206EF4B18A8B5A2CE7E293715";
    protected String lang = "zh";
    protected MultiValueMap<String, String> emptyParameters = new LinkedMultiValueMap();
    protected MultiValueMap<String, String> pageParameters = new LinkedMultiValueMap();

    {
        pageParameters.add("current", String.valueOf(1));
        pageParameters.add("size", String.valueOf(10));
    }

    protected String commonPostWithToken(String url, String reqBody) throws Exception {
        String result = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(reqBody)
                        .header("Accept-Language",lang)
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        return result;
    }

    protected String commonGetWithToken(String url, MultiValueMap<String, String> parameters)throws Exception{
        String result = mvc.perform(get(url)
                        .params(parameters)
                        .header("Accept-Language",lang)
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        return result;
    }

    protected String commonPost(String url, String reqBody) throws Exception {
        String result = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(reqBody)
                        .header("Accept-Language",lang)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        return result;
    }

    protected String commonGet(String url, MultiValueMap<String, String> parameters)throws Exception{
        String result = mvc.perform(get(url)
                        .params(parameters)
                        .header("Accept-Language",lang)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        return result;
    }
}
