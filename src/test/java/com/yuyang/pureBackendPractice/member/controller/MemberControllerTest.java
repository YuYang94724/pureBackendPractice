package com.yuyang.pureBackendPractice.member.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postMembersLogin() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/members:login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"ste\", \"passoword\": \"123\"}");
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void getMembers() {
    }

    @Test
    void patchMembersAbout() {
    }

    @Test
    void patchMembersPassword() {
    }

    @Test
    void removeMember() {
    }

    @Test
    void getMembersRecords() {
    }
}