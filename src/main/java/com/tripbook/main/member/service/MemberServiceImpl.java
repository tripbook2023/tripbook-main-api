package com.tripbook.main.member.service;

import org.springframework.stereotype.Service;

import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;

	@Override
	public Integer memberSave(Member member) {
		userValidation(member);
		memberRepository.save(member);
		return 1;
	}

	private void userValidation(Member member) {
		if (memberRepository.findByEmail(member.getEmail()) != null) {
			log.error("Member_Already_Exists.");
			throw new CustomException.EmailDuplicateException("email duplicated", ErrorCode.EMAIL_DUPLICATION);
		}
	}
}
