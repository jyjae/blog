package com.yeonjae.mylog.service;

import com.yeonjae.mylog.domain.Post;
import com.yeonjae.mylog.repository.PostRepository;
import com.yeonjae.mylog.request.PostCreate;
import com.yeonjae.mylog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());

    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        // given
        Post requestPost = Post.builder()
                .title("1234567891011121314")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // 클라이언트 요구사항
            // 응답 json title 값을 최대 10자로 해주세요.

        // when
        PostResponse response = postService.get(requestPost.getId());

        //then
        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("1234567891", response.getTitle());
        assertEquals("bar", response.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test3() {
        // given
        Post requestPost1 = Post.builder()
                .title("1234567891011121314")
                .content("bar")
                .build();

        Post requestPost2 = Post.builder()
                .title("1234567891011121314")
                .content("bar")
                .build();

        postRepository.saveAll(List.of(requestPost1, requestPost2));

        // when
        List<PostResponse> responses = postService.getList();

        // then
        assertEquals(2L, responses.size());

    }

}