package com.tripbook.main.member.dto;



import com.tripbook.main.member.enums.Gender;
import com.tripbook.main.member.enums.MemberRole;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResponseMember {

    @Builder
    public static class info{
        private Long id;
        private String name;
        private String email;
        private Gender gender;
        private MemberRole role;
    }

}
