package com.tripbook.main.member.service;

import java.util.Optional;

import com.tripbook.main.member.dto.RequestMember;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.vo.MemberVO;

public interface MemberService {
	public Member memberSave(Member member);

	// public Integer memberUpdate()
	public Optional<Member> memberCertification(MemberVO memberVO);

	public boolean memberNameValidation(RequestMember.SignupNameValidator requestMember);

}
