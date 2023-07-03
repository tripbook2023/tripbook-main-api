package com.tripbook.main.article.enums;

import lombok.Getter;

public enum ArticleCommentStatus {
    ACTIVE("활성"),
    DELETED("삭제");

    @Getter
    private String desc;

    ArticleCommentStatus(String desc) {
        this.desc = desc;
    }

}
