package com.yeonjae.mylog.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeonjae.mylog.domain.Post;
import com.yeonjae.mylog.domain.QPost;
import com.yeonjae.mylog.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getListByQueryDsl(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffSet())
                .orderBy(QPost.post.id.desc())
                .fetch();
    }
}
