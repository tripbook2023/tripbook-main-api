package com.tripbook.main.member.service;

import java.util.Optional;

import com.tripbook.main.member.dto.ResponseMember;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;

public interface MemberService {
	public ResponseMember.Info memberSave(MemberVO member, HttpServletRequest request);

	// public Integer memberUpdate()
	public Optional<Member> memberCertification(MemberVO memberVO);

	public boolean memberNameValidation(MemberVO memberVO);

}
