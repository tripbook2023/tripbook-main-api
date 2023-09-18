package com.tripbook.main.article.enums;

import lombok.Getter;

public enum ArticleStatus {
    ACTIVE("활성"),
    PRE_JUDGEMENT("본인작성"),
    ON_JUDGEMENT("심사중"),
    APPROVED("승인"),
    REJECTED("거절"),
    DELETED("삭제"),
    TEMP("임시저장");
    @Getter
    private String desc;

    ArticleStatus(String desc) {
        this.desc = desc;
    }
}
