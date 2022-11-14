package com.yeonjae.mylog.service;

import com.yeonjae.mylog.domain.Post;
import com.yeonjae.mylog.repository.PostRepository;
import com.yeonjae.mylog.request.PostCreate;
import com.yeonjae.mylog.request.PostSearch;
import com.yeonjae.mylog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

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
}
