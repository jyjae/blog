package com.yeonjae.mylog.repository;

import com.yeonjae.mylog.domain.Post;
import com.yeonjae.mylog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getListByQueryDsl(PostSearch postSearch);
}
