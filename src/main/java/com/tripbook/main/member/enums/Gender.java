package com.tripbook.main.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Gender {

    GENDER_MALE("GENDER_MALE"),
    GENDER_FEMALE("GENDER_FEMALE");

    private final String value;
}
