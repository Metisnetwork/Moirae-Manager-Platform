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
public class PublicityControllerTest extends BaseControllerTest {

    @Test
    public void getAuthorityList() throws Exception {
        System.out.println("result = "  + commonGet("/publicity/getAuthorityList", pageParameters));
    }


    @Test
    public void getProposalList() throws Exception {
        System.out.println("result = "  + commonGet("/publicity/getProposalList", pageParameters));
    }

    @Test
    public void getProposalDetails() throws Exception {
        emptyParameters.add("id", "1");
        System.out.println("result = "  + commonGet("/publicity/getProposalDetails", emptyParameters));
    }

}
