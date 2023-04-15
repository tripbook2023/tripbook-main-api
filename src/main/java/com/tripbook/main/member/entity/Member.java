package com.tripbook.main.member.entity;

import java.io.Serializable;
import java.util.Date;

import com.tripbook.main.global.common.BasicEntity;
import com.tripbook.main.global.enums.Gender;
import com.tripbook.main.global.enums.MemberRole;
import com.tripbook.main.global.enums.MemberStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BasicEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Setter
    @Column
    private Date birth;
    @Setter
    @Column
    private String profile;
    @Column(nullable = false)
    @Setter
    private Boolean isMarketing;
    @Column
    @Setter
    private Long point;
    @Column
    @Setter
    private String AppToken;
    @Column(nullable = false)
    @Setter
    private MemberStatus status;
    @Builder
    public Member(String email, String name, Gender gender, MemberRole role) {
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.role = role;
    }
}