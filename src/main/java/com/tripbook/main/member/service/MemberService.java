package com.tripbook.main.member.service;

import com.tripbook.main.member.dto.RequestMember;
import com.tripbook.main.member.entity.Member;

public interface MemberService {
	public Member memberSave(Member member);

	// public Integer memberUpdate()
	public Member memberCertification(RequestMember.SignupMember requestMember, String memberEmail);

	public boolean memberNameValidation(RequestMember.SignupNameValidator requestMember);

}
