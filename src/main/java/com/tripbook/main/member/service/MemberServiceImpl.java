package com.tripbook.main.member.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripbook.main.file.service.UploadService;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.member.dto.PrincipalMemberDto;
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
	@Qualifier("jwtService")
	private final JwtService jwtService;
	private final UploadService uploadService;
	@Value("${file.upload_path.signup}")
	private String path;

	@Override
	public ResponseMember.Info memberSave(MemberVO member, String deviceValue) {
		//중복가입 검사
		if (!memberValidation(member)) {
			throw new CustomException.MemberAlreadyExist(ErrorCode.MEMBER_NAME_ERROR.getMessage(),
				ErrorCode.MEMBER_NAME_ERROR);
		}
		//프로필 이미지 저장
		if (member.getImageFile() != null) {
			String profileURL = uploadService.imageUpload(member.getImageFile(), path);
			member.setProfile(profileURL);
		}
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
		return memberRepository.findByName(member.getName()) != null;
	}

	@Override
	@Transactional
	public void memberUpdate(MemberVO updateMember) {
		if (memberRepository.findByName(updateMember.getName()) != null) {
			log.error("Already Exist Nickname");
			throw new CustomException.MemberNameAlreadyException(ErrorCode.MEMBER_NAME_ERROR.getMessage(),
				ErrorCode.MEMBER_NAME_ERROR);
		} else {
			Member byEmail = memberRepository.findByEmail(updateMember.getEmail());
			byEmail.updateMember(updateMember);
		}
	}

	@Override
	@Transactional
	public void memberDelete(MemberVO bindMemberVo) {
		/*
		 임시 Delete Member Service
		 @TODO - 후에는 실제 삭제가 아닌 MemberStatus 탈퇴처리 진행 (현재는 편의상 실제 DB데이터삭제)
		 */
		memberRepository.deleteByEmail(bindMemberVo.getEmail());
	}

	@Override
	public ResponseMember.MemberInfo memberSelect(PrincipalMemberDto principalMemberDto) {
		Member member = memberRepository.findByEmail(principalMemberDto.getEmail());
		return new ResponseMember.MemberInfo(member);
	}

	@Transactional
	public void updateMember(RequestMember.MemberReqInfo signupMember, Member findMember) {
		if (signupMember.getImageFile() != null) {
			String profileURL = uploadService.imageUpload(signupMember.getImageFile(), path);
			findMember.updateProfile(profileURL);

		}
		findMember.updateStatus(MemberStatus.STATUS_NORMAL);
		findMember.updateMarketingConsent(signupMember.getMarketingConsent());
		findMember.updateName(signupMember.getName());
		// findMember.updateBirth(signupMember.getBirth());
		memberRepository.save(findMember);
	}

	private boolean memberValidation(MemberVO member) {
		if (memberRepository.findByEmail(member.getEmail()) != null) {
			throw new CustomException.EmailDuplicateException(ErrorCode.EMAIL_DUPLICATION.getMessage(),
				ErrorCode.EMAIL_DUPLICATION);
		}
		if (memberRepository.findByName(member.getName()) != null) {
			throw new CustomException.MemberNameAlreadyException(ErrorCode.MEMBER_NAME_ERROR.getMessage(),
				ErrorCode.MEMBER_NAME_ERROR);
		}
		return true;

	}

	@Override
	public Member getLoginMemberByEmail(String email) {
		return memberRepository.findByEmail(email);
	}
}
