package com.yeonjae.mylog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/v1/posts 요청시 Hello world를 출력한다.")
    void testV1() throws Exception {

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\" }")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello world"))
                .andDo(print());
    }

    @Test
    @DisplayName("/v1/posts 요청시 title은 필수 값이다")
    void testV1Validation () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\", \"content\" : \"내용입니다.\" }"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello world"));
    }


    @Test
    @DisplayName("/v2/posts 요청시 Hello world를 출력한다.")
    void testV2() throws Exception {

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/v2/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\" }")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{}"))
                .andDo(print());
    }

    // jsonPath: json 객체를 탐색하기 위한 표준화된 방법
    @Test
    @DisplayName("/v2/posts 요청시 title은 필수 값이다")
    void testV2Validation () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v2/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\", \"content\" : \"내용입니다.\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("/v3/posts 요청시 Hello world를 출력한다.")
    void testV3() throws Exception {

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/v3/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\" }")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{}"))
                .andDo(print());
    }

    // jsonPath: json 객체를 탐색하기 위한 표준화된 방법
    @Test
    @DisplayName("/v3/posts 요청시 title은 필수 값이다")
    void testV3Validation () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v3/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\", \"content\" : \"내용입니다.\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

}