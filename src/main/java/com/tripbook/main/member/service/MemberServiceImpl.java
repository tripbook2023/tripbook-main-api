package com.tripbook.main.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.member.dto.RequestMember;
import com.tripbook.main.member.dto.ResponseMember;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.enums.MemberStatus;
import com.tripbook.main.member.repository.MemberRepository;
import com.tripbook.main.member.vo.MemberVO;
import com.tripbook.main.token.dto.TokenInfo;
import com.tripbook.main.token.service.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	private final JwtService jwtService;

	@Override
	public ResponseMember.Info memberSave(MemberVO member, String deviceValue) {
		//중복가입 검사
		if (!memberValidation(member)) {
			throw new CustomException.MemberAlreadyExist(ErrorCode.MEMBER_NAME_ERROR.getMessage(),
				ErrorCode.MEMBER_NAME_ERROR);
		}
		//가입진행
		Member saveMember = memberRepository.save(new Member(member));
		TokenInfo tokenInfo = jwtService.saveToken(saveMember, deviceValue);
		return ResponseMember.Info.builder()
			.message("success")
			.refreshToken(tokenInfo.getRefreshToken())
			.accessToken(tokenInfo.getAccessToken()).build();
	}

	@Override
	public Optional<Member> memberCertification(MemberVO memberVO) {
		return Optional.ofNullable(memberRepository.findByEmail(memberVO.getEmail()));
	}

	@Override
	public boolean memberNameValidation(MemberVO member) {
		if (memberRepository.findByName(member.getName()) != null) {
			return false;
		}
		return true;
	}

	@Transactional
	public void updateMember(RequestMember.SignupMember signupMember, Member findMember) {
		findMember.updateStatus(MemberStatus.STATUS_NORMAL);
		findMember.updateProfile(signupMember.getProfile());
		findMember.updateIsMarketing(signupMember.getIsMarketing());
		findMember.updateName(signupMember.getName());
		findMember.updateBirth(signupMember.getBirth());
		memberRepository.save(findMember);
	}

	private boolean memberValidation(MemberVO member) {
		if (memberRepository.findByEmailOrName(member.getEmail(), member.getName()) != null) {
			log.info("Member_Already_Exists.");
			throw new CustomException.EmailDuplicateException(ErrorCode.EMAIL_DUPLICATION.getMessage(),
				ErrorCode.EMAIL_DUPLICATION);
		}
		return true;

	}
}
