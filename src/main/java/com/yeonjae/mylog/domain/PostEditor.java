package com.yeonjae.mylog.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
@RequiredArgsConstructor
public class PostEditor {

    private final String title;
    private final String content;


    public static PostEditorBuilder builder() {
        return new PostEditorBuilder();
    }

    public static class PostEditorBuilder {
        private String title;
        private String content;

        PostEditorBuilder() {
        }

        public PostEditorBuilder title(final String title) {
            this.title = title==null? this.title :title;
            return this;
        }

        public PostEditorBuilder content(final String content) {
            this.content = content ==null ? this.content:content;
            return this;
        }

        public PostEditor build() {
            return new PostEditor(this.title, this.content);
        }

        public String toString() {
            return "PostEditor.PostEditorBuilder(title=" + this.title + ", content=" + this.content + ")";
        }
    }
}
