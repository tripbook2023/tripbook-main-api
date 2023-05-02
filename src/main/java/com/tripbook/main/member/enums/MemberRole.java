package com.tripbook.main.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
/*
 *회원 등급 정책
  - 일반회원: 가입 후 활동하는 회원
  - 에디터회원: 에디터 신청 합격한 회원
  - 휴면회원: 1년 간 로그인 하지 않은 회원
  - 정지회원: 공지 위반 회원
  - 탈퇴회원: 탈퇴한 회원
 */
@RequiredArgsConstructor
@Getter
public enum MemberRole {

    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_EDITOR("ROLE_EDITOR");

    private final String value;

}