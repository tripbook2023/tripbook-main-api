package com.tripbook.main.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.member.dto.RequestMember;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.entity.Survey;
import com.tripbook.main.member.enums.MemberStatus;
import com.tripbook.main.member.repository.MemberRepository;
import com.tripbook.main.member.repository.SurveyRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	private final SurveyRepository surveyRepository;
	private final EntityManager entityManager;

	@Override
	public Member memberSave(Member member) {
		Member findMember = memberValidation(member);
		if (findMember == null) {
			findMember = memberRepository.save(member);
		}
		return findMember;
	}

	@Override
	public Member memberCertification(RequestMember.SignupMember requestMember,
		String memberEmail) {
		Member findMember = memberRepository.findByEmail(memberEmail);
		if (findMember == null) {
			throw new CustomException.MemberNotFound(ErrorCode.MEMBER_NOTFOUND.getMessage(), ErrorCode.MEMBER_NOTFOUND);
		} else if (findMember.getStatus() != MemberStatus.ADDITIONAL_AUTHENTICATION) {
			//이미 인증완료 된 멤버
			throw new CustomException.MemberAlreadyAuthenticate(ErrorCode.MEMBER_ALREADY_AUTHENTICATE.getMessage(),
				ErrorCode.MEMBER_ALREADY_AUTHENTICATE);
		}
		// memberSurveySave(findMember, requestMember.getSignupSurvey());
		updateMember(requestMember, findMember);
		return findMember;
	}

	@Override
	public boolean memberNameValidation(RequestMember.SignupNameValidator requestMember) {
		if (memberRepository.findByName(requestMember.getName()) != null) {
			throw new CustomException.MemberAlreadyExist(ErrorCode.MEMBER_NAME_ERROR.getMessage(),
				ErrorCode.MEMBER_NAME_ERROR);
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

	private void memberSurveySave(Member member, RequestMember.SignupSurvey survey) {
		//@TODO survey내 null 값 체크
		Survey surveyData = Survey.builder()
			.memberId(member)
			.accompany(survey.getAccompany())
			.purpose(survey.getPurpose())
			.period(survey.getPeriod())
			.location(survey.getLocation())
			.transportation(survey.getTransportation())
			.build();
		surveyRepository.save(surveyData);
	}

	private Member memberValidation(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());
		if (findMember != null) {
			//이미 가입 된 유저
			log.info("Member_Already_Exists.");
			return findMember;
		}
		return null;

	}
}
