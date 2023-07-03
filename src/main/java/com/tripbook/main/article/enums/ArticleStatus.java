package com.tripbook.main.article.enums;

import lombok.Getter;

public enum ArticleStatus {
    ACTIVE("활성"),
    DELETED("삭제");
    @Getter
    private String desc;

    ArticleStatus(String desc) {
        this.desc = desc;
    }
}
