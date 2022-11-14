package com.yeonjae.mylog.request;

import lombok.Builder;
import lombok.Getter;

import static java.lang.Math.max;

@Getter
@Builder
public class PostSearch {
    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    public long getOffSet() {
        return (max(1, page)-1) * Math.min(size, 2000);
    }

}
