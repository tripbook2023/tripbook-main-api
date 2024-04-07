package com.tripbook.main.member.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.article.enums.ArticleStatus;
import com.tripbook.main.article.repository.ArticleRepository;
import com.tripbook.main.global.dto.ResponseImage;
import com.tripbook.main.global.entity.Image;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.enums.ImageCategory;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.global.repository.ImageRepository;
import com.tripbook.main.global.service.UploadService;
import com.tripbook.main.member.dto.PrincipalMemberDto;
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
	private final ArticleRepository articleRepository;
	private final ImageRepository imageRepository;
	@Qualifier("jwtService")
	private final JwtService jwtService;
	private final UploadService uploadService;

	@Override
	public ResponseMember.Info memberSave(MemberVO member, String deviceValue) {
		Member deleteMember = withdrawalMemberUpdate(member);
		TokenInfo tokenInfo = null;
		ResponseImage.ImageInfo imageInfo;

		if (deleteMember != null) {
			deleteMember.updateStatus(MemberStatus.STATUS_NORMAL);
			tokenInfo = jwtService.saveToken(deleteMember, deviceValue);
			return ResponseMember.Info.builder()
				.message("success")
				.refreshToken(tokenInfo.getRefreshToken())
				.accessToken(tokenInfo.getAccessToken()).build();
		}
		if (!memberValidation(member)) {
			throw new CustomException.MemberAlreadyExist(ErrorCode.MEMBER_NAME_ERROR.getMessage(),
				ErrorCode.MEMBER_NAME_ERROR);
		}
		if (member.getImageFile() != null) {
			imageInfo = uploadService.imageUpload(member.getImageFile(),
				ImageCategory.MEMBER.toString());
			member.setProfile(imageInfo.getUrl());
		} else {
			imageInfo = null;
		}
		Member resultMember = memberRepository.save(new Member(member));
		//이미지  <-> 멤버 연결
		if (imageInfo != null) {
			imageRefIdMapping(imageInfo, resultMember.getId());
		}

		//토큰 응답
		tokenInfo = jwtService.saveToken(resultMember, deviceValue);
		return ResponseMember.Info.builder()
			.message("success")
			.refreshToken(tokenInfo.getRefreshToken())
			.accessToken(tokenInfo.getAccessToken()).build();
	}

	private void imageRefIdMapping(ResponseImage.ImageInfo imageInfo, Long refId) {
		Optional<Image> targetImage = imageRepository.findById(imageInfo.getId());
		targetImage.ifPresent(image -> image.updateRefId(refId));
	}

	private Member withdrawalMemberUpdate(MemberVO member) {
		Member rstMember = memberRepository.findByEmail(member.getEmail());
		if (rstMember == null) {
			return null;
		}
		if (rstMember.getStatus().equals(MemberStatus.STATUS_WITHDRAWAL)) {
			return rstMember;
		}
		return null;
	}

	@Override
	public Optional<Member> memberCertification(MemberVO memberVO) {
		return Optional.ofNullable(memberRepository.findByEmail(memberVO.getEmail()));
	}

	@Override
	public List<ArticleResponseDto.ArticleResponse> memberTempArticleList(String email) {
		Member loginMember = getLoginMemberByEmail(email);
		List<ArticleResponseDto.ArticleResponse> resultList = articleRepository.findAllByStatusAndMemberEmail(
				ArticleStatus.TEMP, email)
			.stream().map(article -> article.toDto(loginMember))
			.collect(Collectors.toList());

		return resultList;
	}

	@Override
	public Page<ArticleResponseDto.ArticleResponse> memberRecentArticleList(String email, Integer page, Integer size) {
		Member loginMember = getLoginMemberByEmail(email);
		Sort pageSort = Sort.by("createdAt").descending();
		Pageable pageable = PageRequest.of(page, size, pageSort);
		return articleRepository.findAllByStatusAndMemberEmail(
			ArticleStatus.ACTIVE, email, pageable).map(article -> article.toDto(loginMember));
	}

	@Override
	public boolean memberNameValidation(MemberVO member) {
		return memberRepository.findByName(member.getName()) != null;
	}

	@Override
	@Transactional
	public void memberUpdate(MemberVO updateMember) {
		if (updateMember.getName() != null) {
			if (memberRepository.findByName(updateMember.getName()) != null) {
				log.error("Already Exist Nickname");
				throw new CustomException.MemberNameAlreadyException(ErrorCode.MEMBER_NAME_ERROR.getMessage(),
					ErrorCode.MEMBER_NAME_ERROR);
			}
		}
		Member byEmail = memberRepository.findByEmail(updateMember.getEmail());
		if (byEmail != null) {
			//Prfile Default Set
			if (updateMember.getProfile() != null) {
				if (updateMember.getProfile().isEmpty()) {
					updateMember.setProfile(null);
				}
			}
			//Profile Save.
			if (updateMember.getImageFile() != null) {
				if (byEmail.getProfile() != null) {
					//Old Profile Image Delete
					//S3 Image Delete
					uploadService.imageDelete(byEmail.getId(), ImageCategory.MEMBER.toString());
				}
				ResponseImage.ImageInfo imageInfo = uploadService.imageUpload(updateMember.getImageFile(),
					ImageCategory.MEMBER.name());

				updateMember.setProfile(imageInfo.getUrl());
				//이미지  <-> 멤버 연결
				imageRefIdMapping(imageInfo, byEmail.getId());

			}
			byEmail.updateMember(updateMember);
		}

	}

	@Override
	@Transactional
	public void memberDelete(MemberVO bindMemberVo) {
		Member rstMember = memberRepository.findByEmail(bindMemberVo.getEmail());
		if (rstMember == null) {
			log.error("MemberNotFound::{}", bindMemberVo.getEmail());
			throw new CustomException.MemberNotFound(ErrorCode.MEMBER_NOTFOUND.getMessage(), ErrorCode.MEMBER_NOTFOUND);
		}
		// rstMember.updateStatus(MemberStatus.STATUS_WITHDRAWAL);
		preDeleteMember(rstMember);
	}

	private void preDeleteMember(Member rstMember) {
		articleRepository.deleteArticleByMember(rstMember);
		memberRepository.delete(rstMember);
	}

	@Override
	public ResponseMember.MemberInfo memberSelect(PrincipalMemberDto principalMemberDto) {
		Member byEmail = memberRepository.findByEmail(principalMemberDto.getEmail());
		Asserts.notNull(byEmail, "MemberIsNull");
		return new ResponseMember.MemberInfo(byEmail);
	}

	@Override
	public MemberVO memberVoSelect(PrincipalMemberDto principalMemberDto) {
		Member byEmail = memberRepository.findByEmail(principalMemberDto.getEmail());
		Asserts.notNull(byEmail, "MemberIsNull");
		return new MemberVO(byEmail);
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
