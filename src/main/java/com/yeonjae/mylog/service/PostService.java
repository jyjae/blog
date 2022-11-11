package com.yeonjae.mylog.service;

import com.yeonjae.mylog.domain.Post;
import com.yeonjae.mylog.repository.PostRepository;
import com.yeonjae.mylog.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        // PostCreate -> Entity
        Post post = new Post(postCreate.getTitle(), postCreate.getContent());
        // repository.save(postCreate)
        postRepository.save(post);
    }
}
