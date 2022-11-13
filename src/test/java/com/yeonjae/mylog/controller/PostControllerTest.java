package com.yeonjae.mylog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeonjae.mylog.domain.Post;
import com.yeonjae.mylog.repository.PostRepository;
import com.yeonjae.mylog.request.PostCreate;
import jdk.jfr.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // 각각의 테스트는 서로 영향을 주면 안된다.
    // 그래서 @BeforeEach 통해서 사전에 삭제해줌
    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

//    @Test
//    @DisplayName("/v1/posts 요청시 Hello world를 출력한다.")
//    void testV1() throws Exception {
//
//        // expected
//        mockMvc.perform(MockMvcRequestBuilders.post("/v1/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\" }")
//                )
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("Hello world"))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("/v1/posts 요청시 title은 필수 값이다")
//    void testV1Validation () throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/v1/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"title\":\"\", \"content\" : \"내용입니다.\" }"))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("타이틀값은 필수값입니다."));
//    }


    @Test
    @DisplayName("/v2/posts 요청시 Hello world를 출력한다.")
    void testV2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/v2/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(print());
    }

    // jsonPath: json 객체를 탐색하기 위한 표준화된 방법
    @Test
    @DisplayName("/v2/posts 요청시 title은 필수 값이다")
    void testV2Validation () throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/v2/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("/v3/posts 요청시 Hello world를 출력한다.")
    void testV3() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/v3/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    // jsonPath: json 객체를 탐색하기 위한 표준화된 방법
    @Test
    @DisplayName("/v3/posts 요청시 title은 필수 값이다")
    void testV3Validation () throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/v3/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("/v3/posts 요청시 DB에 값이 저장된다.")
    void testV3Write () throws Exception {
        // given
        // 생성자를 사용하면 title과 content는 둘 다 String일 때 두 위치를 바꿔버리게 되면 나중에 버그 찾기가 어려워진다.
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/v3/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());

    }


    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected (when + then)
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("foo"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        // given
        Post post1 = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();

        Post post2 = Post.builder()
                .title("foo2")
                .content("bar2")
                .build();

        postRepository.saveAll(List.of(post1, post2));

        // expected
        mockMvc.perform(get("/posts")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$.[0].id").value(post1.getId()))
                .andExpect(jsonPath("$.[0].title").value("foo1"))
                .andExpect(jsonPath("$.[0].content").value("bar1"))
                .andExpect(jsonPath("$.[1].id").value(post2.getId()))
                .andExpect(jsonPath("$.[1].title").value("foo2"))
                .andExpect(jsonPath("$.[1].content").value("bar2"))
                .andDo(print());
    }



}