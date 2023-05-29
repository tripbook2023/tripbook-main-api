package com.tripbook.main.member.service;

import java.util.Optional;

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
import com.tripbook.main.member.vo.MemberVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	private final SurveyRepository surveyRepository;

	@Override
	public Member memberSave(Member member) {
		Member findMember = memberValidation(member);
		if (findMember == null) {
			findMember = memberRepository.save(member);
		}
		return findMember;
	}

	@Override
	public Optional<Member> memberCertification(MemberVO memberVO) {
		return Optional.ofNullable(memberRepository.findByEmail(memberVO.getEmail()));
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
