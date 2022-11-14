package com.yeonjae.mylog.service;

import com.yeonjae.mylog.domain.Post;
import com.yeonjae.mylog.domain.PostEditor;
import com.yeonjae.mylog.exception.PostNotFound;
import com.yeonjae.mylog.repository.PostRepository;
import com.yeonjae.mylog.request.PostCreate;
import com.yeonjae.mylog.request.PostEdit;
import com.yeonjae.mylog.request.PostSearch;
import com.yeonjae.mylog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        // PostCreate -> Entity
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        // repository.save(postCreate)
        postRepository.save(post);
    }

    public PostResponse get(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();


    }

    public List<PostResponse> getList(Pageable pageable) {
        //여기서 PostResponse builder를 사용해서 생성하면 나중에 service쪽에 builder부분이 상당히 많아질것이다.
        //  >> PostResponse 쪽에서 생성자 오버로딩을 사용한다.
        //ageRequest pageRequest  = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id"));

        return postRepository.findAll(pageable).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getListByQueryDsl(PostSearch request) {
        return postRepository.getListByQueryDsl(request).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow((PostNotFound::new));

        // 변경
        // 1. setter 금지
        // 2. 만약 post 안에서 바로 변경 시 문제 생길 수 있다
        //  >> Editor 클래스 새로 생성...

        // no 픽스
        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        post.edit(editorBuilder
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build());

        /*
            >> @Transactional 선언적 명시해줌으로써 commit 알아서 해주니깐 생략 가능.
            postRepository.save(post);
         */
    }


    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        // 존재하는 경우
        postRepository.delete(post);
    }
}
