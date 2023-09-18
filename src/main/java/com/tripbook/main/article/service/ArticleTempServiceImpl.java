package com.tripbook.main.article.service;

import com.tripbook.main.article.dto.ArticleRequestDto;
import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.article.entity.ArticleImage;
import com.tripbook.main.article.entity.ArticleTemp;
import com.tripbook.main.article.entity.ArticleTempImage;
import com.tripbook.main.article.repository.ArticleTempImageRepository;
import com.tripbook.main.article.repository.ArticleTempRepository;
import com.tripbook.main.file.service.UploadService;
import com.tripbook.main.global.entity.Image;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.global.repository.ImageRepository;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleTempServiceImpl implements ArticleTempService{
    private final ArticleTempRepository articleTempRepository;
    private final ArticleTempImageRepository articleTempImageRepository;
    private final ImageRepository imageRepository;
    private final UploadService uploadService;
    private final MemberService memberService;

    @Override
    @Transactional
    public ArticleResponseDto.ArticleResponse saveArticleTemp(ArticleRequestDto.ArticleSaveRequest requestDto, OAuth2User principal) {
        String email = principal.getName();
        Member loginMember = memberService.getLoginMemberByEmail(email);

        if (loginMember == null) {
            throw new CustomException.MemberNotFound(ErrorCode.MEMBER_NOTFOUND.getMessage(),ErrorCode.MEMBER_NOTFOUND);
        }

        ArticleTemp articleTemp = ArticleTemp.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .tagList(requestDto.getTagList())
                .build();

        ArticleResponseDto.ArticleResponse response = articleTemp.toDto(loginMember);

        List<ArticleTempImage> imageList = requestDto.getImageList().stream()
                .map(file -> uploadService.imageUpload(file, "articleTemp"))
                .map(url -> imageRepository.save(Image.builder().url(url).build()))
                .map(image -> articleTempImageRepository.save(ArticleTempImage.builder().image(image).articleTemp(articleTemp).build()))
                .toList();

        response.setImageList(imageList.stream().map(ArticleImage::toDto).toList());

        return response;
    }
}
