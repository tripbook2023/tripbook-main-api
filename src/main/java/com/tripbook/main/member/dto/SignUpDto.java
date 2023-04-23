package com.tripbook.main.member.dto;



import com.tripbook.main.member.enums.Gender;
import com.tripbook.main.member.enums.MemberRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDto {

    private String email;
    private String name;
    private Gender gender;
    private MemberRole role;
    private Boolean isEnable;


}
