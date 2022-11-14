package com.yeonjae.mylog.service;

import com.yeonjae.mylog.domain.Post;
import com.yeonjae.mylog.exception.PostNotFound;
import com.yeonjae.mylog.repository.PostRepository;
import com.yeonjae.mylog.request.PostCreate;
import com.yeonjae.mylog.request.PostEdit;
import com.yeonjae.mylog.request.PostSearch;
import com.yeonjae.mylog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

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
    @DisplayName("글 페이징 처리 조회")
    void test3() {
        // given
        List<Post> requestPosts = IntStream.range(1,31)
                        .mapToObj(i ->
                                Post.builder()
                                        .title("제목 - "+i)
                                        .content("내용 - "+i)
                                        .build()
                        ).collect(Collectors.toList());


        postRepository.saveAll(requestPosts);

        Pageable pageable = PageRequest.of(0,5, DESC, "id");

        // when
        List<PostResponse> responses = postService.getList(pageable);

        // then
        assertEquals(5L, responses.size());
        assertEquals("제목 - 30", responses.get(0).getTitle());
        assertEquals("내용 - 26", responses.get(4).getContent());

    }

    @Test
    @DisplayName("글 querydsl 페이징 처리 조회")
    void test4() {
        // given
        List<Post> requestPosts = IntStream.range(1,31)
                .mapToObj(i -> Post.builder()
                                .title("제목 - "+i)
                                .content("내용 - "+i)
                                .build()
                ).collect(Collectors.toList());


        postRepository.saveAll(requestPosts);

        PostSearch request = PostSearch.builder()
                .build();

        // when
        List<PostResponse> responses = postService.getListByQueryDsl(request);

        // then
        assertEquals(10L, responses.size());
        assertEquals("제목 - 30", responses.get(0).getTitle());
        assertEquals("내용 - 26", responses.get(4).getContent());

    }

    @Test
    @DisplayName("제목 수정")
    void test5() {
        // given
        Post post =  postRepository.save(
                Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build());

        PostEdit request = PostEdit.builder()
                .title("제목입니다. 하하하하")
                .content("내용입니다.")
                .build();

        // when
        postService.edit(post.getId(), request);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 수정되지 않았습니다. id=" + post.getId()));

        assertEquals("제목입니다. 하하하하", changedPost.getTitle());
        assertEquals("내용입니다.", changedPost.getContent());

    }

    @Test
    @DisplayName("내용 수정")
    void test6() {
        // given
        Post post =  postRepository.save(
                Post.builder()
                        .title("제목입니다.")
                        .content("내용입니다.")
                        .build());

        PostEdit request = PostEdit.builder()
                .title("제목입니다.")
                .content("내용입니다. 하하하")
                .build();

        // when
        postService.edit(post.getId(), request);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 수정되지 않았습니다. id=" + post.getId()));

        assertEquals("제목입니다.", changedPost.getTitle());
        assertEquals("내용입니다. 하하하", changedPost.getContent());

    }

    @Test
    @DisplayName("글 삭제")
    void test7() {
        // given
        Post post =  postRepository.save(
                Post.builder()
                        .title("제목입니다.")
                        .content("내용입니다.")
                        .build());
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertEquals(0, postRepository.count());

    }

    @Test
    @DisplayName("글 1개 조회 - 존재하지 않는 글")
    void test8() {
        // given
        Post post = Post.builder()
                .title("1234567891011121314")
                .content("bar")
                .build();
        postRepository.save(post);


        // expected
        assertThrows(PostNotFound.class, () -> postService.get(post.getId()+1L));
    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test9() {
        // given
        Post post =  postRepository.save(
                Post.builder()
                        .title("제목입니다.")
                        .content("내용입니다.")
                        .build());
        postRepository.save(post);

        // expected
        assertThrows(PostNotFound.class, () ->postService.delete(post.getId()+1L));

    }

    @Test
    @DisplayName("게시글 수정 - 존재하지 않는 글")
    void test10() {
        // given
        Post post =  postRepository.save(
                Post.builder()
                        .title("제목입니다.")
                        .content("내용입니다.")
                        .build());
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("제목입니다. 하핳")
                .content("내용입니다.")
                .build();

        // expected
        assertThrows(PostNotFound.class, () ->postService.edit(post.getId()+1L, postEdit));

    }






}