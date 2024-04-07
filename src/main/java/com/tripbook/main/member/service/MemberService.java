package com.tripbook.main.member.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.member.dto.PrincipalMemberDto;
import com.tripbook.main.member.dto.ResponseMember;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.vo.MemberVO;

public interface MemberService {
	public ResponseMember.Info memberSave(MemberVO member, String deviceValue);

	// public Integer memberUpdate()
	public Optional<Member> memberCertification(MemberVO memberVO);

	public List<ArticleResponseDto.ArticleResponse> memberTempArticleList(String email);

	public Page<ArticleResponseDto.ArticleResponse> memberRecentArticleList(String email, Integer page, Integer size);

	public boolean memberNameValidation(MemberVO memberVO);

	void memberUpdate(MemberVO updateMember);

	void memberDelete(MemberVO bindMemberVo);

	ResponseMember.MemberInfo memberSelect(PrincipalMemberDto principalMemberDto);

	MemberVO memberVoSelect(PrincipalMemberDto principalMemberDto);

	Member getLoginMemberByEmail(String email);
}
